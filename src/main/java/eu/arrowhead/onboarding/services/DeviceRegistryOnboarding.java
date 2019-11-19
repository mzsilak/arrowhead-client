package eu.arrowhead.onboarding.services;


import eu.arrowhead.onboarding.OnboardingClient;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;

public interface DeviceRegistryOnboarding
{
    SystemRegistryOnboarding registerDevice(final DeviceRegistryEntry entry) throws TransportException;

    OnboardingClient removeDevice(final DeviceRegistryEntry entry) throws TransportException;

    DeviceRegistryEntry getDeviceRegistryEntry();
}
