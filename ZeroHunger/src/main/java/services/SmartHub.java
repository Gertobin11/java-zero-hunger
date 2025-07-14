/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import grpc.smart_hub.FoodRequest;
import grpc.smart_hub.SmartHubServiceGrpc.SmartHubServiceImplBase;
import grpc.smart_hub.Status;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gerto
 */
public class SmartHub extends SmartHubServiceImplBase {

    List<FoodRequest> foodRequests = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        // create the smart hub instance
        SmartHub smartHub = new SmartHub();

        // listen on a unique (in this distibuted system) port
        int port = 50052;

        // create te server
        Server server = ServerBuilder.forPort(port)
                .addService(smartHub)
                .build()
                .start();

        System.out.println("SmartHub server started, listening on " + port);

        server.awaitTermination();
    }

    @Override
    public void handleFoodRequests(FoodRequest request, StreamObserver<Status> responseObserver) {
        System.out.println("Recieved request for food items");
        // add the requat to an array of currently requested items
        foodRequests.add(request);
        // create a default status message for initial food request
        Status status = Status.newBuilder().setStatus("pending").setPickupTime("not set").setRequestId(request.getRequestId()).setDeliveryId(0).build();

        System.out.println("Request added to list for delivery when items are available");
        // return our response
        responseObserver.onNext(status);

        responseObserver.onCompleted();

    }

}
