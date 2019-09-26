package eu.arrowhead.client.services.model;

import eu.arrowhead.client.transport.ProtocolConfiguration;
import eu.arrowhead.client.misc.ServiceInterfaces;

import java.util.*;

public class ArrowheadService
{
    private Long id;
    private String serviceDefinition;
    private Set<String> interfaces;
    private Map<String, String> serviceMetadata = new HashMap<>();

    public ArrowheadService()
    {
        super();
    }

    public ArrowheadService(final String serviceDefinition, final String... interfaces)
    {
        this.serviceDefinition = serviceDefinition;
        this.interfaces = new HashSet<String>(Arrays.asList(interfaces));
    }

    public ArrowheadService(final String serviceDefinition, final ProtocolConfiguration configuration, final ServiceInterfaces... interfaces)
    {
        this.serviceDefinition = serviceDefinition;
        this.interfaces = new HashSet<>();

        for (ServiceInterfaces anInterface : interfaces)
        {
            this.interfaces.add(anInterface.forProtocol(configuration));
        }
    }

    public ArrowheadService(final Long id, final String serviceDefinition, final Set<String> interfaces, final Map<String, String> serviceMetadata)
    {
        this.id = id;
        this.serviceDefinition = serviceDefinition;
        this.interfaces = interfaces;
        this.serviceMetadata = serviceMetadata;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getServiceDefinition()
    {
        return serviceDefinition;
    }

    public void setServiceDefinition(final String serviceDefinition)
    {
        this.serviceDefinition = serviceDefinition;
    }

    public Set<String> getInterfaces()
    {
        return interfaces;
    }

    public void setInterfaces(final Set<String> interfaces)
    {
        this.interfaces = interfaces;
    }

    public Map<String, String> getServiceMetadata()
    {
        return serviceMetadata;
    }

    public void setServiceMetadata(final Map<String, String> serviceMetadata)
    {
        this.serviceMetadata = serviceMetadata;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ArrowheadService [");
        sb.append("id=").append(id);
        sb.append(", serviceDefinition='").append(serviceDefinition).append('\'');
        sb.append(", interfaces=").append(interfaces);
        sb.append(", serviceMetadata=").append(serviceMetadata);
        sb.append(']');
        return sb.toString();
    }
}
