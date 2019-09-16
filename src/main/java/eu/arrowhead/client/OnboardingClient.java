package eu.arrowhead.client;

import eu.arrowhead.client.spi.OnboardingClientBuilder;

public interface OnboardingClient
{
    static OnboardingClientBuilder withProtocol(final Protocols protocol)
    {
        return new OnboardingClientBuilder(protocol);
    }

    void plain();
}
