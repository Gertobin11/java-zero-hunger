/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.FoodSource;

import dns.ServiceRegistry;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

/**
 *
 * @author gerto
 */
public class FoodSourceServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        // create the food source instance
        FoodSource mainFoodSource = new FoodSource();

        // create te server put 0 to get dynamically alotted port
        Server server = ServerBuilder.forPort(0).
                addService(mainFoodSource).
                build().
                start();

        System.out.println("Main Food Source started, listening on " + server.
                getPort());

        ServiceRegistry.registerService("food-source-service", server.getPort());

        server.awaitTermination();
    }
}
