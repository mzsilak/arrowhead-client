package eu.arrowhead.onboarding.services;


import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.services.request.SystemRegistryEntry;

public interface SystemRegistryOnboarding
{
    ServiceRegistryOnboarding registerSystem(final SystemRegistryEntry request) throws TransportException;

    DeviceRegistryOnboarding removeSystem(final SystemRegistryEntry request) throws TransportException;

    SystemRegistryEntry getSystemRegistryEntry();
}
