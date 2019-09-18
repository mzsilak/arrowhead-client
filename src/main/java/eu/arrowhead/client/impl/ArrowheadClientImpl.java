package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.OnboardingClient;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.services.*;

public class ArrowheadClientImpl implements ArrowheadClient
{
    private final SystemEndpointHolder endpointHolder;
    private final Transport transport;

    public ArrowheadClientImpl(final SystemEndpointHolder endpointHolder, final Transport transport)
    {

        this.endpointHolder = endpointHolder;
        this.transport = transport;
    }

    @Override
    public OnboardingController onboardingController()
    {
        return null;
    }

    @Override
    public DeviceRegistry deviceRegistry()
    {
        return null;
    }

    @Override
    public SystemRegistry systemRegistry()
    {
        return null;
    }

    @Override
    public ServiceRegistry serviceRegistry()
    {
        return null;
    }

    @Override
    public Orchestrator orchestrator()
    {
        return null;
    }

    @Override
    public EventHandler eventHandler()
    {
        return null;
    }
}
