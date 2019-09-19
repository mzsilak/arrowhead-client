package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.misc.*;
import eu.arrowhead.client.services.DeviceRegistry;
import eu.arrowhead.client.services.Orchestrator;
import eu.arrowhead.client.services.ServiceRegistry;
import eu.arrowhead.client.services.SystemRegistry;
import eu.arrowhead.client.services.model.ServiceEndpoint;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.services.response.OnboardingResponse;
import eu.arrowhead.client.utils.UriUtil;
import eu.arrowhead.onboarding.OnboardingClient;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.util.Objects;

public class OnboardingClientImpl implements OnboardingClient
{
    public static final String SERVICE_SUFFIX = "onboarding";

    private static final String PORT_PROPERTY = "onboarding.port";
    private static final String METHOD_PLAIN_SUFFIX = "plain";
    private static final String METHOD_SHARED_KEY_SUFFIX = "sharedKey";
    private static final String METHOD_CERTIFICATE_SUFFIX = "certificate";

    private final SSLContext sslContext;
    private final ProtocolConfiguration protocol;
    private final Transport transport;
    private final SystemEndpointHolder endpointHolder;
    private final UriUtil uriUtil;

    public OnboardingClientImpl(final ProtocolConfiguration protocol, final InetAddress inetAddress, final OnboardingClientBuilder builder)
    {
        this.uriUtil = new UriUtil(Objects.requireNonNull(protocol), Objects.requireNonNull(inetAddress), protocol.getInt(PORT_PROPERTY), SERVICE_SUFFIX);
        this.sslContext = Objects.requireNonNull(builder.getSslContext());
        this.protocol = protocol;
        transport = protocol.getTransport();
        transport.setMaxRetries(builder.getRetries());
        transport.setDelayBetweenRetries(builder.getDelayBetweenRetries(), builder.getTimeUnitForRetries());

        endpointHolder = new SystemEndpointHolder(protocol);
        endpointHolder.add(CoreSystems.ONBOARDING_CONTROLLER, uriUtil.copyBuild());
    }

    @Override
    public DeviceRegistryOnboarding plain(final OnboardingRequest request) throws TransportException
    {
        final OnboardingResponse response = transport.post(OnboardingResponse.class, uriUtil.copyBuild(METHOD_PLAIN_SUFFIX), request);
        adaptEndpoints(response.getServices());
        adaptSSLContext(response);
        return new DeviceRegistryOnboardingImpl(this, endpointHolder, transport);
    }

    @Override
    public DeviceRegistryOnboarding withSharedKey(final OnboardingWithSharedKeyRequest request) throws TransportException
    {
        final OnboardingResponse response = transport.post(OnboardingResponse.class, uriUtil.copyBuild(METHOD_SHARED_KEY_SUFFIX), request);
        adaptEndpoints(response.getServices());
        adaptSSLContext(response);
        return new DeviceRegistryOnboardingImpl(this, endpointHolder, transport);
    }

    @Override
    public DeviceRegistryOnboarding withCertificate(final OnboardingWithCertificateRequest request) throws TransportException
    {
        final OnboardingResponse response = transport.post(OnboardingResponse.class, uriUtil.copyBuild(METHOD_CERTIFICATE_SUFFIX), request);
        adaptEndpoints(response.getServices());
        adaptSSLContext(response);
        return new DeviceRegistryOnboardingImpl(this, endpointHolder, transport);
    }

    private void adaptSSLContext(final OnboardingResponse response)
    {
    }

    private SystemEndpointHolder adaptEndpoints(final ServiceEndpoint... endpoints)
    {
        for (ServiceEndpoint endpoint : endpoints)
        {
            UriBuilder builder = UriComponentsBuilder.fromUri(endpoint.getUri());

            switch (endpoint.getSystem())
            {
                case DEVICE_REGISTRY_SERVICE:
                    endpointHolder.add(CoreSystems.DEVICE_REGISTRY, builder.replacePath(DeviceRegistry.SYSTEM_SUFFIX).build());
                    break;
                case SYSTEM_REGISTRY_SERVICE:
                    endpointHolder.add(CoreSystems.SYSTEM_REGISTRY, builder.replacePath(SystemRegistry.SYSTEM_SUFFIX).build());
                    break;
                case SERVICE_REGISTRY_SERVICE:
                    endpointHolder.add(CoreSystems.SERVICE_REGISTRY, builder.replacePath(ServiceRegistry.SYSTEM_SUFFIX).build());
                    break;
                case ORCH_SERVICE:
                    endpointHolder.add(CoreSystems.ORCHESTRATOR, builder.replacePath(Orchestrator.SYSTEM_SUFFIX).build());
                    break;
                default:
                    break;
            }
        }

        return endpointHolder;
    }
}
