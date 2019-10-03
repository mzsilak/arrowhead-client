package eu.arrowhead.client.services.response;

import eu.arrowhead.client.services.request.ServiceRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class ServiceQueryResult
{
    private List<ServiceRegistryEntry> serviceQueryData = new ArrayList<>();

    public ServiceQueryResult()
    {
        super();
    }

    public ServiceQueryResult(final List<ServiceRegistryEntry> serviceQueryData)
    {
        this.serviceQueryData = serviceQueryData;
    }

    public List<ServiceRegistryEntry> getServiceQueryData()
    {
        return serviceQueryData;
    }

    public void setServiceQueryData(final List<ServiceRegistryEntry> serviceQueryData)
    {
        this.serviceQueryData = serviceQueryData;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ServiceQueryResult [");
        sb.append("serviceQueryData=(").append(serviceQueryData.size()).append(" services)");
        sb.append(']');
        return sb.toString();
    }
}
