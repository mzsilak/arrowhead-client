package eu.arrowhead.client.impl;

import eu.arrowhead.client.misc.CoreSystems;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.DeviceRegistryOnboarding;
import eu.arrowhead.client.services.ServiceRegistryOnboarding;
import eu.arrowhead.client.services.SystemRegistryOnboarding;
import eu.arrowhead.client.services.request.SystemRegistryEntry;

public class SystemRegistryOnboardingImpl extends ClientImpl implements SystemRegistryOnboarding
{
    public static final String SERVICE_SUFFIX = "systemregistry";

    private final static String METHOD_REGISTER_SUFFIX = "publish";
    private final static String METHOD_REMOVE_SUFFIX = "unpublish";

    private final DeviceRegistryOnboarding deviceRegistry;
    private final SystemEndpointHolder endpointHolder;
    private final Transport transport;

    public SystemRegistryOnboardingImpl(final DeviceRegistryOnboarding deviceRegistry, final SystemEndpointHolder endpointHolder, final Transport transport)
    {
        super(endpointHolder.get(CoreSystems.SYSTEM_REGISTRY));

        this.deviceRegistry = deviceRegistry;
        this.endpointHolder = endpointHolder;
        this.transport = transport;
    }

    @Override
    public ServiceRegistryOnboarding registerSystem(final SystemRegistryEntry request) throws TransportException
    {
        transport.post(SystemRegistryEntry.class, build(METHOD_REGISTER_SUFFIX), request);
        return new ServiceRegistryOnboardingImpl(this, endpointHolder, transport);
    }

    @Override
    public DeviceRegistryOnboarding removeSystem(final SystemRegistryEntry request) throws TransportException
    {
        transport.post(SystemRegistryEntry.class, build(METHOD_REMOVE_SUFFIX), request);
        return deviceRegistry;
    }
}
