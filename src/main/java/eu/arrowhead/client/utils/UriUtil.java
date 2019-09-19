package eu.arrowhead.client.utils;

import eu.arrowhead.client.misc.ProtocolConfiguration;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Objects;

public class UriUtil
{
    private final UriComponentsBuilder baseBuilder;

    public UriUtil(final ProtocolConfiguration protocols, final InetAddress inetAddress, int port, final String... path)
    {
        final String baseUri = String.format("%s://%s:%d", protocols.getScheme(), inetAddress.getHostAddress(), port);
        baseBuilder = UriComponentsBuilder.fromHttpUrl(baseUri).pathSegment(path);
    }

    public UriUtil(final URI uri)
    {
        baseBuilder = UriComponentsBuilder.fromUri(uri);
    }

    public UriUtil(final ProtocolConfiguration protocol, final String address, final Integer port, final String serviceURI) throws UnknownHostException
    {
        this(protocol, InetAddress.getByName(address), port, serviceURI);
    }

    public URI copyBuild(final String... path)
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
        final UriUtil client = (UriUtil) o;
        return baseBuilder.toUriString().equals(client.baseBuilder.toUriString());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(baseBuilder);
    }
}
