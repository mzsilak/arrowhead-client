package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.impl.OnboardingControllerImpl;
import eu.arrowhead.client.misc.*;
import eu.arrowhead.client.services.*;
import eu.arrowhead.client.services.model.ServiceEndpoint;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.services.response.OnboardingResponse;
import eu.arrowhead.client.transport.ProtocolConfiguration;
import eu.arrowhead.client.transport.RetryHandler;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.utils.UriUtils;
import eu.arrowhead.onboarding.OnboardingClient;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.util.Objects;

import static eu.arrowhead.client.services.OnboardingController.PORT_PROPERTY;
import static eu.arrowhead.client.services.OnboardingController.SYSTEM_SUFFIX;

public class OnboardingClientImpl implements OnboardingClient
{
    private final Logger logger = LogManager.getLogger();

    private final SSLContext sslContext;
    private final ProtocolConfiguration protocol;
    private final Transport transport;
    private final RetryHandler retryHandler;
    private final SystemEndpointHolder endpointHolder;
    private final UriUtils uriUtils;
    private final OnboardingController onboardingController;

    public OnboardingClientImpl(final ProtocolConfiguration protocol, final InetAddress inetAddress, final OnboardingClientBuilder builder)
    {
        this.uriUtils = new UriUtils(Objects.requireNonNull(protocol), Objects.requireNonNull(inetAddress), protocol.getInt(PORT_PROPERTY), SYSTEM_SUFFIX);
        this.sslContext = Objects.requireNonNull(builder.getSslContext());
        this.protocol = protocol;
        this.transport = protocol.getTransport();

        this.retryHandler = new RetryHandler();
        this.retryHandler.setMaxRetries(builder.getRetries());
        this.retryHandler.setDelayBetweenRetries(builder.getDelayBetweenRetries(), builder.getTimeUnitForRetries());

        this.endpointHolder = new SystemEndpointHolder(protocol);
        this.endpointHolder.add(CoreSystems.ONBOARDING_CONTROLLER, uriUtils.copyBuild());

        this.onboardingController = new OnboardingControllerImpl(null, uriUtils.copyBuild(), transport);
        logger.debug("Created {}", this);
    }

    @Override
    public DeviceRegistryOnboarding plain(final OnboardingRequest request) throws TransportException
    {
        final OnboardingResponse response = retryHandler.invoke(() -> onboardingController.plain(request));
        adaptEndpoints(response.getServices());
        adaptSSLContext(response);
        return new DeviceRegistryOnboardingImpl(this, endpointHolder, transport, retryHandler);
    }

    @Override
    public DeviceRegistryOnboarding withSharedKey(final OnboardingWithSharedKeyRequest request) throws TransportException
    {
        final OnboardingResponse response = retryHandler.invoke(() -> onboardingController.withSharedKey(request));
        adaptEndpoints(response.getServices());
        adaptSSLContext(response);
        return new DeviceRegistryOnboardingImpl(this, endpointHolder, transport, retryHandler);
    }

    @Override
    public DeviceRegistryOnboarding withCertificate(final OnboardingWithCertificateRequest request) throws TransportException
    {
        final OnboardingResponse response = retryHandler.invoke(() -> onboardingController.withCertificate(request));
        adaptEndpoints(response.getServices());
        adaptSSLContext(response);
        return new DeviceRegistryOnboardingImpl(this, endpointHolder, transport, retryHandler);
    }

    private void adaptSSLContext(final OnboardingResponse response)
    {
    }

    private SystemEndpointHolder adaptEndpoints(final ServiceEndpoint... endpoints)
    {
        for (ServiceEndpoint endpoint : endpoints)
        {
            UriBuilder builder = UriComponentsBuilder.fromUri(endpoint.getUri());
            logger.debug("Adapting endpoint of {} to {}", endpoint.getSystem(), builder.build());

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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("OnboardingClientImpl [");
        sb.append("sslContext=").append(sslContext);
        sb.append(", protocol=").append(protocol);
        sb.append(", transport=").append(transport);
        sb.append(", endpointHolder=").append(endpointHolder);
        sb.append(", uriUtil=").append(uriUtils);
        sb.append(']');
        return sb.toString();
    }
}
