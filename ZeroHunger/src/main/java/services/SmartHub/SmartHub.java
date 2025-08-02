/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.SmartHub;

import com.google.protobuf.Empty;
import dns.ServiceDiscovery;
import grpc.common.Address;
import grpc.common.FoodItemQuantity;
import grpc.common.FoodRequest;
import grpc.common.SavedFoodRequest;
import grpc.food_source.FoodSourceServiceGrpc;
import grpc.food_source.FoodSourceServiceGrpc.FoodSourceServiceStub;
import grpc.food_source.Stock;
import grpc.food_source.StockResponse;
import grpc.logistics.Delivery;
import grpc.logistics.DeliveryResponse;
import grpc.logistics.LogisticsServiceGrpc;
import grpc.logistics.LogisticsServiceGrpc.LogisticsServiceBlockingStub;
import grpc.smart_hub.SmartHubServiceGrpc.SmartHubServiceImplBase;
import grpc.smart_hub.StatusRequest;
import grpc.smart_hub.StatusResponse;
import io.grpc.ManagedChannel;
import static io.grpc.Status.NOT_FOUND;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import common.Common;
import grpc.smart_hub.SavedFoodRequests;
import java.io.IOException;

/**
 *
 * @author gerto
 */
public class SmartHub extends SmartHubServiceImplBase {

    List<SavedFoodRequest> foodRequests = new ArrayList<>();
    // an instance variable to call other services 
    private final ServiceDiscovery serviceDiscovery;

    // constructor that takes in a Service Discovery instance
    public SmartHub(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public void handleFoodRequests(FoodRequest request, StreamObserver<SavedFoodRequest> responseObserver) {

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

        System.out.
                println("Request added to list for delivery when items are available");
        // return our response
        responseObserver.onNext(savedFoodRequest);

        responseObserver.onCompleted();

    }
    
    /**
     * Function that returns all the saved food requests stored on the server
     * @param request empty request object
     * @param responseObserver used to send the saved requests to the client
     */
    @Override
    public void getCurrentRequests(Empty request, StreamObserver<SavedFoodRequests> responseObserver) {
        SavedFoodRequests currentRequests = SavedFoodRequests.newBuilder().addAllItems(foodRequests).build();
        // return all the saved requests
        responseObserver.onNext(currentRequests);

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

    @Override
    public void triggerChecks(Empty request, StreamObserver<Empty> responseObserver) {
        // declare channels here so can be checked and shutdown
        ManagedChannel logisticsChannel = null;
        ManagedChannel foodSourceChannel = null;
        try {
            // look for needed services
            serviceDiscovery.start();
            StockResponse stockResponse = null;
            System.out.println("Smart Hub Service initiating action checks....");

            // return if no current food requests
            if (foodRequests.size() < 1) {
                System.out.println("No requests to check");
                return;
            }
            System.out.println("Attempting to contact Food Source");

            // call helper method below to get the Food source channel
            foodSourceChannel = Common.
                    getChannel("food-source-service", serviceDiscovery);
            if (foodSourceChannel == null) {
                responseObserver.onError(NOT_FOUND.
                        withDescription("Unable to find Food Source Service").
                        asException());
                return;
            }
            FoodSourceServiceStub foodSource;
            foodSource = FoodSourceServiceGrpc.newStub(foodSourceChannel);

            try {
                // call helper method below to make calls to the food source
                stockResponse = checkIfFoodRequestsInStock(foodSource);
            } catch (InterruptedException e) {
                System.err.
                        println("Unable to get stock response from food service with error: " + e);
                return;
            }
            // return if no food requests can be delivered
            if (stockResponse.getStockList() != null && stockResponse.
                    getStockList().
                    size() < 1) {
                System.out.println("Currently no Food requests to fulfill");
                return;
            }

            // call helper function to get logistics channel
            logisticsChannel = Common.
                    getChannel("logistics-service", serviceDiscovery);
            if (logisticsChannel == null) {
                responseObserver.onError(NOT_FOUND.
                        withDescription("Unable to find Logistics Service").
                        asException());
                return;
            }

            // get the logistics blocking stub from the logistics grpc
            LogisticsServiceBlockingStub logistics;
            logistics = LogisticsServiceGrpc.newBlockingStub(logisticsChannel);

            // loop over the list and try to make deliveries
            for (Stock stock : stockResponse.getStockList()) {
                // call helper method that send the deleivery request
                sendDeliveryRequest(stock, logistics);
            }

            System.out.println("System Checks finished");
        } catch (IOException ex) {
            System.err.println("Unable to connect to DNS");
        }
        finally {
            // if the channel is still active, shut it down
            if (logisticsChannel != null) {
                System.out.println("Shutting down the Logistics channel channel.");
                try {
                    logisticsChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for channel shutdown: " + e.getMessage());
                    // if an error ocurred in shutdown call interupt on the thread
                    Thread.currentThread().interrupt();
                }
            }
            // check to see if the food source channel is still active
             if (foodSourceChannel != null) {
                System.out.println("Shutting down the Food Source channel.");
                try {
                    foodSourceChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for channel shutdown: " + e.getMessage());
                    // if an error ocurred in shutdown call interupt on the thread
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private StockResponse checkIfFoodRequestsInStock(FoodSourceServiceStub foodSource) throws InterruptedException {
        // create a countdown to limit the time make the method wait for a response
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // I needed to store the return value from on next in a varibale, found good method here:
        // https://stackoverflow.com/questions/3964211/when-to-use-atomicreference-in-java
        final AtomicReference<StockResponse> responseHolder = new AtomicReference<>();

        StreamObserver<StockResponse> responseObserver = new StreamObserver<StockResponse>() {

            @Override
            public void onNext(StockResponse response) {
                System.out.println("Received data from Food Source");
                // set the response in the response holder
                responseHolder.set(response);
            }

            @Override
            public void onError(Throwable t) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void onCompleted() {
                System.out.
                        println("Finished checking Food Source for Food Requests in stock");
                countDownLatch.countDown(); // call the countdown method to allow the server to move on
            }
        };

        // use the newly created responseObserver to be used in the calling code
        StreamObserver<SavedFoodRequest> requestObserver = foodSource.
                checkIfFoodRequestIsInStock(responseObserver);

        // loop through all the saved food requests and stream them to the Food Service
        for (SavedFoodRequest foodRequest : foodRequests) {
            requestObserver.onNext(foodRequest);
        }

        // after the loop finishes, call the onComplete method to signify the stream has finished
        requestObserver.onCompleted();

        // 
        if (!countDownLatch.await(1, TimeUnit.MINUTES)) {
            System.err.println("RPC timed out.");
            requestObserver.onError(new RuntimeException("Timeout"));
            return null;
        }
        // return the stored response
        return responseHolder.get();
    }

    private void sendDeliveryRequest(Stock stock, LogisticsServiceBlockingStub logistics) {
        int requestID = stock.getRequestId();
        SavedFoodRequest matchingRequest = null;
        for (SavedFoodRequest foodRequest : foodRequests) {
            if (foodRequest.getRequestId() == requestID) {
                matchingRequest = foodRequest;
                break;
            }
        }

        if (matchingRequest == null) {
            System.out.
                    println("No saved request found for returned in stock request, id: " + requestID);
        } else {
            System.out.
                    println("Matching request found... attempting delivery request");
            // extract the foodRequest from the saved request
            FoodRequest foodRequestForDelivery = matchingRequest.
                    getFoodRequest();
            // get the destination from the matching food request
            Address destination = matchingRequest.getFoodRequest().
                    getDestination();
            // get the location of where the food request can be fulfilled from
            Address location = stock.getAddress();
            // get the items that are to be delivered
            List<FoodItemQuantity> itemsForDelivery = foodRequestForDelivery.
                    getItemsList();
            // Build the delivery message from the details above
            Delivery delivery = Delivery.newBuilder().
                    addAllItems(itemsForDelivery).
                    setDestination(destination).
                    setLocation(location).
                    build();

            try {
                DeliveryResponse response = logistics.
                        handleDeliveryRequest(delivery);
                // only update the food request if it has been accepted
                if (response.getAccepted()) {
                    // create an update message based on the response from the logistics service
                    SavedFoodRequest updatedRequest = matchingRequest.
                            toBuilder().
                            setDeliveryId(response.getDeliveryId()).
                            setStatus("Enroute").
                            setPickupTime(response.getPickupTime()).
                            build();

                    // we replace the old saved food request with the updated version
                    int index = foodRequests.indexOf(matchingRequest);
                    foodRequests.set(index, updatedRequest);
                    System.out.
                            println("Delivery request accepted. DeliveryID: " + updatedRequest.
                                    getDeliveryId() + "expected pick up time: " + updatedRequest.
                                            getPickupTime());
                } else {
                    System.out.println("Delivery Request has not been accepted");
                }
            } catch (StatusRuntimeException ex) {
                System.err.
                        println("Failed with deivery request with error: " + ex.
                                getMessage());
            }
        }
    }
}
