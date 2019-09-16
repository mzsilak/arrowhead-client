package eu.arrowhead.client.spi;

import eu.arrowhead.client.OnboardingClient;
import eu.arrowhead.client.Protocols;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.util.Objects;

public class OnboardingClientImpl implements OnboardingClient
{
    private final SSLContext sslContext;
    private final Protocols protocol;
    private final InetAddress address;
    private final int retries;

    public OnboardingClientImpl(final Protocols protocol, final InetAddress address, final int retries)
    {
        this(null, protocol, address, retries);
    }

    public OnboardingClientImpl(final SSLContext sslContext, final Protocols protocol, final InetAddress address,
                                final int retries)
    {
        if (protocol.isSecure())
        { this.sslContext = Objects.requireNonNull(sslContext); }
        else
        {
            this.sslContext = null;
        }
        this.protocol = Objects.requireNonNull(protocol);
        this.address = Objects.requireNonNull(address);
        this.retries = retries;
    }

    @Override
    public void plain()
    {

    }
}
