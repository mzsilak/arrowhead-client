package eu.arrowhead.client.services.request;

import eu.arrowhead.client.services.model.ArrowheadService;

public class ServiceRegistryQuery
{
    private ArrowheadService service;
    private boolean pingProviders = false;
    private boolean metadataSearch = false;
    private Integer version = 0;

    public ServiceRegistryQuery()
    {
        super();
    }

    public ServiceRegistryQuery(final ArrowheadService service)
    {
        this.service = service;
    }

    public ServiceRegistryQuery(final ArrowheadService service, final boolean pingProviders, final boolean metadataSearch, final Integer version)
    {
        this.service = service;
        this.pingProviders = pingProviders;
        this.metadataSearch = metadataSearch;
        this.version = version;
    }

    public ArrowheadService getService()
    {
        return service;
    }

    public void setService(final ArrowheadService service)
    {
        this.service = service;
    }

    public boolean isPingProviders()
    {
        return pingProviders;
    }

    public void setPingProviders(final boolean pingProviders)
    {
        this.pingProviders = pingProviders;
    }

    public boolean isMetadataSearch()
    {
        return metadataSearch;
    }

    public void setMetadataSearch(final boolean metadataSearch)
    {
        this.metadataSearch = metadataSearch;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(final Integer version)
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ServiceRegistryQuery [");
        sb.append("service=").append(service);
        sb.append(", pingProviders=").append(pingProviders);
        sb.append(", metadataSearch=").append(metadataSearch);
        sb.append(", version=").append(version);
        sb.append(']');
        return sb.toString();
    }
}
