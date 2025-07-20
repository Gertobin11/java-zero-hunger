/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.Logistics;

import dns.ServiceRegistry;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

/**
 *
 * @author gerto
 */
public class LogisticsServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        // create the logistics instance
        Logistics mainLogistics = new Logistics();

        // create te server put 0 to get dynamically alotted port
        Server server = ServerBuilder.forPort(0).
                addService(mainLogistics).
                build().
                start();

        System.out.
                println("Main Logistics Service started, listening on " + server.
                        getPort());

        ServiceRegistry.registerService("logistics", server.getPort());

        server.awaitTermination();
    }
}
