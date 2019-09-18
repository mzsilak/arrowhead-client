package eu.arrowhead.client.services;


import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.request.SystemRegistryEntry;

public interface SystemRegistryOnboarding
{
    ServiceRegistryOnboarding registerSystem(final SystemRegistryEntry request) throws TransportException;

    DeviceRegistryOnboarding removeSystem(final SystemRegistryEntry request) throws TransportException;

}
