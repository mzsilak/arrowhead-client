package eu.arrowhead.client.transport;

import eu.arrowhead.client.transport.http.HttpTransport;
import eu.arrowhead.client.utils.LogUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class ProtocolConfiguration
{
    public static final ProtocolConfiguration HTTP =
            new ProtocolConfiguration("http.properties", "http", false, new HttpTransport());

    public static final ProtocolConfiguration HTTPS =
            new ProtocolConfiguration("https.properties", "https", true, new HttpTransport());

    private final Logger logger = LogManager.getLogger();
    private final Transport transport;
    private final Properties properties;
    private final String scheme;
    private final boolean secure;

    private ProtocolConfiguration(final String configFile, final String scheme, final boolean secure, final Transport transport)
    {
        this.scheme = scheme;
        this.secure = secure;
        this.transport = transport;
        properties = new Properties();
        try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFile))
        {
            if (Objects.nonNull(inputStream))
            { properties.load(inputStream); }
        }
        catch (IOException e)
        {
            LogUtils.printShortStackTrace(logger, Level.ERROR, e);
            throw new RuntimeException(e);
        }
    }

    public String getString(final String key)
    {
        return properties.getProperty(key);
    }

    public Integer getInt(final String key)
    {
        return Integer.valueOf(properties.getProperty(key));
    }

    public boolean isSecure()
    {
        return secure;
    }

    public Transport getTransport()
    {
        return transport;
    }

    public String getScheme()
    {
        return scheme;
    }
}
