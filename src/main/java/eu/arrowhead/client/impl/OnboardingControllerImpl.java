package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.services.OnboardingController;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.services.response.OnboardingResponse;

import java.net.URI;

public class OnboardingControllerImpl extends ServiceClientImpl implements OnboardingController
{
    public OnboardingControllerImpl(final ArrowheadClient arrowheadClient, final URI systemUri, final Transport transport)
    {
        super(arrowheadClient, systemUri, transport);
    }

    @Override
    public OnboardingResponse plain(final OnboardingRequest request) throws TransportException
    {
        return transport.post(OnboardingResponse.class, uriUtils.copyBuild(METHOD_PLAIN_SUFFIX), request);
    }

    @Override
    public OnboardingResponse withSharedKey(final OnboardingWithSharedKeyRequest request) throws TransportException
    {
        return transport.post(OnboardingResponse.class, uriUtils.copyBuild(METHOD_SHARED_KEY_SUFFIX), request);
    }

    @Override
    public OnboardingResponse withCertificate(final OnboardingWithCertificateRequest request) throws TransportException
    {
        return transport.post(OnboardingResponse.class, uriUtils.copyBuild(METHOD_CERTIFICATE_SUFFIX), request);
    }
}
