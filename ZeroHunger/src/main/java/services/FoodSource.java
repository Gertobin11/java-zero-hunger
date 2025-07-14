/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import grpc.common.Address;
import grpc.common.FoodItem;
import grpc.common.FoodItemQuantity;
import grpc.food_source.FoodSourceServiceGrpc.FoodSourceServiceImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * A method that returns an array list of FoodItemQunatity items 
     * that match the passed address
     * @param address an instance of an Address, with a location String and an eircode String
     * @param responseObserver what we send back to the client i.e a stream of foodItemQuantities
     */
    @Override
    public void streamAvailableFoodItems(Address address, StreamObserver<FoodItemQuantity> responseObserver) {
        System.out.println("Received request for available food items at eircode: " + address.getEircode());
        // get the dummy data
        Map<String, List<FoodItemQuantity>> dummyData = generateDummyData();
        // get the eircode, which acts as the key in the map
        String eircode = address.getEircode();
        // get the list of available food items at the eircode
        List<FoodItemQuantity> availableFoodItems = dummyData.get(eircode);

        for (FoodItemQuantity availableFoodItem : availableFoodItems) {
            responseObserver.onNext(availableFoodItem);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        responseObserver.onCompleted();

        System.out.println(
                "Finished listing available items at: " + address.getEircode());
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
        String[] essentialGroceries = {
            "Milk",
            "Pasta",
            "Rice",
            "Tomatoes",
            "Beans",
            "Fish",
            "Pasta Sauce",
            "Cereal",
            "Oats",
            "Soup",
            "Cooking Oil",
            "Tea",
            "Coffee",
            "Sugar",
            "Flour",
            "Peanut Butter",
            "Jam",
            "Biscuits",
            "Vegetables",
            "Salt"
        };

        for (String eircode : eircodes) {
            // create the arraylist of food items
            List<FoodItemQuantity> fooditemQuantityList = new ArrayList<>();
            for (int i = 0; i <= essentialGroceries.length; i++) {
                // create a food item from the FoodItem message 
                FoodItem foodItem = FoodItem.newBuilder().setId(i + 1).setName(essentialGroceries[i]).build();
                // create  a foodItemQuantity, using the previous FoodItem
                FoodItemQuantity foodItemQuantity = FoodItemQuantity.newBuilder().setItem(foodItem).setQuantity(+3).build();
                // add it to the list
                fooditemQuantityList.add(foodItemQuantity);
            }
            // create an entry in the map for the current eircode in the loop
            eircodeToFoodItemQuantityMap.put(eircode, fooditemQuantityList);
//          
        }
        return eircodeToFoodItemQuantityMap;
    }
}
