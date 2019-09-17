package eu.arrowhead.client;

import eu.arrowhead.client.services.*;

public interface ArrowheadClient
{
    OnboardingClient onboardingClient();

    DeviceRegistry deviceRegistry();

    SystemRegistry systemRegistry();

    ServiceRegistry serviceRegistry();

    Orchestrator orchestrator();

    EventHandler eventHandler();

}
