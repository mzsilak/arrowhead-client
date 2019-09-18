package eu.arrowhead.client.services;


import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryQuery;

public interface ServiceRegistry extends ArrowheadClientFacet
{
    ServiceRegistry query(final ServiceRegistryQuery request) throws TransportException;

    ServiceRegistry registerService(final ServiceRegistryEntry request) throws TransportException;

    ServiceRegistry removeService(final ServiceRegistryEntry request) throws TransportException;
}
