package eu.arrowhead.client.services;


import eu.arrowhead.client.ArrowheadClient;

public interface ServiceRegistry extends ArrowheadClientFacet
{
    ArrowheadClient registerService();

    SystemRegistry removeService();
}
