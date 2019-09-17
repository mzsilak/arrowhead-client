package eu.arrowhead.client.misc;

import java.net.URI;

public interface Transport
{
    void setMaxRetries(int retries);

    <T> T get(final Class<T> clz, final URI uri) throws TransportException;

    <T> T get(final Class<T> clz, final URI uri, final Object... pathParameters) throws TransportException;

    <T, B> T post(final Class<T> clz, final URI uri, final B body) throws TransportException;

    <T, B> T post(final Class<T> clz, final URI uri, final B body, final Object... pathParameters) throws TransportException;

    <B> void put(final URI uri, final B body) throws TransportException;

    <B> void put(final URI uri, final B body, final Object... pathParameters) throws TransportException;

    void delete(final URI uri) throws TransportException;

    void delete(final URI uri, final Object... pathParameters) throws TransportException;
}
