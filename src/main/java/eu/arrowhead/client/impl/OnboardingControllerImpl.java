package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.OnboardingController;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.services.response.OnboardingResponse;
import eu.arrowhead.client.utils.UriUtil;

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
        return null;
    }

    @Override
    public OnboardingResponse withSharedKey(final OnboardingWithSharedKeyRequest request) throws TransportException
    {
        return null;
    }

    @Override
    public OnboardingResponse withCertificate(final OnboardingWithCertificateRequest request) throws TransportException
    {
        return null;
    }
}
