package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.services.*;
import eu.arrowhead.onboarding.impl.SSLContextBuilder;
import eu.arrowhead.onboarding.impl.ServiceRegistryOnboardingImpl;
import eu.arrowhead.onboarding.services.ServiceRegistryOnboarding;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;

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

    private ServiceRegistryOnboarding serviceRegistryOnboarding;
    private SystemRegistryOnboarding systemRegistryOnboarding;

    public ArrowheadClientImpl(final SystemEndpointHolder endpointHolder, final Transport transport)
    {
        super();
        this.endpointHolder = endpointHolder;
        this.transport = transport;
    }

    @Override
    public OnboardingController onboardingController()
    {
        return onboardingController;
    }

    @Override
    public DeviceRegistry deviceRegistry()
    {
        return deviceRegistry;
    }

    @Override
    public SystemRegistry systemRegistry()
    {
        return systemRegistry;
    }

    @Override
    public ServiceRegistry serviceRegistry()
    {
        return serviceRegistry;
    }

    @Override
    public Orchestrator orchestrator()
    {
        return orchestrator;
    }

    @Override
    public EventHandler eventHandler()
    {
        return eventHandler;
    }

    @Override
    public ServiceRegistryOnboarding serviceOffboarding()
    {
        return serviceRegistryOnboarding;
    }

    @Override
    public SystemRegistryOnboarding systemOffboarding()
    {
        return systemRegistryOnboarding;
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

    public ServiceRegistryOnboarding getServiceRegistryOnboarding()
    {
        return serviceRegistryOnboarding;
    }

    public void setServiceRegistryOnboarding(final ServiceRegistryOnboarding serviceRegistryOnboarding)
    {
        this.serviceRegistryOnboarding = serviceRegistryOnboarding;
    }

    public SystemRegistryOnboarding getSystemRegistryOnboarding()
    {
        return systemRegistryOnboarding;
    }

    public void setSystemRegistryOnboarding(final SystemRegistryOnboarding systemRegistryOnboarding)
    {
        this.systemRegistryOnboarding = systemRegistryOnboarding;
    }
}
