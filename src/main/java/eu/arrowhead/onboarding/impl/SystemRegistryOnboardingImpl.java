package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.impl.SystemRegistryImpl;
import eu.arrowhead.client.misc.*;
import eu.arrowhead.client.services.SystemRegistry;
import eu.arrowhead.client.transport.RetryHandler;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.utils.UriUtils;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import eu.arrowhead.onboarding.services.ServiceRegistryOnboarding;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;
import eu.arrowhead.client.services.request.SystemRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemRegistryOnboardingImpl implements SystemRegistryOnboarding
{
    private final Logger logger = LogManager.getLogger();
    private final DeviceRegistryOnboarding deviceRegistry;
    private final SystemEndpointHolder endpointHolder;
    private final SystemRegistry systemRegistry;
    private final SSLContextBuilder sslContextBuilder;
    private final RetryHandler retryHandler;
    private final Transport transport;
    private final UriUtils uriUtils;

    private SystemRegistryEntry systemRegistryEntry;

    public SystemRegistryOnboardingImpl(final DeviceRegistryOnboarding deviceRegistry, final SystemEndpointHolder endpointHolder, final Transport transport,
                                        final RetryHandler retryHandler,
                                        final SSLContextBuilder sslContextBuilder)
    {
        this.uriUtils = new UriUtils(endpointHolder.get(CoreSystems.SYSTEM_REGISTRY));
        this.deviceRegistry = deviceRegistry;
        this.endpointHolder = endpointHolder;
        this.transport = transport;
        this.retryHandler = retryHandler;
        this.sslContextBuilder = sslContextBuilder;
        this.systemRegistry = new SystemRegistryImpl(null, uriUtils.copyBuild(), transport, sslContextBuilder);
        logger.debug("Created {}", this);
    }

    @Override
    public ServiceRegistryOnboarding registerSystem(final SystemRegistryEntry request) throws TransportException
    {
        systemRegistryEntry = retryHandler.invokeWithErrorHandler(() -> systemRegistry.registerSystem(request), () -> removeSystem(request));
        request.setId(systemRegistryEntry.getId());
        request.getProvidedSystem().setId(systemRegistryEntry.getProvidedSystem().getId());
        request.getProvider().setId(systemRegistryEntry.getProvider().getId());
        return new ServiceRegistryOnboardingImpl(this, endpointHolder, transport, retryHandler, sslContextBuilder);
    }

    @Override
    public DeviceRegistryOnboarding removeSystem(final SystemRegistryEntry request) throws TransportException
    {
        systemRegistryEntry = systemRegistry.removeSystem(request);
        request.setId(null);
        request.getProvidedSystem().setId(null);
        request.getProvider().setId(null);
        return deviceRegistry;
    }

    public SystemRegistryEntry getSystemRegistryEntry()
    {
        return systemRegistryEntry;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("SystemRegistryOnboardingImpl [");
        sb.append("endpointHolder=").append(endpointHolder);
        sb.append(", transport=").append(transport);
        sb.append(", uriUtil=").append(uriUtils);
        sb.append(']');
        return sb.toString();
    }
}
