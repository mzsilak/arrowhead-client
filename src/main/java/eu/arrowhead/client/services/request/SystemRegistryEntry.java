package eu.arrowhead.client.services.request;

import eu.arrowhead.client.services.model.ArrowheadDevice;
import eu.arrowhead.client.services.model.ArrowheadSystem;

import java.time.LocalDateTime;

public class SystemRegistryEntry extends AbstractRegistryEntry
{
    private Long id;
    private ArrowheadSystem providedSystem;
    private ArrowheadDevice provider;

    public SystemRegistryEntry()
    {
        super();
    }

    public SystemRegistryEntry(final ArrowheadDevice provider, final ArrowheadSystem providedSystem, final String serviceURI, final LocalDateTime endOfValidity)
    {
        this.provider = provider;
        this.providedSystem = providedSystem;
        this.serviceURI = serviceURI;
        this.endOfValidity = endOfValidity;
    }

    public SystemRegistryEntry(final Long id, final ArrowheadDevice provider, final ArrowheadSystem providedSystem, final String serviceURI,
                               final LocalDateTime endOfValidity)
    {
        this.id = id;
        this.provider = provider;
        this.providedSystem = providedSystem;
        this.serviceURI = serviceURI;
        this.endOfValidity = endOfValidity;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public ArrowheadDevice getProvider()
    {
        return provider;
    }

    public void setProvider(final ArrowheadDevice provider)
    {
        this.provider = provider;
    }

    public ArrowheadSystem getProvidedSystem()
    {
        return providedSystem;
    }

    public void setProvidedSystem(final ArrowheadSystem providedSystem)
    {
        this.providedSystem = providedSystem;
    }

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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("SystemRegistryEntry [");
        sb.append("id=").append(id);
        sb.append(", providedSystem=").append(providedSystem);
        sb.append(", provider=").append(provider);
        super.appendFields(sb);
        sb.append(']');
        return sb.toString();
    }
}
