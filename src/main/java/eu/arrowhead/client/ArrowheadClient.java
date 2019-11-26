package eu.arrowhead.client;

import eu.arrowhead.client.services.*;
import eu.arrowhead.onboarding.services.ServiceRegistryOnboarding;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;

public interface ArrowheadClient
{
    OnboardingController onboardingController();

    DeviceRegistry deviceRegistry();

    SystemRegistry systemRegistry();

    ServiceRegistry serviceRegistry();

    Orchestrator orchestrator();

    EventHandler eventHandler();

    ServiceRegistryOnboarding serviceOffboarding();

    SystemRegistryOnboarding systemOffboarding();
}
