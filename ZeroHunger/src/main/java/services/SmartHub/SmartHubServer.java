/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.SmartHub;

import dns.ServiceDiscovery;
import dns.ServiceRegistry;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

/**
 *
 * @author gerto
 */
public class SmartHubServer {
        public static void main(String[] args) throws IOException, InterruptedException {
        // Create an instance of service discovery to be able to use it in the Smart hub 
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
        
        // create the smart hub instance
        SmartHub smartHub = new SmartHub(serviceDiscovery);

        // create te server put 0 to get dynamically alotted port
        Server server = ServerBuilder.forPort(0).
                addService(smartHub).
                build().
                start();

        System.out.println("SmartHub server started, listening on " + server.getPort());
         
        ServiceRegistry.registerService("smart-hub-service", server.getPort());

        server.awaitTermination();
    }
}
