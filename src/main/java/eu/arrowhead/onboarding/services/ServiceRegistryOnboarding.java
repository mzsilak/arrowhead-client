package eu.arrowhead.onboarding.services;


import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.services.ArrowheadClientFacet;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;

public interface ServiceRegistryOnboarding extends ArrowheadClientFacet
{
    ServiceRegistryEntry getServiceRegistryEntry();

    ArrowheadClient registerService(final ServiceRegistryEntry request) throws TransportException;

    SystemRegistryOnboarding removeService(final ServiceRegistryEntry request) throws TransportException;
}
