package eu.arrowhead.client.services;


import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryQuery;

public interface ServiceRegistry extends ArrowheadClientFacet
{
    String SYSTEM_SUFFIX = "serviceregistry";
    String PORT_PROPERTY = "service_registry.port";

    String METHOD_QUERY_SUFFIX = "query";
    String METHOD_REGISTER_SUFFIX = "register";
    String METHOD_REMOVE_SUFFIX = "remove";

    ServiceRegistryEntry query(final ServiceRegistryQuery request) throws TransportException;

    ServiceRegistryEntry registerService(final ServiceRegistryEntry request) throws TransportException;

    ServiceRegistryEntry removeService(final ServiceRegistryEntry request) throws TransportException;
}
