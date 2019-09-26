package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.services.ArrowheadClientFacet;
import eu.arrowhead.client.utils.UriUtils;

import java.net.URI;

public class ServiceClientImpl implements ArrowheadClientFacet
{
    protected final ArrowheadClient client;
    protected final UriUtils uriUtils;
    protected final Transport transport;

    public ServiceClientImpl(final ArrowheadClient client, final URI uri, final Transport transport)
    {

        this.client = client;
        this.uriUtils = new UriUtils(uri);
        this.transport = transport;
    }

    @Override
    public ArrowheadClient getClient()
    {
        return client;
    }
}
