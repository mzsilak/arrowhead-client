package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.transport.ProtocolConfiguration;
import eu.arrowhead.client.utils.security.SSLContextConfigurator;
import eu.arrowhead.onboarding.OnboardingClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OnboardingClientBuilder extends SSLContextBuilder<OnboardingClientBuilder>
{
    private final Logger logger = LogManager.getLogger();

    private String address = "localhost";
    private int retries = 3;
    private long delayBetweenRetries = 5;
    private TimeUnit timeUnitForRetries = TimeUnit.SECONDS;


    public OnboardingClientBuilder(final ProtocolConfiguration protocol)
    {
        super(protocol);
        logger.info("Creating new {} with protocol {}", getClass().getSimpleName(), protocol);
    }

    InetAddress getAddress()
    {
        try
        {
            return InetAddress.getByName(address);
        }
        catch (UnknownHostException e)
        {
            throw new RuntimeException(e);
        }
    }

    int getRetries()
    {
        return retries;
    }

    long getDelayBetweenRetries()
    {
        return delayBetweenRetries;
    }

    TimeUnit getTimeUnitForRetries()
    {
        return timeUnitForRetries;
    }


    public OnboardingClientBuilder withOnboardingAddress(final String address)
    {
        this.address = address;
        return this;
    }

    public OnboardingClientBuilder withDelayBetweenRetries(final long number, final TimeUnit unit)
    {
        this.delayBetweenRetries = number;
        this.timeUnitForRetries = unit;
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

    public OnboardingClientBuilder withInsecureSSLContext()
    {
        try
        {
            if (Objects.isNull(sslContext))
            {
                prepareSslContext();
                logger.warn("Using builtin accept-all, trust-all SSLContext for Onboarding");
                super.sslContext = configurator.createTrustAllSSLContext(true);

            }
            else
            {
                logger.debug("Using given {}: {}", SSLContext.class.getSimpleName(), sslContext);
            }

            HttpsURLConnection.setDefaultHostnameVerifier(SSLContextConfigurator.NoopHostnameVerifier.INSTANCE);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }

        return this;
    }

    public OnboardingClient build()
    {
        return new OnboardingClientImpl(protocol, getAddress(), this);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("OnboardingClientBuilder [");
        sb.append("address='").append(address).append('\'');
        sb.append(", retries=").append(retries);
        sb.append(", delayBetweenRetries=").append(delayBetweenRetries);
        sb.append(", timeUnitForRetries=").append(timeUnitForRetries);
        sb.append(']');
        return sb.toString();
    }
}
