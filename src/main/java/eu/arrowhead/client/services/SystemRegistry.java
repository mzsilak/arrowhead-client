package eu.arrowhead.client.services;


import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.request.SystemRegistryEntry;

public interface SystemRegistry extends ArrowheadClientFacet
{
    String SYSTEM_SUFFIX = "systemregistry";
    String PORT_PROPERTY = "system_registry.port";

    String METHOD_REGISTER_SUFFIX = "publish";
    String METHOD_REMOVE_SUFFIX = "unpublish";

    SystemRegistryEntry registerSystem(final SystemRegistryEntry request) throws TransportException;

    SystemRegistryEntry removeSystem(final SystemRegistryEntry request) throws TransportException;

}
