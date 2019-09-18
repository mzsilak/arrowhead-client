package eu.arrowhead.client.misc;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public enum ServiceInterfaces
{
    JSON;

    public String forProtocol(final ProtocolConfiguration protocolConfiguration)
    {
        switch (protocolConfiguration)
        {
            case HTTP:
                return "HTTP-INSECURE-" + name();
            case HTTPS:
                return "HTTP-SECURE-" + name();
            default:
                return null;
        }
    }
}
