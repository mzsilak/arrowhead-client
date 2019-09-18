package eu.arrowhead.client.services.request;

import eu.arrowhead.client.services.model.ArrowheadDevice;

import java.time.LocalDateTime;

public class DeviceRegistryEntry
{
    private Long id;
    private String macAddress;
    private LocalDateTime endOfValidity;
    private ArrowheadDevice device;

    public DeviceRegistryEntry()
    {
        super();
    }

    public DeviceRegistryEntry(final String macAddress, final LocalDateTime endOfValidity, final ArrowheadDevice device)
    {
        this.macAddress = macAddress;
        this.endOfValidity = endOfValidity;
        this.device = device;
    }

    public DeviceRegistryEntry(final Long id, final String macAddress, final LocalDateTime endOfValidity, final ArrowheadDevice device)
    {
        this.id = id;
        this.macAddress = macAddress;
        this.endOfValidity = endOfValidity;
        this.device = device;
    }


    public DeviceRegistryEntry(final String macAddress, final LocalDateTime endOfValidity, final String deviceName)
    {
        this.macAddress = macAddress;
        this.endOfValidity = endOfValidity;
        this.device = new ArrowheadDevice(deviceName);
    }

    public DeviceRegistryEntry(final Long id, final String macAddress, final LocalDateTime endOfValidity, final String deviceName)
    {
        this.id = id;
        this.macAddress = macAddress;
        this.endOfValidity = endOfValidity;
        this.device = new ArrowheadDevice(deviceName);
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getMacAddress()
    {
        return macAddress;
    }

    public void setMacAddress(final String macAddress)
    {
        this.macAddress = macAddress;
    }

    public LocalDateTime getEndOfValidity()
    {
        return endOfValidity;
    }

    public void setEndOfValidity(final LocalDateTime endOfValidity)
    {
        this.endOfValidity = endOfValidity;
    }

    public ArrowheadDevice getDevice()
    {
        return device;
    }

    public void setDevice(final ArrowheadDevice device)
    {
        this.device = device;
    }
}
