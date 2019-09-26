package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.services.ServiceRegistry;
import eu.arrowhead.client.services.request.*;

import java.net.URI;

public class ServiceRegistryImpl extends ServiceClientImpl implements ServiceRegistry
{
    public ServiceRegistryImpl(final ArrowheadClient client, final URI uri, final Transport transport)
    {
        super(client, uri, transport);
    }

    @Override
    public ServiceRegistryEntry query(final ServiceRegistryQuery request) throws TransportException
    {
        return transport.put(ServiceRegistryEntry.class, uriUtils.copyBuild(ServiceRegistry.METHOD_QUERY_SUFFIX), request);
    }

    @Override
    public ServiceRegistryEntry registerService(final ServiceRegistryEntry request) throws TransportException
    {
        return transport.post(ServiceRegistryEntry.class, uriUtils.copyBuild(ServiceRegistry.METHOD_REGISTER_SUFFIX), request);
    }

    @Override
    public ServiceRegistryEntry removeService(final ServiceRegistryEntry request) throws TransportException
    {
        return transport.put(ServiceRegistryEntry.class, uriUtils.copyBuild(ServiceRegistry.METHOD_REMOVE_SUFFIX), request);
    }
}
