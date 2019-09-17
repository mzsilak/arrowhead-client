package eu.arrowhead.client.impl;

import eu.arrowhead.client.OnboardingClient;
import eu.arrowhead.client.misc.Protocols;
import eu.arrowhead.client.utils.SSLContextConfigurator;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.net.InetAddress;
import java.util.Objects;

public class OnboardingClientBuilder
{

    private final Protocols protocol;
    private String address = "localhost";
    private int retries = 3;
    private SSLContext context;
    private SSLContextConfigurator configurator;
    private TrustManagerFactory trustManagerFactory;
    private KeyManagerFactory keyManagerFactory;

    public OnboardingClientBuilder(final Protocols protocol)
    {
        this.protocol = protocol;
    }

    public OnboardingClientBuilder withOnboardingAddress(final String address)
    {
        this.address = address;
        return this;
    }

    public OnboardingClientBuilder withRetries(final int retries)
    {
        this.retries = retries;
        return this;
    }

    public OnboardingClientBuilder withMaximumRetries()
    {
        this.retries = Integer.MAX_VALUE;
        return this;
    }

    public OnboardingClientBuilder withSSLContext(final SSLContext context)
    {
        this.context = context;
        return this;
    }

    public OnboardingClientBuilder withSSLConfiguration(final SSLContextConfigurator configurator)
    {
        this.configurator = configurator;
        return this;
    }

    public OnboardingClientBuilder withSSLFactories(final TrustManagerFactory trustManagerFactory,
                                                    final KeyManagerFactory keyManagerFactory)
    {
        this.trustManagerFactory = trustManagerFactory;
        this.keyManagerFactory = keyManagerFactory;
        return this;
    }

    public OnboardingClient build()
    {
        try
        {
            final OnboardingClient onboardingClient;
            final InetAddress inetAddress = InetAddress.getByName(address);

            if (protocol.isSecure())
            {
                final SSLContext sslContext;
                if (Objects.nonNull(context))
                {
                    sslContext = context;
                }
                else if (Objects.nonNull(configurator))
                {
                    sslContext = configurator.createSSLContext(true);
                }
                else if (Objects.nonNull(keyManagerFactory) || Objects.nonNull(trustManagerFactory))
                {

                    sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(keyManagerFactory != null ? keyManagerFactory.getKeyManagers() : null,
                                    trustManagerFactory != null ? trustManagerFactory.getTrustManagers() : null, null);
                }
                else
                {
                    configurator = new SSLContextConfigurator(true);
                    sslContext = configurator.createSSLContext(true);
                }

                onboardingClient = new OnboardingClientImpl(protocol, inetAddress, retries, sslContext);
            }
            else
            {
                onboardingClient = new OnboardingClientImpl(protocol, inetAddress, retries);
            }

            return onboardingClient;
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }
}
