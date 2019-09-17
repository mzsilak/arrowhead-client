package eu.arrowhead.client.services;


import javax.imageio.spi.ServiceRegistry;

public interface SystemRegistry extends ArrowheadClientFacet
{
    ServiceRegistry registerSystem();
    DeviceRegistry removeSystem();

}
