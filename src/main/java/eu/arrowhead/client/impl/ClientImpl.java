package eu.arrowhead.client.impl;

import eu.arrowhead.client.misc.ProtocolConfiguration;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetAddress;
import java.net.URI;
import java.util.Objects;

abstract class ClientImpl
{
    private final UriComponentsBuilder baseBuilder;

    protected ClientImpl(final ProtocolConfiguration protocols, final InetAddress inetAddress, int port, final String... path)
    {
        final String baseUri = String.format("%s://%s:%d", protocols.getScheme(), inetAddress.getHostAddress(), port);
        baseBuilder = UriComponentsBuilder.fromHttpUrl(baseUri).pathSegment(path);
    }

    protected ClientImpl(final URI uri)
    {
        baseBuilder = UriComponentsBuilder.fromUri(uri);
    }


    URI build(final String... path)
    {
        return baseBuilder.cloneBuilder()
                          .pathSegment(path)
                          .build(true)
                          .toUri();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final ClientImpl client = (ClientImpl) o;
        return baseBuilder.toUriString().equals(((ClientImpl) o).baseBuilder.toUriString());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(baseBuilder);
    }
}
