/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.SmartHub;

import com.google.protobuf.Empty;
import dns.ServiceDiscovery;
import grpc.common.FoodRequest;
import grpc.common.SavedFoodRequest;
import grpc.food_source.FoodSourceServiceGrpc;
import grpc.food_source.FoodSourceServiceGrpc.FoodSourceServiceStub;
import grpc.food_source.Stock;
import grpc.food_source.StockResponse;
import grpc.smart_hub.SmartHubServiceGrpc.SmartHubServiceImplBase;
import grpc.smart_hub.StatusRequest;
import grpc.smart_hub.StatusResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import static io.grpc.Status.NOT_FOUND;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.ServiceInfo;

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

    @Override
    public void triggerChecks(Empty request, StreamObserver<Empty> responseObserver) {
        StockResponse stockResponse = null;
        System.out.println("Smart Hub Service initiating action checks....");

        // return if no current food requests
        if (foodRequests.size() < 1) {
            System.out.println("No requests to check");
            return;
        }
        System.out.println("Attempting to contact Food Source");

        ManagedChannel channel = getFoodSourceChannel();
        if (channel == null) {
            responseObserver.onError(NOT_FOUND.
                    withDescription("Unable to find Food Source Service").
                    asException());
            return;
        }
        FoodSourceServiceStub foodSource = FoodSourceServiceGrpc.
                newStub(channel);
        
        try {
            stockResponse = checkIfFoodRequestsInStock(foodSource);
        } catch (InterruptedException e) {
            System.err.println("Unable to get stock response from food service with error: " + e);
            return;
        }
        // return if no food requests can be delivered
        if(stockResponse.getStockList().size() < 1) {
            System.out.println("Currently no Food requests to fulfill");
            return;
        }
        
        // loop over the list and try to make deliveries
        for(Stock stock: stockResponse.getStockList()) {
            
        }
    }

    private ManagedChannel getFoodSourceChannel() {
        ServiceInfo serviceInfo = null;
        int retries = 3;
        while (retries > 0) {
            // call the find service method to get the service info for Food Source
            serviceInfo = serviceDiscovery.findService("food-source-service");
            try {
                Thread.sleep(3000); // wait for 3 seconds before retrying
            } catch (InterruptedException ex) {
                Thread.currentThread().
                        interrupt();
            }
            retries--;
        }

        // if service is not found return null
        if (serviceInfo == null) {
            return null;
        }

        // get the host and port from the service info
        String host = serviceInfo.getInet4Addresses()[0].getHostAddress();
        int port = serviceInfo.getPort();

        System.out.println("FoodSource service found at " + host + ":" + port);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).
                usePlaintext().
                build();

        return channel;
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
}
