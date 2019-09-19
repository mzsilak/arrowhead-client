package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.services.SystemRegistry;
import eu.arrowhead.client.utils.UriUtil;
import eu.arrowhead.client.misc.CoreSystems;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import eu.arrowhead.onboarding.services.ServiceRegistryOnboarding;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;
import eu.arrowhead.client.services.request.SystemRegistryEntry;

public class SystemRegistryOnboardingImpl implements SystemRegistryOnboarding
{
    private final DeviceRegistryOnboarding deviceRegistry;
    private final SystemEndpointHolder endpointHolder;
    private final Transport transport;
    private final UriUtil uriUtil;

    public SystemRegistryOnboardingImpl(final DeviceRegistryOnboarding deviceRegistry, final SystemEndpointHolder endpointHolder, final Transport transport)
    {
        this.uriUtil = new UriUtil(endpointHolder.get(CoreSystems.SYSTEM_REGISTRY));
        this.deviceRegistry = deviceRegistry;
        this.endpointHolder = endpointHolder;
        this.transport = transport;
    }

    @Override
    public ServiceRegistryOnboarding registerSystem(final SystemRegistryEntry request) throws TransportException
    {
        transport.post(SystemRegistryEntry.class, uriUtil.copyBuild(SystemRegistry.METHOD_REGISTER_SUFFIX), request);
        return new ServiceRegistryOnboardingImpl(this, endpointHolder, transport);
    }

    @Override
    public DeviceRegistryOnboarding removeSystem(final SystemRegistryEntry request) throws TransportException
    {
        transport.post(SystemRegistryEntry.class, uriUtil.copyBuild(SystemRegistry.METHOD_REMOVE_SUFFIX), request);
        return deviceRegistry;
    }
}
