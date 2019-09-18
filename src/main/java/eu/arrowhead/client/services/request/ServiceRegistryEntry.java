package eu.arrowhead.client.services.request;

import eu.arrowhead.client.services.model.ArrowheadService;
import eu.arrowhead.client.services.model.ArrowheadSystem;

import java.time.LocalDateTime;

public class ServiceRegistryEntry
{
    private Long id;
    private ArrowheadService providedService;
    private ArrowheadSystem provider;
    private String serviceURI;
    private Boolean udp;
    private LocalDateTime endOfValidity;

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

    public String getServiceURI()
    {
        return serviceURI;
    }

    public void setServiceURI(final String serviceURI)
    {
        this.serviceURI = serviceURI;
    }

    public Boolean getUdp()
    {
        return udp;
    }

    public void setUdp(final Boolean udp)
    {
        this.udp = udp;
    }

    public LocalDateTime getEndOfValidity()
    {
        return endOfValidity;
    }

    public void setEndOfValidity(final LocalDateTime endOfValidity)
    {
        this.endOfValidity = endOfValidity;
    }
}
