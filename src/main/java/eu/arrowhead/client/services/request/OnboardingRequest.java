package eu.arrowhead.client.services.request;

public class OnboardingRequest
{
    private String name;

    public OnboardingRequest()
    {
        super();
    }

    public OnboardingRequest(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }
}
