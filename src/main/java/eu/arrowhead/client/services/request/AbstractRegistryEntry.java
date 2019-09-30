package eu.arrowhead.client.services.request;

import java.time.LocalDateTime;

public abstract class AbstractRegistryEntry
{
    protected String serviceURI;
    protected LocalDateTime endOfValidity;

    public AbstractRegistryEntry()
    { super(); }

    public String getServiceURI()
    {
        return serviceURI;
    }

    public void setServiceURI(final String serviceURI)
    {
        this.serviceURI = serviceURI;
    }

    public LocalDateTime getEndOfValidity()
    {
        return endOfValidity;
    }

    public void setEndOfValidity(final LocalDateTime endOfValidity)
    {
        this.endOfValidity = endOfValidity;
    }

    protected void appendFields(final StringBuilder sb)
    {
        sb.append("serviceURI='").append(serviceURI).append('\'');
        sb.append(", endOfValidity=").append(endOfValidity);
    }
}
