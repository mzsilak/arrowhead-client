package eu.arrowhead.client.services.request;

public class OnboardingWithSharedKeyRequest
{
    private String name;
    private String sharedKey;

    public OnboardingWithSharedKeyRequest()
    {
        super();
    }

    public OnboardingWithSharedKeyRequest(final String name, final String sharedKey)
    {
        this.name = name;
        this.sharedKey = sharedKey;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getSharedKey()
    {
        return sharedKey;
    }

    public void setSharedKey(final String sharedKey)
    {
        this.sharedKey = sharedKey;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("OnboardingWithSharedKeyRequest [");
        sb.append("name='").append(name).append('\'');
        sb.append(", sharedKey='").append(sharedKey).append('\'');
        sb.append(']');
        return sb.toString();
    }
}
