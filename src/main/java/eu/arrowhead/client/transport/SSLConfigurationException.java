package eu.arrowhead.client.transport;

public class SSLConfigurationException extends Exception
{
    public SSLConfigurationException()
    {
        super();
    }

    public SSLConfigurationException(final String message)
    {
        super(message);
    }

    public SSLConfigurationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public SSLConfigurationException(final Throwable cause)
    {
        super(cause);
    }

    public SSLConfigurationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
