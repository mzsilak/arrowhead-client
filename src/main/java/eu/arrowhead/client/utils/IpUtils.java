package eu.arrowhead.client.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class IpUtils
{

    /* If needed, this method can be used to get the IPv4 address of the host machine. Public point-to-point IP
    addresses are prioritized over private
      (site local) IP addresses */
    @SuppressWarnings("unused")
    public static String getIpAddress() throws SocketException
    {
        List<InetAddress> addresses = new ArrayList<>();

        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface inf = (NetworkInterface) e.nextElement();
            Enumeration ee = inf.getInetAddresses();
            while (ee.hasMoreElements()) {
                addresses.add((InetAddress) ee.nextElement());
            }
        }

        addresses = addresses.stream().filter(current -> !current.getHostAddress().contains(":"))
                             .filter(current -> !current.isLoopbackAddress())
                             .filter(current -> !current.isMulticastAddress())
                             .filter(current -> !current.isLinkLocalAddress()).collect(Collectors.toList());
        if (addresses.isEmpty()) {
            throw new SocketException("No valid addresses left after filtering");
        }
        for (InetAddress address : addresses) {
            if (!address.isSiteLocalAddress()) {
                return address.getHostAddress();
            }
        }
        return addresses.get(0).getHostAddress();
    }
}
