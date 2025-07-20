package common;


import dns.ServiceDiscovery;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.jmdns.ServiceInfo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author gerto
 */
public class Common {
    /**
     * Generic method to retrieve a channel
     *
     * @param service the service that is requested
     * @return a managed channel for the requested service
     */
    public static ManagedChannel getChannel(String service, ServiceDiscovery serviceDiscovery) {
        ServiceInfo serviceInfo = null;
        int retries = 3;
        while (retries > 0) {
            // call the find service method to get the service 
            serviceInfo = serviceDiscovery.findService(service);
            try {
                Thread.sleep(3000); // wait for 3 seconds before retrying
            } catch (InterruptedException ex) {
                Thread.currentThread().
                        interrupt();
            }
            retries--;
        }

        // if service is not found return null
        if (serviceInfo == null) {
            return null;
        }

        // get the host and port from the service info
        String host = serviceInfo.getInet4Addresses()[0].getHostAddress();
        int port = serviceInfo.getPort();

        System.out.println(service + " found at " + host + ":" + port);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).
                usePlaintext().
                build();

        return channel;
    }
}
