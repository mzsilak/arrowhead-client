package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.impl.ArrowheadClientBuilder;
import eu.arrowhead.client.impl.ServiceRegistryImpl;
import eu.arrowhead.client.misc.CoreSystems;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.ServiceRegistry;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryQuery;
import eu.arrowhead.client.utils.UriUtil;
import eu.arrowhead.onboarding.services.ServiceRegistryOnboarding;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;

public class ServiceRegistryOnboardingImpl implements ServiceRegistryOnboarding
{
    private final SystemRegistryOnboarding systemRegistry;
    private final ServiceRegistry serviceRegistry;
    private final SystemEndpointHolder endpointHolder;
    private final Transport transport;
    private final UriUtil uriUtil;

    public ServiceRegistryOnboardingImpl(final SystemRegistryOnboarding systemRegistry, final SystemEndpointHolder endpointHolder, final Transport transport)
    {
        this.uriUtil = new UriUtil(endpointHolder.get(CoreSystems.SERVICE_REGISTRY));
        this.systemRegistry = systemRegistry;
        this.endpointHolder = endpointHolder;
        this.transport = transport;
        this.serviceRegistry = new ServiceRegistryImpl(null, uriUtil.copyBuild(), transport);
    }

    @Override
    public ArrowheadClient registerService(final ServiceRegistryEntry request) throws TransportException
    {
        transport.post(ServiceRegistryEntry.class, uriUtil.copyBuild(ServiceRegistry.METHOD_REGISTER_SUFFIX), request);
        return null;
    }

    @Override
    public SystemRegistryOnboarding removeService(final ServiceRegistryEntry request) throws TransportException
    {
        transport.put(ServiceRegistryEntry.class, uriUtil.copyBuild(ServiceRegistry.METHOD_REMOVE_SUFFIX), request);
        return systemRegistry;
    }

    @Override
    public ServiceRegistryEntry query(final ServiceRegistryQuery request) throws TransportException
    {
        return transport.put(ServiceRegistryEntry.class, uriUtil.copyBuild(ServiceRegistry.METHOD_QUERY_SUFFIX), request);
    }

    @Override
    public ArrowheadClient getClient()
    {
        return ArrowheadClientBuilder.withServiceRegistry(endpointHolder.getProtocolConfiguration(), serviceRegistry, transport)
                                     .withSystemEndpoints(endpointHolder)
                                     .build();
    }
}
