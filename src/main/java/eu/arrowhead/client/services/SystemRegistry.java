package eu.arrowhead.client.services;


import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.request.SystemRegistryEntry;

public interface SystemRegistry extends ArrowheadClientFacet
{
    SystemRegistryEntry registerSystem(final SystemRegistryEntry request) throws TransportException;

    SystemRegistryEntry removeSystem(final SystemRegistryEntry request) throws TransportException;

}
