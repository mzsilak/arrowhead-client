package eu.arrowhead.client.services.model;

public class ArrowheadDevice
{
    private Long id;
    private String deviceName;

    public ArrowheadDevice()
    {
        super();
    }

    public ArrowheadDevice(final String deviceName)
    {
        this.deviceName = deviceName;
    }

    public ArrowheadDevice(final Long id, final String deviceName)
    {
        this.id = id;
        this.deviceName = deviceName;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(final String deviceName)
    {
        this.deviceName = deviceName;
    }
}
