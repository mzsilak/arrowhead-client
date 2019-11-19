package eu.arrowhead.client;

import eu.arrowhead.client.transport.ProtocolConfiguration;
import eu.arrowhead.client.services.model.ArrowheadSystem;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.services.request.SystemRegistryEntry;
import eu.arrowhead.client.utils.IpUtils;
import eu.arrowhead.onboarding.OnboardingClient;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import eu.arrowhead.onboarding.services.ServiceRegistryOnboarding;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;

import java.time.LocalDateTime;

public class DemoClient
{
    public static void main(String[] args) throws Exception
    {
        final OnboardingClient client = OnboardingClient.withProtocol(ProtocolConfiguration.HTTPS)
                                                        .withOnboardingAddress("localhost")
                                                        .withRetries(1)
                                                        .withKeyStorePasswords("123456", "keypass")
                                                        .withInsecureSSLContext()
                                                        .build();


        final DeviceRegistryOnboarding deviceRegistryOnboarding = client.withSharedKey(
                new OnboardingWithSharedKeyRequest("client", "password"));

        final DeviceRegistryEntry deviceRegistryEntry = new DeviceRegistryEntry("00:00:00:00:00:00", LocalDateTime.now().plusDays(3), "device");
        final SystemRegistryOnboarding systemRegistryOnboarding = deviceRegistryOnboarding.registerDevice(deviceRegistryEntry);

        final SystemRegistryEntry systemRegistryEntry = new SystemRegistryEntry(deviceRegistryEntry.getProvidedDevice(),
                                                                                new ArrowheadSystem("system", IpUtils.getIpAddress(), 22), null,
                                                                                deviceRegistryEntry.getEndOfValidity());
        final ServiceRegistryOnboarding serviceRegistryOnboarding = systemRegistryOnboarding.registerSystem(systemRegistryEntry);

        final ArrowheadClient arrowheadClient = serviceRegistryOnboarding.getClient();
        systemRegistryOnboarding.removeSystem(systemRegistryEntry).removeDevice(deviceRegistryEntry);
    }
}
