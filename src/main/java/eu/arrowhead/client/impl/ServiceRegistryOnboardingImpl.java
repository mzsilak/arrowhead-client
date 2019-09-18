package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.CoreSystems;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.ServiceRegistryOnboarding;
import eu.arrowhead.client.services.SystemRegistryOnboarding;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryQuery;

public class ServiceRegistryOnboardingImpl extends ClientImpl implements ServiceRegistryOnboarding
{
    public static final String SERVICE_SUFFIX = "serviceregistry";

    private final static String METHOD_REGISTER_SUFFIX = "register";
    private final static String METHOD_REMOVE_SUFFIX = "remove";
    private final static String METHOD_QUERY_SUFFIX = "query";

    private final SystemRegistryOnboarding systemRegistry;
    private final SystemEndpointHolder endpointHolder;
    private final Transport transport;

    public ServiceRegistryOnboardingImpl(final SystemRegistryOnboarding systemRegistry, final SystemEndpointHolder endpointHolder, final Transport transport)
    {
        super(endpointHolder.get(CoreSystems.SERVICE_REGISTRY));

        this.systemRegistry = systemRegistry;
        this.endpointHolder = endpointHolder;
        this.transport = transport;
    }

    @Override
    public ArrowheadClient registerService(final ServiceRegistryEntry request) throws TransportException
    {
        transport.post(ServiceRegistryEntry.class, build(METHOD_REGISTER_SUFFIX), request);
        return null;
    }

    @Override
    public SystemRegistryOnboarding removeService(final ServiceRegistryEntry request) throws TransportException
    {
        transport.put(ServiceRegistryEntry.class, build(METHOD_REMOVE_SUFFIX), request);
        return systemRegistry;
    }

    @Override
    public ServiceRegistryEntry query(final ServiceRegistryQuery request) throws TransportException
    {
        return transport.put(ServiceRegistryEntry.class, build(METHOD_QUERY_SUFFIX), request);
    }

    @Override
    public ArrowheadClient getClient()
    {
        return ArrowheadClientBuilder.createWith(endpointHolder, transport);
    }
}
