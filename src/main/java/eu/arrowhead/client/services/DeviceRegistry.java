package eu.arrowhead.client.services;


import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;

public interface DeviceRegistry extends ArrowheadClientFacet
{
    DeviceRegistryEntry registerSystem(final DeviceRegistryEntry entry) throws TransportException;

    DeviceRegistryEntry removeSystem(final DeviceRegistryEntry entry) throws TransportException;
}
