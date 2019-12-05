package eu.arrowhead.onboarding;

import eu.arrowhead.client.ArrowheadClient;
import eu.arrowhead.client.services.model.ArrowheadService;
import eu.arrowhead.client.services.model.ArrowheadSystem;
import eu.arrowhead.client.services.request.DeviceRegistryEntry;
import eu.arrowhead.client.services.request.OnboardingWithSharedKeyRequest;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.services.request.SystemRegistryEntry;
import eu.arrowhead.client.transport.SSLConfigurationException;
import eu.arrowhead.client.transport.TransportException;
import eu.arrowhead.onboarding.services.DeviceRegistryOnboarding;
import eu.arrowhead.onboarding.services.ServiceRegistryOnboarding;
import eu.arrowhead.onboarding.services.ServiceWrapper;
import eu.arrowhead.onboarding.services.SystemRegistryOnboarding;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public abstract class OnboardingService implements Supplier<ArrowheadClient>, ServiceWrapper<ArrowheadClient>
{
    private final Long defaultValidity;
    private final TemporalUnit defaultValidityUnit;

    protected final OnboardingClient onboardingClient;
    protected final AtomicReference<ArrowheadClient> arrowheadClient;
    protected final AtomicReference<ServiceRegistryEntry> serviceRegistryEntry;
    protected final AtomicReference<SystemRegistryEntry> systemRegistryEntry;
    protected final AtomicReference<DeviceRegistryEntry> deviceRegistryEntry;

    public OnboardingService(final OnboardingClient onboardingClient, final Long defaultValidity, final TemporalUnit defaultValidityUnit)
    {
        this.defaultValidity = defaultValidity;
        this.defaultValidityUnit = defaultValidityUnit;

        this.onboardingClient = onboardingClient;
        this.arrowheadClient = new AtomicReference<>();
        this.serviceRegistryEntry = new AtomicReference<>();
        this.systemRegistryEntry = new AtomicReference<>();
        this.deviceRegistryEntry = new AtomicReference<>();
    }

    @Override
    public ArrowheadClient get()
    {
        ArrowheadClient client;
        while (Objects.isNull(client = arrowheadClient.get()))
        {
            sleep(100L);
        }

        return client;
    }

    protected void sleep(final long milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e)
        {
            // nothing
        }
    }

    protected String getOnboardingName()
    {
        return getSystemName();
    }

    protected abstract String getSharedKey();

    protected String getDeviceName()
    {
        return getOnboardingName() + "-device";
    }

    protected String getMacAddress()
    {
        return "";
    }

    protected abstract String getSystemName();

    protected abstract String getSystemAddress();

    protected abstract int getSystemPort();

    protected String getServiceName()
    {
        return getOnboardingName() + "-service";
    }

    protected abstract String[] getServiceInterfaces();

    protected abstract String getServiceUri();

    protected DeviceRegistryOnboarding onboardingWithSharedKey(final String name, final String sharedKey) throws SSLConfigurationException, TransportException
    {
        return onboardingClient.withSharedKey(new OnboardingWithSharedKeyRequest(name, sharedKey));
    }

    protected SystemRegistryOnboarding registerDevice(final DeviceRegistryOnboarding deviceRegistryOnboarding) throws TransportException
    {
        return deviceRegistryOnboarding.registerDevice(deviceRegistryEntry.get());
    }

    protected ServiceRegistryOnboarding registerSystem(final SystemRegistryOnboarding systemRegistryOnboarding) throws TransportException
    {
        return systemRegistryOnboarding.registerSystem(systemRegistryEntry.get());
    }

    protected ArrowheadClient registerService(final ServiceRegistryOnboarding serviceRegistryOnboarding) throws TransportException
    {
        return serviceRegistryOnboarding.registerService(serviceRegistryEntry.get());
    }

    @Override
    public void start() throws Exception
    {
        synchronized (arrowheadClient)
        {
            if (Objects.nonNull(arrowheadClient.get()))
            {
                return;
            }

            final DeviceRegistryOnboarding deviceRegistryOnboarding = onboardingWithSharedKey(getSystemName(), getSharedKey());

            deviceRegistryEntry.set(deviceRegistryEntry());
            final SystemRegistryOnboarding systemRegistryOnboarding = registerDevice(deviceRegistryOnboarding);

            systemRegistryEntry.set(systemRegistryEntry(deviceRegistryEntry.get()));
            final ServiceRegistryOnboarding serviceRegistryOnboarding = registerSystem(systemRegistryOnboarding);

            serviceRegistryEntry.set(serviceRegistryEntry(systemRegistryEntry.get()));
            final ArrowheadClient client = registerService(serviceRegistryOnboarding);

            arrowheadClient.set(client);
        }
    }

    @Override
    public void stop() throws Exception
    {
        synchronized (arrowheadClient)
        {
            final ArrowheadClient client = arrowheadClient.getAndSet(null);
            if (Objects.isNull(client))
            {
                return;
            }

            final ServiceRegistryOnboarding serviceRegistryOnboarding = client.serviceOffboarding();
            final SystemRegistryOnboarding systemRegistryOnboarding = serviceRegistryOnboarding.removeService(serviceRegistryEntry.getAndSet(null));
            final DeviceRegistryOnboarding deviceRegistryOnboarding = systemRegistryOnboarding.removeSystem(systemRegistryEntry.getAndSet(null));
            deviceRegistryOnboarding.removeDevice(deviceRegistryEntry.getAndSet(null));
        }
    }

    protected DeviceRegistryEntry deviceRegistryEntry()
    {
        return new DeviceRegistryEntry(getMacAddress(),
                                       LocalDateTime.now().plus(defaultValidity, defaultValidityUnit),
                                       getDeviceName());
    }

    protected SystemRegistryEntry systemRegistryEntry(final DeviceRegistryEntry entry)
    {
        final ArrowheadSystem system = new ArrowheadSystem(getSystemName(), getSystemAddress(), getSystemPort());
        return new SystemRegistryEntry(entry.getProvidedDevice(),
                                       system,
                                       getServiceUri(),
                                       entry.getEndOfValidity());
    }

    protected ServiceRegistryEntry serviceRegistryEntry(final SystemRegistryEntry entry)
    {
        final ArrowheadService service = new ArrowheadService(getServiceName(), getServiceInterfaces());
        return new ServiceRegistryEntry(entry.getProvidedSystem(),
                                        service,
                                        getServiceUri());
    }
}
