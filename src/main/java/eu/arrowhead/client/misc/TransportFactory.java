package eu.arrowhead.client.misc;

import java.util.function.Supplier;

@FunctionalInterface
public interface TransportFactory extends Supplier<Transport>
{
}
