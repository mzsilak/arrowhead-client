package eu.arrowhead.client.misc;

import eu.arrowhead.client.transport.ProtocolConfiguration;

public enum ServiceInterfaces
{
    JSON;

    public String forProtocol(final ProtocolConfiguration protocolConfiguration)
    {
        switch (protocolConfiguration.getScheme())
        {
            case "http":
                return "HTTP-INSECURE-" + name();
            case "https":
                return "HTTP-SECURE-" + name();
            default:
                return null;
        }
    }
}
