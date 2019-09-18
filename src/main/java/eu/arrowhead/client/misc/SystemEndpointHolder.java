package eu.arrowhead.client.misc;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SystemEndpointHolder
{
    private final ConcurrentMap<CoreSystems, URI> systems = new ConcurrentHashMap<>();

    public void add(final CoreSystems system, final URI uri)
    {
        systems.put(system, uri);
    }

    public URI get(final CoreSystems system)
    {
        return systems.get(system);
    }
}
