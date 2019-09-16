package eu.arrowhead.client;

import eu.arrowhead.client.utils.LogUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum Protocols
{
    HTTP("http.properties", false),
    HTTPS("https.properties", true);

    private final Logger logger = LogManager.getLogger();
    private final Properties properties;
    private final boolean secure;

    Protocols(final String configFile, final boolean secure)
    {
        this.secure = secure;
        properties = new Properties();
        try
        {
            try (final InputStream inputStream = getClass().getResourceAsStream(configFile))
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
}
