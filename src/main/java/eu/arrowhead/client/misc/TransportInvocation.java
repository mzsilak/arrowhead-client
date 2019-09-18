package eu.arrowhead.client.misc;

@FunctionalInterface
public interface TransportInvocation<RETURN>
{

    RETURN invoke() throws TransportException;

}
