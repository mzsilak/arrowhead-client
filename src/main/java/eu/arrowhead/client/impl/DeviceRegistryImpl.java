package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.services.DeviceRegistry;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;

import java.net.URI;

public class DeviceRegistryImpl extends ServiceClientImpl implements DeviceRegistry
{
    public DeviceRegistryImpl(final ArrowheadClient arrowheadClient, final URI systemUri, final Transport transport)
    {
        super(arrowheadClient, systemUri, transport);
    }

    @Override
    public DeviceRegistryEntry registerSystem(final DeviceRegistryEntry request) throws TransportException
    {
        return transport.post(DeviceRegistryEntry.class, uriUtils.copyBuild(DeviceRegistry.METHOD_REGISTER_SUFFIX), request);
    }

    @Override
    public DeviceRegistryEntry removeSystem(final DeviceRegistryEntry request) throws TransportException
    {
        return transport.post(DeviceRegistryEntry.class, uriUtils.copyBuild(DeviceRegistry.METHOD_REMOVE_SUFFIX), request);
    }
}
