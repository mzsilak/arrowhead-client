package eu.arrowhead.client.services.model;

import java.net.URI;

public class ServiceEndpoint
{
    public enum Type
    {
        DEVICE_REGISTRY_SERVICE, SYSTEM_REGISTRY_SERVICE, SERVICE_REGISTRY_SERVICE, ORCH_SERVICE
    }

    private Type system;
    private URI uri;

    public Type getSystem()
    {
        return system;
    }

    public void setSystem(Type system)
    {
        this.system = system;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ServiceEndpoint [");
        sb.append("system=").append(system);
        sb.append(", uri=").append(uri);
        sb.append(']');
        return sb.toString();
    }
}
