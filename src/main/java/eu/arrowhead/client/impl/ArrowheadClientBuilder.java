package eu.arrowhead.client.impl;

import eu.arrowhead.client.ArrowheadClient;

public class ArrowheadClientBuilder
{


    public ArrowheadClient build()
    {
        return new ArrowheadClientImpl();
    }
}
