package eu.arrowhead.client;

import eu.arrowhead.client.services.*;

public interface ArrowheadClient
{
    OnboardingController onboardingController();

    DeviceRegistry deviceRegistry();

    SystemRegistry systemRegistry();

    ServiceRegistry serviceRegistry();

    Orchestrator orchestrator();

    EventHandler eventHandler();

}
