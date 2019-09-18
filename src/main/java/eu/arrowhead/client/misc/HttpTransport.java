package eu.arrowhead.client.misc;

import eu.arrowhead.client.utils.LogUtils;
import eu.arrowhead.client.utils.ThreadUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class HttpTransport implements Transport
{
    private final Logger logger = LogManager.getLogger();
    private final RestTemplate restTemplate;
    private int maxRetries;
    private long delayBetweenRetries;
    private TimeUnit timeUnitForRetries;

    public HttpTransport()
    {
        restTemplate = new RestTemplate();
    }

    @Override
    public void setMaxRetries(final int maxRetries)
    {
        this.maxRetries = maxRetries;
    }

    @Override
    public void setDelayBetweenRetries(final long delayBetweenRetries, final TimeUnit timeUnitForRetries)
    {
        this.delayBetweenRetries = delayBetweenRetries;
        this.timeUnitForRetries = timeUnitForRetries;
    }

    private <T> T invoke(final TransportInvocation<T> method) throws TransportException
    {
        Throwable throwable = null;
        for (int i = 0; i < maxRetries; i++)
        {
            try
            {
                return method.invoke();
            }
            catch (final RestClientException e)
            {
                logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
                throwable = e;
            }

            logger.info("Sleeping {} {} before retry ...", delayBetweenRetries, timeUnitForRetries.name());
            ThreadUtils.sleep(delayBetweenRetries, timeUnitForRetries);
        }

        LogUtils.printShortStackTrace(logger, Level.ERROR, throwable);
        throw new TransportException(throwable);
    }

    private void invokeVoid(final VoidInvocation method) throws TransportException
    {
        Throwable throwable = null;
        for (int i = 0; i < maxRetries; i++)
        {
            try
            {
                method.invoke();
            }
            catch (final RestClientException e)
            {
                logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
                throwable = e;
            }
        }

        LogUtils.printShortStackTrace(logger, Level.ERROR, throwable);
        throw new TransportException(throwable);
    }

    @Override
    public <T> T get(final Class<T> clz, final URI uri) throws TransportException
    {
        return invoke(() -> restTemplate.getForEntity(uri, clz).getBody());
    }

    @Override
    public <T> T get(final Class<T> clz, final URI uri, final Object... pathParameters) throws TransportException
    {
        return invoke(() -> restTemplate.getForEntity(uri.toASCIIString(), clz, pathParameters).getBody());
    }


    @Override
    public <T, B> T post(final Class<T> clz, final URI uri, final B body) throws TransportException
    {
        return invoke(() -> restTemplate.postForEntity(uri, body, clz).getBody());
    }

    @Override
    public <T, B> T post(final Class<T> clz, final URI uri, final B body, final Object... pathParameters) throws TransportException
    {
        return invoke(() -> restTemplate.postForEntity(uri.toASCIIString(), body, clz, pathParameters).getBody());
    }

    @Override
    public <B> void put(final URI uri, final B body) throws TransportException
    {
        invokeVoid(() -> restTemplate.put(uri, body));
    }

    @Override
    public <B> void put(final URI uri, final B body, final Object... pathParameters) throws TransportException
    {
        invokeVoid(() -> restTemplate.put(uri.toASCIIString(), body, pathParameters));
    }

    @Override
    public <T, B> T put(final Class<T> cls, final URI uri, final B body) throws TransportException
    {
        return invoke(() -> restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(body), cls)).getBody();
    }

    @Override
    public <T, B> T put(final Class<T> cls, final URI uri, final B body, final Object... pathParameters) throws TransportException
    {
        return invoke(() -> restTemplate.exchange(uri.toASCIIString()
                , HttpMethod.PUT, new HttpEntity<>(body), cls, pathParameters)).getBody();
    }

    @Override
    public void delete(final URI uri) throws TransportException
    {
        invokeVoid(() -> restTemplate.delete(uri));
    }

    @Override
    public void delete(final URI uri, final Object... pathParameters) throws TransportException
    {
        invokeVoid(() -> restTemplate.delete(uri.toASCIIString(), pathParameters));
    }
}
