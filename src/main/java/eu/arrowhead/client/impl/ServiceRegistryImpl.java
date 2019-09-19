package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.ServiceRegistry;
import eu.arrowhead.client.services.request.*;
import eu.arrowhead.client.utils.UriUtil;

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
        return null;
    }

    @Override
    public ServiceRegistryEntry registerService(final ServiceRegistryEntry request) throws TransportException
    {
        return null;
    }

    @Override
    public ServiceRegistryEntry removeService(final ServiceRegistryEntry request) throws TransportException
    {
        return null;
    }
}
