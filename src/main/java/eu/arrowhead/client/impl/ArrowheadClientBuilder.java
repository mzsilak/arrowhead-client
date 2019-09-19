package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.*;
import eu.arrowhead.client.services.*;
import eu.arrowhead.client.services.model.ArrowheadService;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryQuery;
import eu.arrowhead.client.utils.LogUtils;
import eu.arrowhead.client.utils.UriUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Objects;

public class ArrowheadClientBuilder
{
    private final Logger logger = LogManager.getLogger();

    private final ProtocolConfiguration protocol;
    private final Transport transport;
    private SystemEndpointHolder endpointHolder;
    private SSLContext sslContext;
    private ServiceRegistry serviceRegistry;

    private ArrowheadClientBuilder(final ProtocolConfiguration protocol, final Transport transport)
    {
        this.transport = transport;
        this.protocol = protocol;
        this.endpointHolder = new SystemEndpointHolder(protocol);
    }

    public static ArrowheadClientBuilder withOrchestrator(final ProtocolConfiguration protocol,
                                                          final InetAddress orchestratorAddress,
                                                          final Transport transport)
    {
        Objects.requireNonNull(protocol);
        Objects.requireNonNull(orchestratorAddress);
        Objects.requireNonNull(transport);

        final UriUtil uriUtil = new UriUtil(protocol, orchestratorAddress, protocol.getInt(ServiceRegistry.PORT_PROPERTY), ServiceRegistry.SYSTEM_SUFFIX);
        return withOrchestrator(protocol, uriUtil.copyBuild(), transport);
    }

    public static ArrowheadClientBuilder withServiceRegistry(final ProtocolConfiguration protocol,
                                                             final InetAddress serviceRegistryAddress,
                                                             final Transport transport)
    {
        Objects.requireNonNull(protocol);
        Objects.requireNonNull(serviceRegistryAddress);
        Objects.requireNonNull(transport);

        final UriUtil uriUtil = new UriUtil(protocol, serviceRegistryAddress, protocol.getInt(ServiceRegistry.PORT_PROPERTY), ServiceRegistry.SYSTEM_SUFFIX);
        return withServiceRegistry(protocol, uriUtil.copyBuild(), transport);
    }

    public static ArrowheadClientBuilder withOrchestrator(final ProtocolConfiguration protocol,
                                                          final URI orchestratorUri,
                                                          final Transport transport)
    {
        Objects.requireNonNull(protocol);
        Objects.requireNonNull(orchestratorUri);
        Objects.requireNonNull(transport);

        final ArrowheadClientBuilder builder = new ArrowheadClientBuilder(protocol, transport);
        builder.endpointHolder.add(CoreSystems.ORCHESTRATOR, orchestratorUri);
        return builder;
    }

    public static ArrowheadClientBuilder withServiceRegistry(final ProtocolConfiguration protocol,
                                                             final URI serviceRegistryUri,
                                                             final Transport transport)
    {
        Objects.requireNonNull(protocol);
        Objects.requireNonNull(serviceRegistryUri);
        Objects.requireNonNull(transport);

        final ArrowheadClientBuilder builder = new ArrowheadClientBuilder(protocol, transport);
        builder.endpointHolder.add(CoreSystems.SERVICE_REGISTRY, serviceRegistryUri);
        return builder;
    }

    public static ArrowheadClientBuilder withServiceRegistry(final ProtocolConfiguration protocol,
                                                             final ServiceRegistry serviceRegistry,
                                                             final Transport transport)
    {
        Objects.requireNonNull(protocol);
        Objects.requireNonNull(serviceRegistry);
        Objects.requireNonNull(transport);

        final ArrowheadClientBuilder builder = new ArrowheadClientBuilder(protocol, transport);
        builder.serviceRegistry = serviceRegistry;
        return builder;
    }

    public ArrowheadClientBuilder withSystemEndpoints(final SystemEndpointHolder endpointHolder)
    {
        endpointHolder.addAll(this.endpointHolder);
        this.endpointHolder = endpointHolder;
        return this;
    }

    private Orchestrator getOrchestrator()
    {
        final URI orchestratorUri;
        if (endpointHolder.contains(CoreSystems.ORCHESTRATOR))
        {
            orchestratorUri = endpointHolder.get(CoreSystems.ORCHESTRATOR);
        }
        else
        {
            if (Objects.isNull(serviceRegistry))
            {
                final URI serviceRegistryUri = endpointHolder.get(CoreSystems.SERVICE_REGISTRY);

                if (Objects.isNull(serviceRegistryUri))
                {
                    throw new IllegalStateException("Either Orchestrator or ServiceRegistry URI must be set at this point");
                }

                serviceRegistry = new ServiceRegistryImpl(null, serviceRegistryUri, transport);
            }

            // query service registry for orchestration service
            orchestratorUri = getSystemUri(ServiceDefinitions.ORCHESTRATION, Orchestrator.SYSTEM_SUFFIX);
        }

        return new OrchestratorImpl(null, orchestratorUri, transport);
    }

    private URI getSystemUri(final ServiceDefinitions definition, final String serviceSuffix)
    {
        try
        {
            final String serviceDefinition = definition.getServiceDefinition(protocol);
            final String serviceInterface = ServiceInterfaces.JSON.forProtocol(protocol);
            final ServiceRegistryQuery serviceRegistryQuery = new ServiceRegistryQuery(new ArrowheadService(serviceDefinition, serviceInterface));

            final ServiceRegistryEntry entry = serviceRegistry.query(serviceRegistryQuery);
            final UriUtil uriUtil = new UriUtil(protocol, entry.getProvider().getAddress(), entry.getProvider().getPort(), entry.getServiceURI());
            final UriBuilder uriBuilder = UriComponentsBuilder.fromUri(uriUtil.copyBuild());
            uriBuilder.replacePath(serviceSuffix);
            return uriBuilder.build();
        }
        catch (TransportException | UnknownHostException e)
        {
            LogUtils.printShortStackTrace(logger, Level.ERROR, e);
            throw new RuntimeException("Unable to contact ServiceRegistry", e);
        }
    }

    private <T> T create(final ArrowheadClient client, final Class<T> cls, final URI uri)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        Constructor<T> constructor = cls.getConstructor(ArrowheadClient.class, URI.class, Transport.class);
        return constructor.newInstance(client, uri, transport);
    }

    public ArrowheadClient build()
    {
        final ArrowheadClientImpl client = new ArrowheadClientImpl(endpointHolder, transport);
        client.setDeviceRegistry(new DeviceRegistryImpl(client, getSystemUri(ServiceDefinitions.DEVICE_REGISTRY, DeviceRegistry.SYSTEM_SUFFIX), transport));
        client.setSystemRegistry(new SystemRegistryImpl(client, getSystemUri(ServiceDefinitions.SYSTEM_REGISTRY, SystemRegistry.SYSTEM_SUFFIX), transport));
        client.setServiceRegistry(new ServiceRegistryImpl(client, getSystemUri(ServiceDefinitions.SERVICE_REGISTRY, ServiceRegistry.SYSTEM_SUFFIX), transport));
        client.setOnboardingController(
                new OnboardingControllerImpl(client, getSystemUri(ServiceDefinitions.ONBOARDING, OnboardingController.SYSTEM_SUFFIX), transport));
        client.setOrchestrator(new OrchestratorImpl(client, getSystemUri(ServiceDefinitions.ORCHESTRATION, Orchestrator.SYSTEM_SUFFIX), transport));
        client.setEventHandler(new EventHandlerImpl(client, getSystemUri(ServiceDefinitions.EVENT_SUBSCRIPTION, EventHandler.SYSTEM_SUFFIX), transport));

        return client;
    }
}
