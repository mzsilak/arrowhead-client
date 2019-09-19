package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.services.DeviceRegistry;
import eu.arrowhead.onboarding.OnboardingClient;
import eu.arrowhead.client.utils.UriUtil;
import eu.arrowhead.client.misc.CoreSystems;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;

public class DeviceRegistryOnboardingImpl implements DeviceRegistryOnboarding
{

    private final OnboardingClientImpl onboardingClient;
    private final SystemEndpointHolder endpointHolder;
    private final Transport transport;
    private final UriUtil uriUtil;

    public DeviceRegistryOnboardingImpl(final OnboardingClientImpl onboardingClient, final SystemEndpointHolder endpointHolder, final Transport transport)
    {
        this.uriUtil = new UriUtil(endpointHolder.get(CoreSystems.DEVICE_REGISTRY));
        this.onboardingClient = onboardingClient;
        this.endpointHolder = endpointHolder;
        this.transport = transport;
    }

    @Override
    public SystemRegistryOnboarding registerSystem(final DeviceRegistryEntry request) throws TransportException
    {
        transport.post(DeviceRegistryEntry.class, uriUtil.copyBuild(DeviceRegistry.METHOD_REGISTER_SUFFIX), request);
        return new SystemRegistryOnboardingImpl(this, endpointHolder, transport);
    }

    @Override
    public OnboardingClient removeSystem(final DeviceRegistryEntry request) throws TransportException
    {
        transport.post(DeviceRegistryEntry.class, uriUtil.copyBuild(DeviceRegistry.METHOD_REMOVE_SUFFIX), request);
        return onboardingClient;
    }
}
