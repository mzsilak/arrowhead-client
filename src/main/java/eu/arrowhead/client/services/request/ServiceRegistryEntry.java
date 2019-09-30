package eu.arrowhead.client.services.request;

import eu.arrowhead.client.services.model.ArrowheadService;
import eu.arrowhead.client.services.model.ArrowheadSystem;

import java.time.LocalDateTime;

public class ServiceRegistryEntry extends AbstractRegistryEntry
{
    private Long id;
    private ArrowheadService providedService;
    private ArrowheadSystem provider;
    private Boolean udp;

    public ServiceRegistryEntry()
    {
        super();
    }

    public ServiceRegistryEntry(final ArrowheadService providedService, final ArrowheadSystem provider, final String serviceURI)
    {
        this.providedService = providedService;
        this.provider = provider;
        this.serviceURI = serviceURI;
    }

    public ServiceRegistryEntry(final Long id, final ArrowheadService providedService, final ArrowheadSystem provider, final String serviceURI,
                                final Boolean udp,
                                final LocalDateTime endOfValidity)
    {
        this.id = id;
        this.providedService = providedService;
        this.provider = provider;
        this.serviceURI = serviceURI;
        this.udp = udp;
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

    public ArrowheadService getProvidedService()
    {
        return providedService;
    }

    public void setProvidedService(final ArrowheadService providedService)
    {
        this.providedService = providedService;
    }

    public ArrowheadSystem getProvider()
    {
        return provider;
    }

    public void setProvider(final ArrowheadSystem provider)
    {
        this.provider = provider;
    }

    public Boolean getUdp()
    {
        return udp;
    }

    public void setUdp(final Boolean udp)
    {
        this.udp = udp;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ServiceRegistryEntry [");
        sb.append("id=").append(id);
        sb.append(", providedService=").append(providedService);
        sb.append(", provider=").append(provider);
        sb.append(", udp=").append(udp);
        super.appendFields(sb);
        sb.append(']');
        return sb.toString();
    }
}
