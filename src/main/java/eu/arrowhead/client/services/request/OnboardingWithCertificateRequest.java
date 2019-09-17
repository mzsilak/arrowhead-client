package eu.arrowhead.client.services.request;

public class OnboardingWithCertificateRequest
{
    private String name;
    private String certificateRequest;

    public OnboardingWithCertificateRequest()
    {
        super();
    }

    public OnboardingWithCertificateRequest(final String name, final String certificateRequest)
    {
        this.name = name;
        this.certificateRequest = certificateRequest;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getCertificateRequest()
    {
        return certificateRequest;
    }

    public void setCertificateRequest(final String certificateRequest)
    {
        this.certificateRequest = certificateRequest;
    }
}
