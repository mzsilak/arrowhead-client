package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.CoreSystems;
import eu.arrowhead.client.misc.SystemEndpointHolder;
import eu.arrowhead.client.misc.Transport;

import java.util.Objects;

public class ArrowheadClientBuilder
{
    public static ArrowheadClient createWith(final SystemEndpointHolder endpointHolder, final Transport transport)
    {
        Objects.requireNonNull(endpointHolder);
        Objects.requireNonNull(transport);
        Objects.requireNonNull(endpointHolder.get(CoreSystems.SERVICE_REGISTRY), "EndpointHolder must contain ServiceRegistry endpoint");
        return new ArrowheadClientImpl(endpointHolder, transport);
    }
}
