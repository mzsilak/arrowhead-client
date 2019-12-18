package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.impl.OnboardingControllerImpl;
import eu.arrowhead.client.misc.CoreSystems;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.services.*;
import eu.arrowhead.client.services.model.ServiceEndpoint;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.services.response.OnboardingResponse;
import eu.arrowhead.client.transport.*;
import eu.arrowhead.client.utils.UriUtils;
import eu.arrowhead.onboarding.OnboardingClient;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

import static eu.arrowhead.client.services.OnboardingController.PORT_PROPERTY;
import static eu.arrowhead.client.services.OnboardingController.SYSTEM_SUFFIX;

public class OnboardingClientImpl implements OnboardingClient
{
    private final Logger logger = LogManager.getLogger();

    private final SSLContextBuilder<?> sslContextBuilder;
    private final ProtocolConfiguration protocol;
    private final Transport transport;
    private final RetryHandler retryHandler;
    private final SystemEndpointHolder endpointHolder;
    private final UriUtils uriUtils;
    private final OnboardingController onboardingController;

    public OnboardingClientImpl(final ProtocolConfiguration protocol, final InetAddress inetAddress, final OnboardingClientBuilder builder)
    {
        this.uriUtils = new UriUtils(Objects.requireNonNull(protocol), Objects.requireNonNull(inetAddress), protocol.getInt(PORT_PROPERTY), SYSTEM_SUFFIX);
        this.sslContextBuilder = Objects.requireNonNull(builder);
        this.protocol = protocol;
        this.transport = protocol.getTransport();

        this.retryHandler = new RetryHandler();
        this.retryHandler.setMaxRetries(builder.getRetries());
        this.retryHandler.setDelayBetweenRetries(builder.getDelayBetweenRetries(), builder.getTimeUnitForRetries());
        transport.setRetryHandler(retryHandler);

        this.endpointHolder = new SystemEndpointHolder(protocol);
        this.endpointHolder.add(CoreSystems.ONBOARDING_CONTROLLER, uriUtils.copyBuild());

        this.onboardingController = new OnboardingControllerImpl(null, uriUtils.copyBuild(), transport, sslContextBuilder);
        logger.debug("Created {}", this);
    }

    private DeviceRegistryOnboarding processResponse(final String name,
                                                     final OnboardingResponse response) throws SSLConfigurationException
    {
        adaptEndpoints(response.getServices());
        adaptSSLContext(name, response);
        return new DeviceRegistryOnboardingImpl(this, endpointHolder, transport, retryHandler, sslContextBuilder);
    }

    @Override
    public DeviceRegistryOnboarding plain(final OnboardingRequest request) throws TransportException, SSLConfigurationException
    {
        final OnboardingResponse response = onboardingController.plain(request);
        return processResponse(request.getName(), response);
    }

    @Override
    public DeviceRegistryOnboarding withSharedKey(final OnboardingWithSharedKeyRequest request) throws TransportException, SSLConfigurationException
    {
        final OnboardingResponse response = onboardingController.withSharedKey(request);
        return processResponse(request.getName(), response);
    }

    @Override
    public DeviceRegistryOnboarding withCertificate(final OnboardingWithCertificateRequest request) throws TransportException, SSLConfigurationException
    {
        final OnboardingResponse response = onboardingController.withCertificate(request);
        return processResponse(request.getName(), response);
    }

    private void adaptEndpoints(final ServiceEndpoint... endpoints)
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
    }

    private void adaptSSLContext(final String name, final OnboardingResponse response) throws SSLConfigurationException
    {
        try
        {
            logger.info("Adapting SSLContext ...");
            final PrivateKey privateKey = sslContextBuilder.parsePrivateKey(response.getPrivateKey(), response.getKeyAlgorithm());
            final Certificate[] chain = sslContextBuilder.parseCertificateChain(response.getKeyFormat(),
                                                                                response.getOnboardingCertificate(),
                                                                                response.getIntermediateCertificate(),
                                                                                response.getRootCertificate());

            sslContextBuilder.storeKeyEntry(String.format("arrowhead-%s-onboarding-certificate", name), privateKey, chain);
            sslContextBuilder.storeCertificateEntry("arrowhead-intermediate-certificate", chain[1]);
            sslContextBuilder.storeCertificateEntry("arrowhead-root-certificate", chain[2]);
            sslContextBuilder.reloadSSLContext();
        }
        catch (KeyStoreException | NoSuchAlgorithmException | InvalidKeySpecException | CertificateException | NoSuchProviderException | IOException e)
        {
            throw new SSLConfigurationException(e.getMessage(), e);
        }
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("OnboardingClientImpl [");
        sb.append("sslContext=").append(sslContextBuilder);
        sb.append(", protocol=").append(protocol);
        sb.append(", transport=").append(transport);
        sb.append(", endpointHolder=").append(endpointHolder);
        sb.append(", uriUtil=").append(uriUtils);
        sb.append(']');
        return sb.toString();
    }
}
