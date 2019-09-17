package eu.arrowhead.client.impl;

import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

abstract class ClientImpl
{
    private final UriBuilder baseBuilder;

    protected ClientImpl() {
        final String baseUri = String.format("http://");
        baseBuilder = UriComponentsBuilder.fromHttpUrl(baseUri);}
}
