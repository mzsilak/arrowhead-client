package eu.arrowhead.client.spi;

import eu.arrowhead.client.ArrowheadClient;

public class ArrowheadClientBuilder
{


    public ArrowheadClient build()
    {
        return new ArrowheadClientImpl();
    }
}
