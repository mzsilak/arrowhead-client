package eu.arrowhead.client.services.response;

import eu.arrowhead.client.services.model.ServiceEndpoint;

public class OnboardingResponse
{
    private boolean success;
    private ServiceEndpoint[] services;
    private String onboardingCertificate;
    private String intermediateCertificate;
    private String rootCertificate;
    private String keyAlgorithm;
    private String keyFormat;
    private byte[] privateKey;
    private byte[] publicKey;

    public OnboardingResponse()
    {
        super();
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(final boolean success)
    {
        this.success = success;
    }

    public ServiceEndpoint[] getServices()
    {
        return services;
    }

    public void setServices(final ServiceEndpoint[] services)
    {
        this.services = services;
    }

    public String getOnboardingCertificate()
    {
        return onboardingCertificate;
    }

    public void setOnboardingCertificate(final String onboardingCertificate)
    {
        this.onboardingCertificate = onboardingCertificate;
    }

    public String getIntermediateCertificate()
    {
        return intermediateCertificate;
    }

    public void setIntermediateCertificate(final String intermediateCertificate)
    {
        this.intermediateCertificate = intermediateCertificate;
    }

    public String getRootCertificate()
    {
        return rootCertificate;
    }

    public void setRootCertificate(final String rootCertificate)
    {
        this.rootCertificate = rootCertificate;
    }

    public String getKeyAlgorithm()
    {
        return keyAlgorithm;
    }

    public void setKeyAlgorithm(final String keyAlgorithm)
    {
        this.keyAlgorithm = keyAlgorithm;
    }

    public String getKeyFormat()
    {
        return keyFormat;
    }

    public void setKeyFormat(final String keyFormat)
    {
        this.keyFormat = keyFormat;
    }

    public byte[] getPrivateKey()
    {
        return privateKey;
    }

    public void setPrivateKey(final byte[] privateKey)
    {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey()
    {
        return publicKey;
    }

    public void setPublicKey(final byte[] publicKey)
    {
        this.publicKey = publicKey;
    }
}
