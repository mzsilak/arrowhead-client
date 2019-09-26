package eu.arrowhead.client.services;


import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.services.response.OnboardingResponse;

public interface OnboardingController extends ArrowheadClientFacet
{
    String SYSTEM_SUFFIX = "onboarding";
    String PORT_PROPERTY = "onboarding.port";

    String METHOD_PLAIN_SUFFIX = "plain";
    String METHOD_SHARED_KEY_SUFFIX = "sharedKey";
    String METHOD_CERTIFICATE_SUFFIX = "certificate";

    OnboardingResponse plain(final OnboardingRequest request) throws TransportException;

    OnboardingResponse withSharedKey(final OnboardingWithSharedKeyRequest request) throws TransportException;

    OnboardingResponse withCertificate(final OnboardingWithCertificateRequest request) throws TransportException;
}
