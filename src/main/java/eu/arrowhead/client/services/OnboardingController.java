package eu.arrowhead.client.services;


import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.services.response.OnboardingResponse;

public interface OnboardingController extends ArrowheadClientFacet
{

    OnboardingResponse plain(final OnboardingRequest request) throws TransportException;

    OnboardingResponse withSharedKey(final OnboardingWithSharedKeyRequest request) throws TransportException;

    OnboardingResponse withCertificate(final OnboardingWithCertificateRequest request) throws TransportException;
}
