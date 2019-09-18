package eu.arrowhead.client.services;


import eu.arrowhead.client.OnboardingClient;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;

public interface DeviceRegistryOnboarding
{
    SystemRegistryOnboarding registerSystem(final DeviceRegistryEntry entry) throws TransportException;

    OnboardingClient removeSystem(final DeviceRegistryEntry entry) throws TransportException;
}
