/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dns;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

/**
 *
 * @author gerto
 */
public class ServiceDiscovery {
    // A map to hold the discovered services

    private Map<String, ServiceInfo> discoveredServices = new HashMap<>();

    private class DiscoveryListener implements ServiceListener {

        @Override
        public void serviceAdded(ServiceEvent event) {
            event.getDNS().
                    requestServiceInfo(event.getType(), event.getName(), 1);
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            System.out.println("CLIENT: Service removed: " + event.getName());
            discoveredServices.remove(event.getName());
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            System.out.println("Service found! Details: " + event.getInfo());
            discoveredServices.put(event.getName(), event.getInfo());
        }
    }

    public void start() throws IOException {
        JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
        // Add a listener for the general gRPC service type.
        jmdns.addServiceListener("_grpc._tcp.local.", new DiscoveryListener());
        System.out.println("Service discovery started.");
    }

    /**
     * Gets the connection details for the passed service name
     *
     * @param serviceName The name of the service
     * @return The Service if found else null
     */
    public ServiceInfo findService(String serviceName) {
        return discoveredServices.get(serviceName);
    }
}
