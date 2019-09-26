package eu.arrowhead.onboarding;

import eu.arrowhead.client.transport.ProtocolConfiguration;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.onboarding.impl.OnboardingClientBuilder;

public interface OnboardingClient
{
    static OnboardingClientBuilder withProtocol(final ProtocolConfiguration protocol)
    {
        return new OnboardingClientBuilder(protocol);
    }

    DeviceRegistryOnboarding plain(final OnboardingRequest request) throws TransportException;

    DeviceRegistryOnboarding withSharedKey(final OnboardingWithSharedKeyRequest request) throws TransportException;

    DeviceRegistryOnboarding withCertificate(final OnboardingWithCertificateRequest request) throws TransportException;

}
