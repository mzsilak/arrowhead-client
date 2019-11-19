package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.services.Orchestrator;
import eu.arrowhead.onboarding.impl.SSLContextBuilder;

import java.net.URI;

public class OrchestratorImpl extends ServiceClientImpl implements Orchestrator
{
    public static final String SERVICE_SUFFIX = "orchestrator";

    public OrchestratorImpl(final ArrowheadClient client, final URI uri, final Transport transport, final SSLContextBuilder sslContextBuilder)
    {
        super(client, uri, transport, sslContextBuilder);
    }
}
