package eu.arrowhead.client.transport.http;

import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;

public class LoggingInterceptor implements ClientHttpRequestInterceptor
{
    private final Logger logger;

    public LoggingInterceptor(final Logger logger)
    {

        this.logger = logger;
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("Logging request: {} {}: {}", request.getMethodValue(), request.getURI(), new String(body));
            final ClientHttpResponse clientHttpResponse = execution.execute(request, body);
            final BufferingResponseWrapper wrapper = new BufferingResponseWrapper(clientHttpResponse);
            final String bodyString = new String(StreamUtils.copyToByteArray(wrapper.getBody()));
            logger.debug("Logging response: {}: {}", clientHttpResponse.getStatusText(), bodyString);
            return wrapper;
        }
        else { return execution.execute(request, body); }
    }

    private String byteToString(byte[] chars)
    {
        return new String(chars);
    }
}
