/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.google.protobuf.Empty;
import dns.ServiceRegistry;
import grpc.common.FoodRequest;
import grpc.common.SavedFoodRequest;
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

        // create te server put 0 to get dynamically alotted port
        Server server = ServerBuilder.forPort(0).
                addService(smartHub).
                build().
                start();

        System.out.println("SmartHub server started, listening on " + server.getPort());
         
        ServiceRegistry.registerService("smart-hub-service", server.getPort());

        server.awaitTermination();
    }

    @Override
    public void handleFoodRequests(FoodRequest request, StreamObserver<StatusResponse> responseObserver) {
        System.out.println("Recieved request for food items");
        // create a saved request from a client request 
        SavedFoodRequest savedFoodRequest = SavedFoodRequest.newBuilder().
                setFoodRequest(request).
                setDeliveryId(0).
                setPickupTime("Not available").
                setStatus("Pending").
                setRequestId(foodRequests.size() + 1).
                build();
        // add the saved food request to the array of saved requests
        foodRequests.add(savedFoodRequest);
        // create a status response based off the initial saved food request
        StatusResponse status = StatusResponse.newBuilder().
                setStatus(savedFoodRequest.getStatus()).
                setPickupTime(savedFoodRequest.getPickupTime()).
                setDeliveryId(savedFoodRequest.getDeliveryId()).
                build();

        System.out.
                println("Request added to list for delivery when items are available");
        // return our response
        responseObserver.onNext(status);

        responseObserver.onCompleted();

    }

    /**
     * Function that checks the status of the saved food request with the
     * request id of the passed request id and returns a status response
     *
     * @param request the request id
     * @param responseObserver the response we return to the client
     */
    @Override
    public void statusUpdate(StatusRequest request, StreamObserver<StatusResponse> responseObserver) {

        SavedFoodRequest matchedRequest = null;
        // loop over all the saved food request items
        for (SavedFoodRequest foodRequest : foodRequests) {
            // check if the request_ids match
            if (foodRequest.getRequestId() == request.getRequestId()) {
                matchedRequest = foodRequest;
                break;
            }
        }

        if (matchedRequest != null) {
            // create a response from the match saved food request item
            StatusResponse response = StatusResponse.newBuilder().
                    setDeliveryId(matchedRequest.getDeliveryId()).
                    setPickupTime(matchedRequest.getPickupTime()).
                    setStatus(matchedRequest.getStatus()).
                    build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            // if it is not found we return a not found error
            responseObserver.onError(NOT_FOUND.
                    withDescription("Request with an ID of " + request.
                            getRequestId() + " not found").
                    asException());
        }

    }

    /**
     * Method that returns the status of all current food requests
     *
     * @param request empty request
     * @param responseObserver the response object sent to the client
     */
    @Override
    public void getAllStatus(Empty request, StreamObserver<StatusResponse> responseObserver) {

        for (SavedFoodRequest savedFoodRequest : foodRequests) {
            // build a status response message for every saved response
            StatusResponse response = StatusResponse.newBuilder().
                    setDeliveryId(savedFoodRequest.getDeliveryId()).
                    setPickupTime(savedFoodRequest.getPickupTime()).
                    setStatus(savedFoodRequest.getStatus()).
                    build();

            responseObserver.onNext(response);
            // introduce a sleep to mimic database look up
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().
                        interrupt();
            }
        }
        // when the loop finishes we inform the client the method is finished
        responseObserver.onCompleted();
    }

}
