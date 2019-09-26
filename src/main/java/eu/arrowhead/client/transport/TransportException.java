package eu.arrowhead.client.transport;

public class TransportException extends Exception
{
    public TransportException()
    {
        super();
    }

    public TransportException(final String message)
    {
        super(message);
    }

    public TransportException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public TransportException(final Throwable cause)
    {
        super(cause);
    }

    public TransportException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
