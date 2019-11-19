package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.services.EventHandler;
import eu.arrowhead.onboarding.impl.SSLContextBuilder;

import java.net.URI;

public class EventHandlerImpl extends ServiceClientImpl implements EventHandler
{
    public EventHandlerImpl(final ArrowheadClient client, final URI uri, final Transport transport, final SSLContextBuilder sslContextBuilder)
    {
        super(client, uri, transport, sslContextBuilder);
    }
}
