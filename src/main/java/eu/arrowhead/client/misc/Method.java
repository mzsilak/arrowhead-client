package eu.arrowhead.client.misc;

public interface Method<RETURN>
{

    RETURN invoke() throws TransportException;

}
