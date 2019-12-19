package eu.arrowhead.client.transport.http;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.arrowhead.client.transport.RetryHandler;
import eu.arrowhead.client.transport.SecureTransport;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.utils.security.SSLContextConfigurator;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.net.URI;
import java.util.concurrent.TimeUnit;

public class HttpTransport implements SecureTransport, Transport
{
    private final Logger logger = LogManager.getLogger();
    private final RestTemplate restTemplate;
    private RetryHandler retryHandler;

    public HttpTransport()
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        mapper.setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.ALWAYS, JsonInclude.Include.NON_NULL));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        final MappingJackson2HttpMessageConverter jackson = new MappingJackson2HttpMessageConverter(mapper);

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(jackson);
        restTemplate.getInterceptors().add(new LoggingInterceptor());
        restTemplate.setRequestFactory(createRequestFactory(createHttpClient()));

        retryHandler = new RetryHandler();
    }

    @Override
    public void setRetryHandler(final RetryHandler retryHandler)
    {
        this.retryHandler = retryHandler;
    }

    private void closeRequestFactory(final ClientHttpRequestFactory requestFactory)
    {
        if (requestFactory instanceof HttpComponentsClientHttpRequestFactory)
        {
            try
            {
                final HttpClient httpClient = ((HttpComponentsClientHttpRequestFactory) requestFactory).getHttpClient();
                if (httpClient instanceof CloseableHttpClient)
                {
                    ((CloseableHttpClient) httpClient).close();
                }

                ((HttpComponentsClientHttpRequestFactory) requestFactory).destroy();
            }
            catch (Exception e)
            {
                logger.warn(e.getMessage(), e);
            }
        }

    }

    private HttpComponentsClientHttpRequestFactory createRequestFactory(final HttpClient httpClient)
    {
        final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectionRequestTimeout(30000);
        requestFactory.setConnectTimeout(30000);
        requestFactory.setReadTimeout(30000);
        return requestFactory;
    }

    private SocketConfig createSocketConfig()
    {
        return SocketConfig.custom()
                           .setTcpNoDelay(true)
                           .setSoReuseAddress(false)
                           .setSoTimeout(10000)
                           .build();
    }

    private HttpClient createHttpClient()
    {
        return HttpClients.custom()
                          .setConnectionReuseStrategy(new NoConnectionReuseStrategy())
                          .setConnectionTimeToLive(1, TimeUnit.MINUTES)
                          .build();
    }

    private HttpClient createHttpClient(final SSLContext sslContext)
    {
        return createHttpClient(sslContext, SSLContextConfigurator.NoopHostnameVerifier.INSTANCE);
    }

    private HttpClient createHttpClient(final SSLContext sslContext, final HostnameVerifier verifier)
    {
        return HttpClients.custom()
                          .setDefaultSocketConfig(createSocketConfig())
                          .setSSLContext(sslContext)
                          .setSSLHostnameVerifier(verifier)
                          .setRetryHandler(new CustomRetryHandler())
                          .setConnectionReuseStrategy(new NoConnectionReuseStrategy())
                          .setConnectionTimeToLive(1, TimeUnit.MINUTES)
                          .build();
    }

    @Override
    public void setSSLContext(final SSLContext sslContext)
    {
        closeRequestFactory(restTemplate.getRequestFactory());
        restTemplate.setRequestFactory(createRequestFactory(createHttpClient(sslContext)));
    }

    @Override
    public void setSSLContext(final SSLContext sslContext, final HostnameVerifier verifier)
    {
        closeRequestFactory(restTemplate.getRequestFactory());
        restTemplate.setRequestFactory(createRequestFactory(createHttpClient(sslContext, verifier)));
    }

    @Override
    public <T> T get(final Class<T> cls, final URI uri) throws TransportException
    {
        try
        {
            retryHandler.invoke(() -> restTemplate.getForEntity(uri, cls).getBody());
            logger.info("Invoking method: {} getForEntity( {})", cls.getSimpleName(), uri.toASCIIString());
            final T returnValue = retryHandler.invoke(() -> restTemplate.getForEntity(uri, cls).getBody());
            logger.info("Returning from invocation with {}", returnValue);
            return returnValue;
        }
        catch (final TransportException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new TransportException(e);
        }
    }

    @Override
    public <T> T get(final Class<T> cls, final URI uri, final Object... pathParameters) throws TransportException
    {
        try
        {
            logger.info("Invoking method: {} getForEntity({}, {})", cls.getSimpleName(), uri.toASCIIString(), pathParameters);
            final T returnValue = retryHandler.invoke(() -> restTemplate.getForEntity(uri.toASCIIString(), cls, pathParameters).getBody());
            logger.info("Returning from invocation with {}", returnValue);
            return returnValue;
        }
        catch (final TransportException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new TransportException(e);
        }
    }


    @Override
    public <T, B> T post(final Class<T> cls, final URI uri, final B body) throws TransportException
    {
        try
        {
            logger.info("Invoking method: {} getForEntity({}, {})", cls.getSimpleName(), uri.toASCIIString(), body);
            final T returnValue = retryHandler.invoke(() -> restTemplate.postForEntity(uri, body, cls).getBody());
            logger.info("Returning from invocation with {}", returnValue);
            return returnValue;
        }
        catch (final TransportException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new TransportException(e);
        }
    }

    @Override
    public <T, B> T post(final Class<T> cls, final URI uri, final B body, final Object... pathParameters) throws TransportException
    {
        try
        {
            logger.info("Invoking method: {} getForEntity({}, {}, {})", cls.getSimpleName(), uri.toASCIIString(), body, pathParameters);
            final T returnValue = retryHandler.invoke(() -> restTemplate.postForEntity(uri.toASCIIString(), body, cls, pathParameters).getBody());
            logger.info("Returning from invocation with {}", returnValue);
            return returnValue;
        }
        catch (final TransportException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new TransportException(e);
        }
    }

    @Override
    public <B> void put(final URI uri, final B body) throws TransportException
    {
        try
        {
            logger.info("Invoking method: void put({}, {})", uri.toASCIIString(), body);
            retryHandler.invokeVoid(() -> restTemplate.put(uri, body));
        }
        catch (final TransportException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new TransportException(e);
        }
    }

    @Override
    public <B> void put(final URI uri, final B body, final Object... pathParameters) throws TransportException
    {
        try
        {
            logger.info("Invoking method: void put({}, {}, {})", uri.toASCIIString(), body, pathParameters);
            retryHandler.invokeVoid(() -> restTemplate.put(uri.toASCIIString(), body, pathParameters));
            logger.info("Returning from void invocation");
        }
        catch (final TransportException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new TransportException(e);
        }
    }

    @Override
    public <T, B> T put(final Class<T> cls, final URI uri, final B body) throws TransportException
    {
        try
        {
            logger.info("Invoking method: {} exchange(PUT)({}, {})", cls.getSimpleName(), uri.toASCIIString(), body);
            final T returnValue = retryHandler.invoke(() -> restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(body), cls).getBody());
            logger.info("Returning from invocation with {}", returnValue);
            return returnValue;
        }
        catch (final TransportException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new TransportException(e);
        }
    }

    @Override
    public <T, B> T put(final Class<T> cls, final URI uri, final B body, final Object... pathParameters) throws TransportException
    {
        try
        {
            logger.info("Invoking method: {} exchange(PUT)({}, {}, {})", cls.getSimpleName(), uri.toASCIIString(), body, pathParameters);
            final T returnValue = retryHandler
                    .invoke(() -> restTemplate.exchange(uri.toASCIIString(), HttpMethod.PUT, new HttpEntity<>(body), cls, pathParameters).getBody());
            logger.info("Returning from invocation with {}", returnValue);
            return returnValue;
        }
        catch (final TransportException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new TransportException(e);
        }
    }

    @Override
    public void delete(final URI uri) throws TransportException
    {
        try
        {
            logger.info("Invoking method: void delete({})", uri.toASCIIString());
            retryHandler.invokeVoid(() -> restTemplate.delete(uri));
            logger.info("Returning from void invocation");
        }
        catch (final TransportException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new TransportException(e);
        }
    }

    @Override
    public void delete(final URI uri, final Object... pathParameters) throws TransportException
    {
        try
        {
            logger.info("Invoking method: void delete({}, {})", uri.toASCIIString(), pathParameters);
            retryHandler.invokeVoid(() -> restTemplate.delete(uri.toASCIIString(), pathParameters));
            logger.info("Returning from void invocation");
        }
        catch (final TransportException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new TransportException(e);
        }
    }

    @Override
    public String toString()
    {
        return "HttpTransport []";
    }

    private static class CustomRetryHandler extends DefaultHttpRequestRetryHandler
    {
        private CustomRetryHandler()
        {
            super(3, true);
        }

        protected boolean handleAsIdempotent(final HttpRequest request)
        {
            return true;
        }
    }
}
