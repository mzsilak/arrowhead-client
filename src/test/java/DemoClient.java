import eu.arrowhead.client.misc.ProtocolConfiguration;
import eu.arrowhead.client.misc.TransportException;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;
import eu.arrowhead.client.services.request.OnboardingRequest;
import eu.arrowhead.client.services.request.SystemRegistryEntry;
import eu.arrowhead.onboarding.OnboardingClient;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;

import java.net.UnknownHostException;
import java.time.LocalDateTime;

public class DemoClient
{
    public static void main(String[] args) throws Exception
    {
        final OnboardingClient client = OnboardingClient.withProtocol(ProtocolConfiguration.HTTP)
                                                        .withOnboardingAddress("localhost")
                                                        .withRetries(3)
                                                        .build();


        try
        {
            final DeviceRegistryOnboarding deviceRegistryOnboarding = client.plain(new OnboardingRequest("client"));
            final SystemRegistryOnboarding systemRegistryOnboarding = deviceRegistryOnboarding
                    .registerSystem(new DeviceRegistryEntry("00:00:00:00:00:00", LocalDateTime.MAX, "device"));
            systemRegistryOnboarding.registerSystem(new SystemRegistryEntry());
        }
        catch (TransportException e)
        {
            e.printStackTrace();
        }
    }
}
