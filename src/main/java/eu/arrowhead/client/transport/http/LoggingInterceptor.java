package eu.arrowhead.client.transport.http;

import eu.arrowhead.client.utils.LogUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;

public class LoggingInterceptor implements ClientHttpRequestInterceptor
{
    private final Logger logger = LogManager.getLogger();

    public LoggingInterceptor()
    {
        super();
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("Request: {} {}: {}", request.getMethodValue(), request.getURI(), new String(body));

            final ClientHttpResponse clientHttpResponse = execution.execute(request, body);
            if (clientHttpResponse.getStatusCode().is2xxSuccessful())
            {
                final BufferingResponseWrapper wrapper = new BufferingResponseWrapper(clientHttpResponse);
                final String bodyString = new String(StreamUtils.copyToByteArray(wrapper.getBody()));
                LogUtils.multiLine(logger, Level.DEBUG, "Response: {} {}\n{}",
                                   clientHttpResponse.getRawStatusCode(),
                                   clientHttpResponse.getStatusText(),
                                   bodyString);
                return wrapper;
            }
            else
            {
                logger.debug("Response: {} {}", clientHttpResponse.getRawStatusCode(), clientHttpResponse.getStatusText());
                return clientHttpResponse;
            }

        }
        else { return execution.execute(request, body); }
    }
}
