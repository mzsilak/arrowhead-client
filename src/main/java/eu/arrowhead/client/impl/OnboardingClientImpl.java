package eu.arrowhead.client.impl;

import eu.arrowhead.client.OnboardingClient;
import eu.arrowhead.client.misc.Protocols;
import eu.arrowhead.client.misc.Transport;
import eu.arrowhead.client.services.DeviceRegistry;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.OnboardingWithCertificateRequest;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

import javax.net.ssl.SSLContext;
import javax.swing.text.PlainDocument;
import java.net.InetAddress;
import java.util.Objects;

public class OnboardingClientImpl extends ClientImpl implements OnboardingClient
{
    private final SSLContext sslContext;
    private final Protocols protocol;
    private final InetAddress address;
    private final Transport transport;


    public OnboardingClientImpl(final Protocols protocol,
                                final InetAddress address,
                                final int retries)
    {
        this(protocol, address, retries, null);
    }

    public OnboardingClientImpl(final Protocols protocol,
                                final InetAddress address,
                                final int retries,
                                final SSLContext sslContext)
    {
        if (protocol.isSecure())
        { this.sslContext = Objects.requireNonNull(sslContext); }
        else
        { this.sslContext = null; }
        this.protocol = Objects.requireNonNull(protocol);
        this.address = Objects.requireNonNull(address);
        transport = protocol.getTransport();
        transport.setMaxRetries(retries);
    }

    @Override
    public DeviceRegistry plain(final OnboardingRequest request)
    {
        return null;
    }

    @Override
    public DeviceRegistry withSharedKey(final OnboardingWithSharedKeyRequest request)
    {
        return null;
    }

    @Override
    public DeviceRegistry withCertificate(final OnboardingWithCertificateRequest request)
    {
        return null;
    }
}
