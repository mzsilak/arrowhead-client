package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.transport.Transport;
import eu.arrowhead.client.services.Orchestrator;

import java.net.URI;

public class OrchestratorImpl extends ServiceClientImpl implements Orchestrator
{
    public static final String SERVICE_SUFFIX = "orchestrator";

    public OrchestratorImpl(final ArrowheadClient client, final URI orchestratorUri, final Transport transport)
    {
        super(client, orchestratorUri, transport);
    }
}
