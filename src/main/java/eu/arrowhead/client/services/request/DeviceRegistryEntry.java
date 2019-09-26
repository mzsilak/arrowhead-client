package eu.arrowhead.client.services.request;

import eu.arrowhead.client.services.model.ArrowheadDevice;

import java.time.LocalDateTime;

public class DeviceRegistryEntry
{
    private Long id;
    private String macAddress;
    private LocalDateTime endOfValidity;
    private ArrowheadDevice providedDevice;

    public DeviceRegistryEntry()
    {
        super();
    }

    public DeviceRegistryEntry(final String macAddress, final LocalDateTime endOfValidity, final ArrowheadDevice providedDevice)
    {
        this.macAddress = macAddress;
        this.endOfValidity = endOfValidity;
        this.providedDevice = providedDevice;
    }

    public DeviceRegistryEntry(final Long id, final String macAddress, final LocalDateTime endOfValidity, final ArrowheadDevice providedDevice)
    {
        this.id = id;
        this.macAddress = macAddress;
        this.endOfValidity = endOfValidity;
        this.providedDevice = providedDevice;
    }


    public DeviceRegistryEntry(final String macAddress, final LocalDateTime endOfValidity, final String deviceName)
    {
        this.macAddress = macAddress;
        this.endOfValidity = endOfValidity;
        this.providedDevice = new ArrowheadDevice(deviceName);
    }

    public DeviceRegistryEntry(final Long id, final String macAddress, final LocalDateTime endOfValidity, final String deviceName)
    {
        this.id = id;
        this.macAddress = macAddress;
        this.endOfValidity = endOfValidity;
        this.providedDevice = new ArrowheadDevice(deviceName);
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

    public ArrowheadDevice getProvidedDevice()
    {
        return providedDevice;
    }

    public void setProvidedDevice(final ArrowheadDevice providedDevice)
    {
        this.providedDevice = providedDevice;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("DeviceRegistryEntry[");
        sb.append("id=").append(id);
        sb.append(", macAddress='").append(macAddress).append('\'');
        sb.append(", endOfValidity=").append(endOfValidity);
        sb.append(", device=").append(providedDevice);
        sb.append(']');
        return sb.toString();
    }
}
