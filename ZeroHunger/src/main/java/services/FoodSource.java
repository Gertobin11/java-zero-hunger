/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import grpc.common.Address;
import grpc.common.FoodItem;
import grpc.food_source.FoodSourceServiceGrpc.FoodSourceServiceImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;

/**
 *
 * @author gerto
 */
public class FoodSource extends FoodSourceServiceImplBase {

    public static void main(String[] args) throws InterruptedException, IOException {
        FoodSource mainFoodSource = new FoodSource();

        int port = 50051;

        Server server = ServerBuilder.forPort(port)
                .addService(mainFoodSource)
                .build()
                .start();

        System.out.println("Main Food Source started, listening on " + port);

        server.awaitTermination();
    }
    
    @Override
	public void streamAvailableFoodItems(Address address, StreamObserver<FoodItem> responseObserver) {
        
        }
}
