package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.impl.ArrowheadClientBuilder;
import eu.arrowhead.client.impl.ServiceRegistryImpl;
import eu.arrowhead.client.misc.*;
import eu.arrowhead.client.services.ServiceRegistry;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.transport.RetryHandler;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.utils.UriUtils;
import eu.arrowhead.onboarding.services.ServiceRegistryOnboarding;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;

public class ServiceRegistryOnboardingImpl implements ServiceRegistryOnboarding
{
    private final Logger logger = LogManager.getLogger();
    private final SystemRegistryOnboarding systemRegistry;
    private final ServiceRegistry serviceRegistry;
    private final SSLContextBuilder sslContextBuilder;
    private final RetryHandler retryHandler;
    private final SystemEndpointHolder endpointHolder;
    private final Transport transport;
    private final UriUtils uriUtils;

    private ServiceRegistryEntry serviceRegistryEntry;

    public ServiceRegistryOnboardingImpl(final SystemRegistryOnboarding systemRegistry,
                                         final SystemEndpointHolder endpointHolder,
                                         final Transport transport,
                                         final RetryHandler retryHandler,
                                         final SSLContextBuilder sslContextBuilder)
    {
        this.uriUtils = new UriUtils(endpointHolder.get(CoreSystems.SERVICE_REGISTRY));
        this.systemRegistry = systemRegistry;
        this.endpointHolder = endpointHolder;
        this.transport = transport;
        this.retryHandler = retryHandler;
        this.sslContextBuilder = sslContextBuilder;
        this.serviceRegistry = new ServiceRegistryImpl(null, uriUtils.copyBuild(), transport, sslContextBuilder);
        logger.debug("Created {}", this);
    }

    @Override
    public ArrowheadClient registerService(final ServiceRegistryEntry request) throws TransportException
    {
        serviceRegistryEntry = retryHandler.invokeWithErrorHandler(() -> serviceRegistry.registerService(request), () -> removeService(request));
        request.setId(serviceRegistryEntry.getId());
        request.getProvidedService().setId(serviceRegistryEntry.getProvidedService().getId());
        request.getProvider().setId(serviceRegistryEntry.getProvider().getId());
        return getClient();
    }

    @Override
    public SystemRegistryOnboarding removeService(final ServiceRegistryEntry request) throws TransportException
    {
        serviceRegistryEntry = serviceRegistry.removeService(request);
        request.setId(null);
        request.getProvidedService().setId(null);
        request.getProvider().setId(null);
        return systemRegistry;
    }

    public ServiceRegistryEntry getServiceRegistryEntry()
    {
        return serviceRegistryEntry;
    }

    @Override
    public ArrowheadClient getClient()
    {
        return ArrowheadClientBuilder.withServiceRegistry(endpointHolder.getProtocolConfiguration(), serviceRegistry, transport)
                                     .withSystemEndpoints(endpointHolder)
                                     .withSSLContextBuilder(sslContextBuilder)
                                     .build();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ServiceRegistryOnboardingImpl [");
        sb.append("endpointHolder=").append(endpointHolder);
        sb.append(", transport=").append(transport);
        sb.append(", uriUtil=").append(uriUtils);
        sb.append(']');
        return sb.toString();
    }
}
