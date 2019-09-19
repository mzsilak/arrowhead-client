package eu.arrowhead.client.misc;

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
