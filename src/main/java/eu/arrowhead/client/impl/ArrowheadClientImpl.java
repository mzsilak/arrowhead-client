package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.services.*;

public class ArrowheadClientImpl implements ArrowheadClient
{
    private final SystemEndpointHolder endpointHolder;
    private final Transport transport;

    private ServiceRegistry serviceRegistry;
    private SystemRegistry systemRegistry;
    private DeviceRegistry deviceRegistry;
    private Orchestrator orchestrator;
    private EventHandler eventHandler;
    private OnboardingController onboardingController;

    public ArrowheadClientImpl(final SystemEndpointHolder endpointHolder, final Transport transport)
    {
        super();
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

    public SystemEndpointHolder getEndpointHolder()
    {
        return endpointHolder;
    }

    public Transport getTransport()
    {
        return transport;
    }

    public ServiceRegistry getServiceRegistry()
    {
        return serviceRegistry;
    }

    public void setServiceRegistry(final ServiceRegistry serviceRegistry)
    {
        this.serviceRegistry = serviceRegistry;
    }

    public SystemRegistry getSystemRegistry()
    {
        return systemRegistry;
    }

    public void setSystemRegistry(final SystemRegistry systemRegistry)
    {
        this.systemRegistry = systemRegistry;
    }

    public DeviceRegistry getDeviceRegistry()
    {
        return deviceRegistry;
    }

    public void setDeviceRegistry(final DeviceRegistry deviceRegistry)
    {
        this.deviceRegistry = deviceRegistry;
    }

    public Orchestrator getOrchestrator()
    {
        return orchestrator;
    }

    public void setOrchestrator(final Orchestrator orchestrator)
    {
        this.orchestrator = orchestrator;
    }

    public EventHandler getEventHandler()
    {
        return eventHandler;
    }

    public void setEventHandler(final EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    public OnboardingController getOnboardingController()
    {
        return onboardingController;
    }

    public void setOnboardingController(final OnboardingController onboardingController)
    {
        this.onboardingController = onboardingController;
    }
}
