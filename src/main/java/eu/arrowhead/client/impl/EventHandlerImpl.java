package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.services.EventHandler;

import java.net.URI;

public class EventHandlerImpl extends ServiceClientImpl implements EventHandler
{
    public EventHandlerImpl(final ArrowheadClient client, final URI uri, final Transport transport)
    {
        super(client, uri, transport);
    }
}
