/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dns;

import java.io.IOException;
import java.net.InetAddress;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

/**
 *
 * @author gerto
 */
public class ServiceDiscovery {

    /**
     * Method that registers the gRPC services with jmDNS
     * @param serviceName the name of the service being registered
     * @param port the port on which the service is on
     */
    public static void registerService(String serviceName, int port) {
        try {
            // Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            // create the service
            ServiceInfo serviceInfo = ServiceInfo.
                    create("_grpc._tcp.local.", serviceName, port, serviceName + " gRPC service");

            // Register the service.
            jmdns.registerService(serviceInfo);

            System.out.
                    println("jmDNS: Service " + serviceName + " registered on port " + port);

            // Add a shutdown hook to unregister the service gracefully.
            Runtime.getRuntime().
                    addShutdownHook(new Thread(() -> {
                        System.out.
                                println("jmDNS: Unregistering all services...");
                        jmdns.unregisterAllServices();
                    }));

        } catch (IOException e) {
            System.err.println("jmDNS: Error registering service.");
        }
    }
}
