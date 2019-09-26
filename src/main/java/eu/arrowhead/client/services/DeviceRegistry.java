package eu.arrowhead.client.services;


import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;

public interface DeviceRegistry extends ArrowheadClientFacet
{
    String SYSTEM_SUFFIX = "deviceregistry";
    String PORT_PROPERTY = "device_registry.port";

    String METHOD_REGISTER_SUFFIX = "publish";
    String METHOD_REMOVE_SUFFIX = "unpublish";

    DeviceRegistryEntry registerSystem(final DeviceRegistryEntry entry) throws TransportException;

    DeviceRegistryEntry removeSystem(final DeviceRegistryEntry entry) throws TransportException;
}
