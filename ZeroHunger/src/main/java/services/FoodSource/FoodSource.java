/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.FoodSource;

import common.Common;
import grpc.common.Address;
import grpc.common.FoodItem;
import grpc.common.FoodItemQuantity;
import grpc.common.FoodRequest;
import grpc.common.SavedFoodRequest;
import grpc.food_source.FoodSourceServiceGrpc.FoodSourceServiceImplBase;
import grpc.food_source.Stock;
import grpc.food_source.StockResponse;
import grpc.food_source.StockResponse.Builder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author gerto
 */
public class FoodSource extends FoodSourceServiceImplBase {

    /**
     * A method that returns an array list of FoodItemQunatity items that match
     * the passed address
     *
     * @param address an instance of an Address, with a location String and an
     * eircode String
     * @param responseObserver what we send back to the client i.e a stream of
     * foodItemQuantities
     */
    @Override
    public void streamAvailableFoodItems(Address address, StreamObserver<FoodItemQuantity> responseObserver) {
        System.out.
                println("Received request for available food items at eircode: " + address.
                        getEircode());
        // get the dummy data
        Map<String, List<FoodItemQuantity>> dummyData = generateDummyData();
        // get the eircode, which acts as the key in the map
        String eircode = address.getEircode();
        // get the list of available food items at the eircode
        List<FoodItemQuantity> availableFoodItems = dummyData.get(eircode);

        for (FoodItemQuantity availableFoodItem : availableFoodItems) {
            responseObserver.onNext(availableFoodItem);
            // introduce a sleep to mimic database look up
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().
                        interrupt();
            }
        }
        responseObserver.onCompleted();

        System.out.println(
                "Finished listing available items at: " + address.getEircode());
    }

    /**
     * Method that receives a stream of requested food items, checks if it is in
     * stock in each location and returns a message of request ids to locations
     * if it is in stock
     *
     * @param responseObserver used to return the message to the client
     * @return an array of locations with food item request id if it is in stock
     */
    @Override
    public StreamObserver<SavedFoodRequest> checkIfFoodRequestIsInStock(StreamObserver<StockResponse> responseObserver) {
        System.out.println("Started receiving requests for stock ");
        // crate a builder that we can add to on every message received if a request is in stock
        Builder builder = StockResponse.newBuilder();

        return new StreamObserver<SavedFoodRequest>() {
            // generate dummy data to handle the request
            Map<String, List<FoodItemQuantity>> dummyData = generateDummyData();

            @Override
            public void onNext(SavedFoodRequest savedFoodRequest) {
                // loop over the entries in the dummy data
                for (Map.Entry<String, List<FoodItemQuantity>> entry : dummyData.
                        entrySet()) {
                    // extract the eircode and the items
                    String eircode = entry.getKey();
                    List<FoodItemQuantity> items = entry.getValue();

                    // set the value to true before each check
                    boolean inStock = true;
                    FoodRequest foodRequest = savedFoodRequest.getFoodRequest();
                    List<FoodItemQuantity> requestedItems = foodRequest.
                            getItemsList();
                    // loop through all the items in the request
                    for (FoodItemQuantity requestedItem : requestedItems) {
                        // check if the item is in the location by checking the item id
                        // stream filter tutorial https://www.geeksforgeeks.org/java/stream-filter-java-examples/
                        Optional<FoodItemQuantity> matchingItem = items.stream().
                                filter(item -> item.getItem().
                                getId() == requestedItem.getItem().
                                        getId()).
                                findFirst();
                        if (matchingItem.isEmpty()) {
                            // if there is no match we set inStock to false and break out of this location
                            inStock = false;
                            break;
                        } else if (requestedItem.getQuantity() > matchingItem.
                                get().
                                getQuantity()) {
                            // if the quantity is less than what is requested we set in stock to false and break out of this location
                            inStock = false;
                            break;
                        }
                    }
                    if (inStock) {
                        Address address = Address.newBuilder().
                                setAddress("").
                                setEircode(eircode).
                                build();

                        // if it is in stock we save the eircode of where it is in stock with the request id and break out of the loop
                        Stock stock = Stock.newBuilder().
                                setAddress(address).
                                setRequestId(savedFoodRequest.getRequestId()).
                                setInstock(true).
                                build();
                        builder.addStock(stock);
                        System.out.println("Added request "+ savedFoodRequest.getRequestId()+ " to it stock list");
                        break;
                    }
                    else {
                        System.out.println("Unable to fulfill request:"+ savedFoodRequest.getRequestId()+ " not enough stock in location: " + entry.getKey());
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
               System.err.println("Unable to perform check if request is in stock with error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Finished checking requests");
                // build the message that we have been building up on the on next call
                StockResponse response = builder.build();
                // call the onNext on the responseObserver to send the message
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * Function that will generate dummy data that mocks the data sent from the
     * smart inventory in supermarkets etc..
     *
     * @return a map with an eircode and an array of foodItemQuantity objects at
     * that location
     */
    private static Map<String, List<FoodItemQuantity>> generateDummyData() {
        // create an empty map
        Map<String, List<FoodItemQuantity>> eircodeToFoodItemQuantityMap = new HashMap<>();
        // create an array of fake eircodes
        String[] eircodes = {"V92 234R", "V92 334J", "V92 1543"};
        // create an array of essential items
        List<String> essentialGroceries = Common.getEssentialGroceriesList();

        for (String eircode : eircodes) {
            // create the arraylist of food items
            List<FoodItemQuantity> fooditemQuantityList = new ArrayList<>();
            for (int i = 0; i < essentialGroceries.size(); i++) {
                // create a food item from the FoodItem message 
                FoodItem foodItem = FoodItem.newBuilder().
                        setId(i + 1).
                        setName(essentialGroceries.get(i)).
                        build();
                // create  a foodItemQuantity, using the previous FoodItem
                FoodItemQuantity foodItemQuantity = FoodItemQuantity.
                        newBuilder().
                        setItem(foodItem).
                        setQuantity(+3).
                        build();
                // add it to the list
                fooditemQuantityList.add(foodItemQuantity);
            }
            // create an entry in the map for the current eircode in the loop
            eircodeToFoodItemQuantityMap.put(eircode, fooditemQuantityList);
        }
        return eircodeToFoodItemQuantityMap;
    }
}
