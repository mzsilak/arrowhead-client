package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.CoreSystems;
import eu.arrowhead.client.misc.ServiceDefinitions;
import eu.arrowhead.client.misc.ServiceInterfaces;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.services.*;
import eu.arrowhead.client.services.model.ArrowheadService;
import eu.arrowhead.client.services.model.ArrowheadSystem;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryQuery;
import eu.arrowhead.client.services.response.ServiceQueryResult;
import eu.arrowhead.client.transport.ProtocolConfiguration;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.utils.LogUtils;
import eu.arrowhead.client.utils.UriUtils;
import eu.arrowhead.onboarding.impl.SSLContextBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Objects;

public class ArrowheadClientBuilder extends SSLContextBuilder<ArrowheadClientBuilder>
{
    private final Logger logger = LogManager.getLogger();

    private final Transport transport;
    private SSLContextBuilder sslContextBuilder;
    private SystemEndpointHolder endpointHolder;
    private ServiceRegistry serviceRegistry;
    private Orchestrator orchestrator;

    private ArrowheadClientBuilder(final ProtocolConfiguration protocol, final Transport transport)
    {
        super(protocol);
        this.transport = transport;
        this.endpointHolder = new SystemEndpointHolder(protocol);
    }

    public static ArrowheadClientBuilder withOrchestrator(final ProtocolConfiguration protocol,
                                                          final InetAddress orchestratorAddress,
                                                          final Transport transport)
    {
        Objects.requireNonNull(protocol);
        Objects.requireNonNull(orchestratorAddress);
        Objects.requireNonNull(transport);

        final UriUtils uriUtils = new UriUtils(protocol, orchestratorAddress, protocol.getInt(ServiceRegistry.PORT_PROPERTY), ServiceRegistry.SYSTEM_SUFFIX);
        return withOrchestrator(protocol, uriUtils.copyBuild(), transport);
    }

    public static ArrowheadClientBuilder withServiceRegistry(final ProtocolConfiguration protocol,
                                                             final InetAddress serviceRegistryAddress,
                                                             final Transport transport)
    {
        Objects.requireNonNull(protocol);
        Objects.requireNonNull(serviceRegistryAddress);
        Objects.requireNonNull(transport);

        final UriUtils uriUtils = new UriUtils(protocol, serviceRegistryAddress, protocol.getInt(ServiceRegistry.PORT_PROPERTY), ServiceRegistry.SYSTEM_SUFFIX);
        return withServiceRegistry(protocol, uriUtils.copyBuild(), transport);
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

    public ArrowheadClientBuilder withSSLContextBuilder(final SSLContextBuilder sslContextBuilder)
    {
        super.copyFrom(sslContextBuilder);
        return this;
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

                serviceRegistry = new ServiceRegistryImpl(null, serviceRegistryUri, transport, this);
            }

            // query service registry for orchestration service
            orchestratorUri = getSystemUri(ServiceDefinitions.ORCHESTRATION, Orchestrator.SYSTEM_SUFFIX);
        }

        return new OrchestratorImpl(null, orchestratorUri, transport, this);
    }

    private URI getSystemUri(final ServiceDefinitions definition, final String serviceSuffix)
    {
        try
        {
            final String serviceDefinition = definition.getServiceDefinition(protocol);
            final String serviceInterface = ServiceInterfaces.JSON.forProtocol(protocol);
            final ServiceRegistryQuery serviceRegistryQuery = new ServiceRegistryQuery(new ArrowheadService(serviceDefinition, serviceInterface));
            serviceRegistryQuery.setPingProviders(true);

            final ServiceQueryResult queryResult = serviceRegistry.query(serviceRegistryQuery);
            if (queryResult.getServiceQueryData().isEmpty())
            {
                throw new RuntimeException("No OrchestrationService found");
            }

            final ServiceRegistryEntry registryEntry = queryResult.getServiceQueryData().get(0);
            final ArrowheadSystem provider = registryEntry.getProvider();

            final UriUtils uriUtils = new UriUtils(protocol, provider.getAddress(), provider.getPort(), registryEntry.getServiceURI());
            final UriBuilder uriBuilder = UriComponentsBuilder.fromUri(uriUtils.copyBuild());
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
        buildSslContext();

        final ArrowheadClientImpl client = new ArrowheadClientImpl(endpointHolder, transport);
        orchestrator = getOrchestrator();

        client.setDeviceRegistry(createImpl(DeviceRegistryImpl.class, client, ServiceDefinitions.DEVICE_REGISTRY, DeviceRegistry.SYSTEM_SUFFIX));
        client.setSystemRegistry(createImpl(SystemRegistryImpl.class, client, ServiceDefinitions.SYSTEM_REGISTRY, SystemRegistry.SYSTEM_SUFFIX));
        client.setServiceRegistry(createImpl(ServiceRegistryImpl.class, client, ServiceDefinitions.SERVICE_REGISTRY, ServiceRegistry.SYSTEM_SUFFIX));
        client.setOnboardingController(createImpl(OnboardingControllerImpl.class, client, ServiceDefinitions.ONBOARDING, OnboardingController.SYSTEM_SUFFIX));
        client.setOrchestrator(createImpl(OrchestratorImpl.class, client, ServiceDefinitions.ORCHESTRATION, Orchestrator.SYSTEM_SUFFIX));
        client.setEventHandler(createImpl(EventHandlerImpl.class, client, ServiceDefinitions.EVENT_SUBSCRIPTION, EventHandler.SYSTEM_SUFFIX));

        return client;
    }

    private <T> T createImpl(final Class<T> cls, final ArrowheadClient client, final ServiceDefinitions definition, final String suffix)
    {
        try
        {
            final URI uri = getSystemUri(definition, suffix);
            final Constructor<T> constructor = cls.getDeclaredConstructor(ArrowheadClient.class, URI.class, Transport.class, SSLContextBuilder.class);
            return constructor.newInstance(client, uri, transport, this);
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            logger.fatal("Unable to create new instance", e);
            throw new RuntimeException(e);
        }
    }
}
