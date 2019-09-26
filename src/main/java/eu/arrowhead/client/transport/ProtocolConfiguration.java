package eu.arrowhead.client.transport;

import eu.arrowhead.client.transport.http.HttpTransport;
import eu.arrowhead.client.utils.LogUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum ProtocolConfiguration
{
    HTTP("http.properties", false, HttpTransport::new),
    HTTPS("https.properties", true, HttpTransport::new);

    private final Logger logger = LogManager.getLogger();
    private final TransportFactory transportFactory;
    private final Properties properties;
    private final boolean secure;

    ProtocolConfiguration(final String configFile, final boolean secure, final TransportFactory transportFactory)
    {
        this.secure = secure;
        this.transportFactory = transportFactory;
        properties = new Properties();
        try
        {
            try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFile))
            {
                properties.load(inputStream);
            }
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
        return transportFactory.get();
    }

    public String getScheme()
    {
        return name().toLowerCase();
    }
}
