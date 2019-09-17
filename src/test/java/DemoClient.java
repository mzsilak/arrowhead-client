import eu.arrowhead.client.OnboardingClient;
import eu.arrowhead.client.misc.Protocols;

import java.net.UnknownHostException;

public class DemoClient
{
    public static void main(String[] args) throws UnknownHostException
    {
        final OnboardingClient client = OnboardingClient.withProtocol(Protocols.HTTPS)
                                                        .withOnboardingAddress("localhost")
                                                        .withRetries(3)
                                                        .build();


        client.plain();
    }
}
