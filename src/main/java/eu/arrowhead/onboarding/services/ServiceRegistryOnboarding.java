package eu.arrowhead.onboarding.services;


import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.ArrowheadClientFacet;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryQuery;

public interface ServiceRegistryOnboarding extends ArrowheadClientFacet
{
    ServiceRegistryEntry query(final ServiceRegistryQuery request) throws TransportException;

    ArrowheadClient registerService(final ServiceRegistryEntry request) throws TransportException;

    SystemRegistryOnboarding removeService(final ServiceRegistryEntry request) throws TransportException;
}
