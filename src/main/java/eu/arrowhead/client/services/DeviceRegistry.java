package eu.arrowhead.client.services;


public interface DeviceRegistry extends ArrowheadClientFacet
{
    SystemRegistry registerSystem();
    OnboardingController removeSystem();
}
