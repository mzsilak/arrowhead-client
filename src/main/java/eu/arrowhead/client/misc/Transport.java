package eu.arrowhead.client.misc;

import eu.arrowhead.client.services.request.DeviceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryEntry;
import eu.arrowhead.client.services.request.ServiceRegistryQuery;
import eu.arrowhead.client.services.response.OnboardingResponse;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public interface Transport
{
    void setMaxRetries(int retries);

    void setDelayBetweenRetries(long delayBetweenRetries, TimeUnit timeUnitForRetries);

    <T> T get(final Class<T> clz, final URI uri) throws TransportException;

    <T> T get(final Class<T> clz, final URI uri, final Object... pathParameters) throws TransportException;

    <T, B> T post(final Class<T> clz, final URI uri, final B body) throws TransportException;

    <T, B> T post(final Class<T> clz, final URI uri, final B body, final Object... pathParameters) throws TransportException;

    <B> void put(final URI uri, final B body) throws TransportException;

    <B> void put(final URI uri, final B body, final Object... pathParameters) throws TransportException;

    <T, B> T put(final Class<T> cls, final URI uri, final B body) throws TransportException;

    <T, B> T put(final Class<T> cls, final URI uri, final B body, final Object... pathParameters) throws TransportException;

    void delete(final URI uri) throws TransportException;

    void delete(final URI uri, final Object... pathParameters) throws TransportException;
}
