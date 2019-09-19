package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.DeviceRegistry;
import eu.arrowhead.client.services.ServiceRegistry;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryQuery;
import eu.arrowhead.client.utils.UriUtil;

import java.net.URI;

public class DeviceRegistryImpl extends ServiceClientImpl implements DeviceRegistry
{
    public DeviceRegistryImpl(final ArrowheadClient arrowheadClient, final URI systemUri, final Transport transport)
    {
        super(arrowheadClient, systemUri, transport);
    }

    @Override
    public DeviceRegistryEntry registerSystem(final DeviceRegistryEntry entry) throws TransportException
    {
        return null;
    }

    @Override
    public DeviceRegistryEntry removeSystem(final DeviceRegistryEntry entry) throws TransportException
    {
        return null;
    }
}
