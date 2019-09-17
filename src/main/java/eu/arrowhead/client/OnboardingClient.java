package eu.arrowhead.client;

import eu.arrowhead.client.misc.Protocols;
import eu.arrowhead.client.services.DeviceRegistry;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.impl.OnboardingClientBuilder;

public interface OnboardingClient
{
    static OnboardingClientBuilder withProtocol(final Protocols protocol)
    {
        return new OnboardingClientBuilder(protocol);
    }

    DeviceRegistry plain(final OnboardingRequest request);

    DeviceRegistry withSharedKey(final OnboardingWithSharedKeyRequest request);

    DeviceRegistry withCertificate(final OnboardingWithCertificateRequest request);

}
