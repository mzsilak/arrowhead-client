package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.impl.DeviceRegistryImpl;
import eu.arrowhead.client.misc.*;
import eu.arrowhead.client.services.DeviceRegistry;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;
import eu.arrowhead.client.transport.RetryHandler;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.utils.UriUtils;
import eu.arrowhead.onboarding.OnboardingClient;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeviceRegistryOnboardingImpl implements DeviceRegistryOnboarding
{
    private final Logger logger = LogManager.getLogger();
    private final OnboardingClientImpl onboardingClient;
    private final SystemEndpointHolder endpointHolder;
    private final DeviceRegistry deviceRegistry;
    private final RetryHandler retryHandler;
    private final Transport transport;
    private final UriUtils uriUtils;

    private DeviceRegistryEntry deviceRegistryEntry;

    public DeviceRegistryOnboardingImpl(final OnboardingClientImpl onboardingClient, final SystemEndpointHolder endpointHolder, final Transport transport,
                                        final RetryHandler retryHandler)
    {
        this.uriUtils = new UriUtils(endpointHolder.get(CoreSystems.DEVICE_REGISTRY));
        this.onboardingClient = onboardingClient;
        this.endpointHolder = endpointHolder;
        this.transport = transport;
        this.retryHandler = retryHandler;
        this.deviceRegistry = new DeviceRegistryImpl(null, uriUtils.copyBuild(), transport);
        logger.debug("Created {}", this);
    }

    @Override
    public SystemRegistryOnboarding registerSystem(final DeviceRegistryEntry request) throws TransportException
    {
        deviceRegistryEntry = retryHandler.invokeWithErrorHandler(() -> deviceRegistry.registerSystem(request), () -> removeSystem(request));
        request.setId(deviceRegistryEntry.getId());
        request.getProvidedDevice().setId(deviceRegistryEntry.getProvidedDevice().getId());
        return new SystemRegistryOnboardingImpl(this, endpointHolder, transport, retryHandler);
    }

    @Override
    public OnboardingClient removeSystem(final DeviceRegistryEntry request) throws TransportException
    {
        deviceRegistryEntry = deviceRegistry.removeSystem(request);
        request.setId(null);
        request.getProvidedDevice().setId(null);
        return onboardingClient;
    }

    public DeviceRegistryEntry getDeviceRegistryEntry()
    {
        return deviceRegistryEntry;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("DeviceRegistryOnboardingImpl [");
        sb.append("endpointHolder=").append(endpointHolder);
        sb.append(", transport=").append(transport);
        sb.append(", uriUtil=").append(uriUtils);
        sb.append(']');
        return sb.toString();
    }
}
