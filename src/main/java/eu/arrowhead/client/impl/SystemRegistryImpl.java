package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.SystemRegistry;
import eu.arrowhead.client.services.request.SystemRegistryEntry;

import java.net.URI;

public class SystemRegistryImpl extends ServiceClientImpl implements SystemRegistry
{

    public SystemRegistryImpl(final ArrowheadClient arrowheadClient, final URI systemUri, final Transport transport)
    {
        super(arrowheadClient, systemUri, transport);
    }

    @Override
    public SystemRegistryEntry registerSystem(final SystemRegistryEntry request) throws TransportException
    {
        return null;
    }

    @Override
    public SystemRegistryEntry removeSystem(final SystemRegistryEntry request) throws TransportException
    {
        return null;
    }
}
