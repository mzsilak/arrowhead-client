package eu.arrowhead.client.services.response;

import eu.arrowhead.client.services.model.ServiceEndpoint;

import java.util.Arrays;
import java.util.StringJoiner;

public class OnboardingResponse
{
    private boolean success;
    private ServiceEndpoint[] services;
    private String onboardingCertificate;
    private String intermediateCertificate;
    private String rootCertificate;
    private String keyAlgorithm;
    private String keyFormat;
    private String privateKey;
    private String publicKey;

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

    public String getPrivateKey()
    {
        return privateKey;
    }

    public void setPrivateKey(final String privateKey)
    {
        this.privateKey = privateKey;
    }

    public String getPublicKey()
    {
        return publicKey;
    }

    public void setPublicKey(final String publicKey)
    {
        this.publicKey = publicKey;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", OnboardingResponse.class.getSimpleName() + "[", "]")
                .add("success=" + success)
                .add("services=" + Arrays.toString(services))
                .add("onboardingCertificate='...'")
                .add("intermediateCertificate='...'")
                .add("rootCertificate='...'")
                .add("keyAlgorithm='" + keyAlgorithm + "'")
                .add("keyFormat='" + keyFormat + "'")
                .add("publicKey='" + publicKey + "'")
                .add("privateKey='...'")
                .toString();
    }
}
