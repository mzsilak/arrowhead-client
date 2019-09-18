import eu.arrowhead.client.OnboardingClient;
import eu.arrowhead.client.misc.ProtocolConfiguration;

import java.net.UnknownHostException;

public class DemoClient
{
    public static void main(String[] args) throws UnknownHostException
    {
        final OnboardingClient client = OnboardingClient.withProtocol(ProtocolConfiguration.HTTPS)
                                                        .withOnboardingAddress("localhost")
                                                        .withRetries(3)
                                                        .build();


        client.plain();
    }
}
