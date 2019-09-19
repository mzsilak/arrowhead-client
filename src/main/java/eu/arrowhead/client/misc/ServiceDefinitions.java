package eu.arrowhead.client.misc;

public enum ServiceDefinitions
{
    ONBOARDING("Onboarding"),
    DEVICE_REGISTRY("DeviceRegistry"),
    SYSTEM_REGISTRY("SystemRegistry"),
    SERVICE_REGISTRY("ServiceRegistry"),
    ORCHESTRATION("OrchestrationService"),
    EVENT_SUBSCRIPTION("EventSubscription");

    private final String serviceDefinition;

    ServiceDefinitions(final String serviceDefinition)
    {
        this.serviceDefinition = serviceDefinition;
    }

    public String getServiceDefinition(final ProtocolConfiguration protocolConfiguration)
    {
        if (protocolConfiguration.isSecure())
        {
            return "Secure" + serviceDefinition;
        }
        else
        { return "Insecure" + serviceDefinition; }
    }
}
