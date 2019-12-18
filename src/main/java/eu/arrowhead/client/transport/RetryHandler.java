package eu.arrowhead.client.transport;

import eu.arrowhead.client.utils.LogUtils;
import eu.arrowhead.client.utils.ThreadUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class RetryHandler
{
    private final Logger logger = LogManager.getLogger();

    private int maxRetries = 5;
    private long delayBetweenRetries = 200;
    private TimeUnit timeUnitForRetries = TimeUnit.MILLISECONDS;

    public void setMaxRetries(final int maxRetries)
    {
        this.maxRetries = maxRetries;
    }

    public void setDelayBetweenRetries(final long delayBetweenRetries, final TimeUnit timeUnitForRetries)
    {
        this.delayBetweenRetries = delayBetweenRetries;
        this.timeUnitForRetries = timeUnitForRetries;
    }

    private TransportException rethrowException(final Throwable throwable)
    {
        LogUtils.printShortStackTrace(logger, Level.ERROR, throwable);

        if (throwable instanceof TransportException)
        {
            return (TransportException) throwable;
        }

        return new TransportException(throwable);
    }

    public <T> T invokeWithErrorHandler(final TransportInvocation<T> method, final VoidTransportInvocation errorMethod) throws TransportException
    {
        Throwable throwable = null;
        for (int i = 0; i <= maxRetries; i++)
        {
            try
            {
                return method.invoke();
            }
            catch (final Exception e)
            {
                logger.warn("{}: {} - Invoking error method", e.getClass().getSimpleName(), e.getMessage());
                errorMethod.invoke();
                throwable = e;
            }

            if (i < maxRetries)
            {
                logger.info("Sleeping {} {} before retry ...", delayBetweenRetries, timeUnitForRetries.name());
                ThreadUtils.sleep(delayBetweenRetries, timeUnitForRetries);
            }
        }
        throw rethrowException(throwable);
    }

    public void invokeVoid(final VoidTransportInvocation method) throws TransportException
    {
        Throwable throwable = null;
        for (int i = 0; i <= maxRetries; i++)
        {
            try
            {
                method.invoke();
                break;
            }
            catch (final Exception e)
            {
                logger.warn("{}: {} - Invoking error method", e.getClass().getSimpleName(), e.getMessage());
                throwable = e;
            }

            if (i < maxRetries)
            {
                logger.info("Sleeping {} {} before retry ...", delayBetweenRetries, timeUnitForRetries.name());
                ThreadUtils.sleep(delayBetweenRetries, timeUnitForRetries);
            }
        }
        throw rethrowException(throwable);
    }

    public <T> T invoke(final TransportInvocation<T> method) throws TransportException
    {
        return invokeWithErrorHandler(method, this::doNothing);
    }

    private void doNothing()
    {
        // intentionally empty
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("TransportHandler [");
        sb.append("maxRetries=").append(maxRetries);
        sb.append(", delayBetweenRetries=").append(delayBetweenRetries);
        sb.append(", timeUnitForRetries=").append(timeUnitForRetries);
        sb.append(']');
        return sb.toString();
    }

}
