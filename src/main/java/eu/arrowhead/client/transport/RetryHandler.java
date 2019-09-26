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

    private int maxRetries = 1;
    private long delayBetweenRetries = 1;
    private TimeUnit timeUnitForRetries = TimeUnit.SECONDS;

    public void setMaxRetries(final int maxRetries)
    {
        this.maxRetries = maxRetries;
    }

    public void setDelayBetweenRetries(final long delayBetweenRetries, final TimeUnit timeUnitForRetries)
    {
        this.delayBetweenRetries = delayBetweenRetries;
        this.timeUnitForRetries = timeUnitForRetries;
    }

    public <T> T invokeWithErrorHandler(final TransportInvocation<T> method, final VoidTransportInvocation errorMethod) throws TransportException
    {
        Throwable throwable = null;
        for (int i = 0; i < maxRetries; i++)
        {
            try
            {
                return method.invoke();
            }
            catch (final Exception e)
            {
                errorMethod.invoke();
                logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
                throwable = e;
            }

            logger.info("Sleeping {} {} before retry ...", delayBetweenRetries, timeUnitForRetries.name());
            ThreadUtils.sleep(delayBetweenRetries, timeUnitForRetries);
        }

        LogUtils.printLongStackTrace(logger, Level.ERROR, throwable);

        if (throwable instanceof TransportException)
        {
            throw (TransportException) throwable;
        }

        throw new TransportException(throwable);
    }

    public <T> T invoke(final TransportInvocation<T> method) throws TransportException
    {
        Throwable throwable = null;
        for (int i = 0; i < maxRetries; i++)
        {
            try
            {
                return method.invoke();
            }
            catch (final Exception e)
            {
                logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
                throwable = e;
            }

            logger.info("Sleeping {} {} before retry ...", delayBetweenRetries, timeUnitForRetries.name());
            ThreadUtils.sleep(delayBetweenRetries, timeUnitForRetries);
        }

        LogUtils.printLongStackTrace(logger, Level.ERROR, throwable);

        if (throwable instanceof TransportException)
        {
            throw (TransportException) throwable;
        }

        throw new TransportException(throwable);
    }

    public void invokeVoid(final VoidTransportInvocation method) throws TransportException
    {
        Throwable throwable = null;
        for (int i = 0; i < maxRetries; i++)
        {
            try
            {
                method.invoke();
            }
            catch (final Exception e)
            {
                logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
                throwable = e;
            }
        }

        LogUtils.printLongStackTrace(logger, Level.ERROR, throwable);

        if (throwable instanceof TransportException)
        {
            throw (TransportException) throwable;
        }

        throw new TransportException(throwable);
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
