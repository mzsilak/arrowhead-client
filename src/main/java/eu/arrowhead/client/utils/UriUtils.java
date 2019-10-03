package eu.arrowhead.client.utils;

import eu.arrowhead.client.transport.ProtocolConfiguration;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Objects;

public class UriUtils
{
    private final UriComponentsBuilder baseBuilder;

    public UriUtils(final ProtocolConfiguration protocols, final InetAddress inetAddress, int port, final String... path)
    {
        final String baseUri = String.format("%s://%s:%d", protocols.getScheme(), inetAddress.getHostAddress(), port);
        baseBuilder = UriComponentsBuilder.fromHttpUrl(baseUri);
        safeAddPaths(baseBuilder, path);
    }

    private void safeAddPaths(final UriComponentsBuilder builder, final String... paths)
    {
        for (String path : paths)
        {
            final String p = path.replaceAll("%20", "/");
            builder.pathSegment(p.split("/"));
        }
    }

    public UriUtils(final URI uri)
    {
        baseBuilder = UriComponentsBuilder.fromUri(uri);
    }

    public UriUtils(final ProtocolConfiguration protocol, final String address, final Integer port, final String serviceURI) throws UnknownHostException
    {
        this(protocol, InetAddress.getByName(address), port, serviceURI);
    }

    public URI copyBuild(final String... path)
    {
        final UriComponentsBuilder builder = baseBuilder.cloneBuilder();
        safeAddPaths(builder, path);
        return builder.build(true).toUri();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("[");
        sb.append(baseBuilder.toUriString());
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final UriUtils client = (UriUtils) o;
        return baseBuilder.toUriString().equals(client.baseBuilder.toUriString());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(baseBuilder);
    }
}
