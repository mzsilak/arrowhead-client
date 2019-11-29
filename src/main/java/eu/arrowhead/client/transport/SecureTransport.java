package eu.arrowhead.client.transport;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

public interface SecureTransport extends Transport
{
    void setSSLContext(final SSLContext context);

    void setSSLContext(final SSLContext sslContext, final HostnameVerifier verifier);
}
