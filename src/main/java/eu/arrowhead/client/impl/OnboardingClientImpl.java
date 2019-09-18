package eu.arrowhead.client.impl;

import eu.arrowhead.client.OnboardingClient;
import eu.arrowhead.client.misc.*;
import eu.arrowhead.client.services.DeviceRegistryOnboarding;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.services.response.OnboardingResponse;
import eu.arrowhead.client.services.model.ServiceEndpoint;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.util.Objects;

public class OnboardingClientImpl extends ClientImpl implements OnboardingClient
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

    public OnboardingClientImpl(final ProtocolConfiguration protocol, final InetAddress inetAddress, final OnboardingClientBuilder builder)
    {
        super(Objects.requireNonNull(protocol), Objects.requireNonNull(inetAddress), protocol.getInt(PORT_PROPERTY), SERVICE_SUFFIX);
        this.sslContext = Objects.requireNonNull(builder.getSslContext());
        this.protocol = protocol;
        transport = protocol.getTransport();
        transport.setMaxRetries(builder.getRetries());
        transport.setDelayBetweenRetries(builder.getDelayBetweenRetries(), builder.getTimeUnitForRetries());

        endpointHolder = new SystemEndpointHolder();
        endpointHolder.add(CoreSystems.ONBOARDING_CONTROLLER, build());
    }

    @Override
    public DeviceRegistryOnboarding plain(final OnboardingRequest request) throws TransportException
    {
        final OnboardingResponse response = transport.post(OnboardingResponse.class, build(METHOD_PLAIN_SUFFIX), request);
        adaptEndpoints(response.getServices());
        adaptSSLContext(response);
        return new DeviceRegistryOnboardingImpl(this, endpointHolder, transport);
    }

    @Override
    public DeviceRegistryOnboarding withSharedKey(final OnboardingWithSharedKeyRequest request) throws TransportException
    {
        final OnboardingResponse response = transport.post(OnboardingResponse.class, build(METHOD_SHARED_KEY_SUFFIX), request);
        adaptEndpoints(response.getServices());
        adaptSSLContext(response);
        return new DeviceRegistryOnboardingImpl(this, endpointHolder, transport);
    }

    @Override
    public DeviceRegistryOnboarding withCertificate(final OnboardingWithCertificateRequest request) throws TransportException
    {
        final OnboardingResponse response = transport.post(OnboardingResponse.class, build(METHOD_CERTIFICATE_SUFFIX), request);
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
                    endpointHolder.add(CoreSystems.DEVICE_REGISTRY, builder.replacePath(DeviceRegistryOnboardingImpl.SERVICE_SUFFIX).build());
                    break;
                case SYSTEM_REGISTRY_SERVICE:
                    endpointHolder.add(CoreSystems.SYSTEM_REGISTRY, builder.replacePath(SystemRegistryOnboardingImpl.SERVICE_SUFFIX).build());
                    break;
                case SERVICE_REGISTRY_SERVICE:
                    endpointHolder.add(CoreSystems.SERVICE_REGISTRY, builder.replacePath(ServiceRegistryOnboardingImpl.SERVICE_SUFFIX).build());
                    break;
                case ORCH_SERVICE:
                    endpointHolder.add(CoreSystems.ORCHESTRATOR, builder.replacePath(OrchestratorImpl.SERVICE_SUFFIX).build());
                    break;
                default:
                    break;
            }
        }

        return endpointHolder;
    }
}
