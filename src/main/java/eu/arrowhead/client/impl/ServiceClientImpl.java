package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.services.ArrowheadClientFacet;
import eu.arrowhead.client.utils.UriUtils;
import eu.arrowhead.onboarding.impl.SSLContextBuilder;

import java.net.URI;

public class ServiceClientImpl implements ArrowheadClientFacet
{
    protected final ArrowheadClient client;
    protected final UriUtils uriUtils;
    protected final Transport transport;
    protected final SSLContextBuilder sslContextBuilder;

    public ServiceClientImpl(final ArrowheadClient client, final URI uri, final Transport transport, final SSLContextBuilder sslContextBuilder)
    {

        this.client = client;
        this.uriUtils = new UriUtils(uri);
        this.transport = transport;
        this.sslContextBuilder = sslContextBuilder;
    }

    @Override
    public ArrowheadClient getClient()
    {
        return client;
    }
}
