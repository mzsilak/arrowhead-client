package eu.arrowhead.client.transport;

@FunctionalInterface
public interface TransportInvocation<RETURN>
{

    RETURN invoke() throws TransportException;

}
