package eu.arrowhead.client.services.model;

public class ArrowheadSystem
{
    private String id;
    private String systemName;
    private String address;
    private Integer port;
    private String authenticationInfo;

    public ArrowheadSystem()
    {
        super();
    }

    public ArrowheadSystem(final String systemName, final String address, final Integer port)
    {
        this.systemName = systemName;
        this.address = address;
        this.port = port;
    }

    public ArrowheadSystem(final String id, final String systemName, final String address, final Integer port, final String authenticationInfo)
    {
        this.id = id;
        this.systemName = systemName;
        this.address = address;
        this.port = port;
        this.authenticationInfo = authenticationInfo;
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getSystemName()
    {
        return systemName;
    }

    public void setSystemName(final String systemName)
    {
        this.systemName = systemName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(final String address)
    {
        this.address = address;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(final Integer port)
    {
        this.port = port;
    }

    public String getAuthenticationInfo()
    {
        return authenticationInfo;
    }

    public void setAuthenticationInfo(final String authenticationInfo)
    {
        this.authenticationInfo = authenticationInfo;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ArrowheadSystem [");
        sb.append("id='").append(id).append('\'');
        sb.append(", systemName='").append(systemName).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", port=").append(port);
        sb.append(", authenticationInfo='").append(authenticationInfo).append('\'');
        sb.append(']');
        return sb.toString();
    }
}
