package eu.arrowhead.client.transport;

import java.util.function.Supplier;

@FunctionalInterface
public interface TransportFactory extends Supplier<Transport>
{
}
