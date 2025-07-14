/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import grpc.smart_hub.FoodRequest;
import grpc.smart_hub.SavedFoodRequest;
import grpc.smart_hub.SmartHubServiceGrpc.SmartHubServiceImplBase;
import grpc.smart_hub.StatusRequest;
import grpc.smart_hub.StatusResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import static io.grpc.Status.NOT_FOUND;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gerto
 */
public class SmartHub extends SmartHubServiceImplBase {

    List<SavedFoodRequest> foodRequests = new ArrayList<>();

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
    public void handleFoodRequests(FoodRequest request, StreamObserver<StatusResponse> responseObserver) {
        System.out.println("Recieved request for food items");
        // create a saved request from a client request 
        SavedFoodRequest savedFoodRequest = SavedFoodRequest.newBuilder().setFoodRequest(request).setDeliveryId(0).setPickupTime("Not available").setStatus("Pending").setRequestId(foodRequests.size() + 1).build();
        // add the saved food request to the array of saved requests
        foodRequests.add(savedFoodRequest);
        // create a status response based off the initial saved food request
        StatusResponse status = StatusResponse.newBuilder().setStatus(savedFoodRequest.getStatus()).setPickupTime(savedFoodRequest.getPickupTime()).setDeliveryId(savedFoodRequest.getDeliveryId()).build();

        System.out.println("Request added to list for delivery when items are available");
        // return our response
        responseObserver.onNext(status);

        responseObserver.onCompleted();

    }

    @Override
    public void statusUpdate(StatusRequest request, StreamObserver<StatusResponse> responseObserver) {

        SavedFoodRequest matchedRequest = null;
        for (SavedFoodRequest foodRequest : foodRequests) {
            if (foodRequest.getRequestId() == request.getRequestId()) {
                matchedRequest = foodRequest;
            }
        }

        if (matchedRequest != null) {
            StatusResponse response = StatusResponse.newBuilder().setDeliveryId(matchedRequest.getDeliveryId()).setPickupTime(matchedRequest.getPickupTime()).setStatus(matchedRequest.getStatus()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        else {
            responseObserver.onError(NOT_FOUND.withDescription("Request with an ID of " + request.getRequestId() + " not found").asException());
        }                

        
    }

}
