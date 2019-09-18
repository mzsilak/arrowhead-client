package eu.arrowhead.client.impl;

import eu.arrowhead.client.OnboardingClient;
import eu.arrowhead.client.misc.CoreSystems;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.DeviceRegistryOnboarding;
import eu.arrowhead.client.services.SystemRegistryOnboarding;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;

public class DeviceRegistryOnboardingImpl extends ClientImpl implements DeviceRegistryOnboarding
{
    public static final String SERVICE_SUFFIX = "deviceregistry";

    private final static String METHOD_REGISTER_SUFFIX = "publish";
    private final static String METHOD_REMOVE_SUFFIX = "unpublish";

    private final OnboardingClientImpl onboardingClient;
    private final SystemEndpointHolder endpointHolder;
    private final Transport transport;

    public DeviceRegistryOnboardingImpl(final OnboardingClientImpl onboardingClient, final SystemEndpointHolder endpointHolder, final Transport transport)
    {
        super(endpointHolder.get(CoreSystems.DEVICE_REGISTRY));

        this.onboardingClient = onboardingClient;
        this.endpointHolder = endpointHolder;
        this.transport = transport;
    }

    @Override
    public SystemRegistryOnboarding registerSystem(final DeviceRegistryEntry request) throws TransportException
    {
        transport.post(DeviceRegistryEntry.class, build(METHOD_REGISTER_SUFFIX), request);
        return new SystemRegistryOnboardingImpl(this, endpointHolder, transport);
    }

    @Override
    public OnboardingClient removeSystem(final DeviceRegistryEntry request) throws TransportException
    {
        transport.post(DeviceRegistryEntry.class, build(METHOD_REMOVE_SUFFIX), request);
        return onboardingClient;
    }
}
