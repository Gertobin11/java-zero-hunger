/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.Logistics;

import dns.ServiceRegistry;
import grpc.logistics.Delivery;
import grpc.logistics.DeliveryResponse;
import grpc.logistics.Location;
import grpc.logistics.LocationUpdate;
import grpc.logistics.LogisticsServiceGrpc.LogisticsServiceImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import static io.grpc.Status.NOT_FOUND;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author gerto
 */
public class Logistics extends LogisticsServiceImplBase {

    List<Integer> acceptedDeliveries = new ArrayList<>();

    /**
     * Method that accepts and rejects requests for delivery
     *
     * @param delivery the items to deliver, the pickup address and the
     * destination
     * @param responseObserver the response we send to the client
     */
    @Override
    public void handleDeliveryRequest(Delivery delivery, StreamObserver<DeliveryResponse> responseObserver) {

        // For this example we just accept all incoming delivery requests
        int acceptedDeliveryID = acceptedDeliveries.size() + 1;
        acceptedDeliveries.add(acceptedDeliveryID);
        LocalDateTime myDateObj = LocalDateTime.now();
        // set the delivery time to be in 30 minuites time
        LocalDateTime pickupTime = myDateObj.plusMinutes(30);

        // create the message with the deliveryID and the message details
        DeliveryResponse response = DeliveryResponse.newBuilder().
                setAccepted(true).
                setDeliveryId(acceptedDeliveryID).
                setMessage("Driver has been dispatched to pick up location").
                setPickupTime(pickupTime.format(DateTimeFormatter.ISO_DATE)).
                build();

        // send the message
        responseObserver.onNext(response);

        // tell the client the method is finished
        responseObserver.onCompleted();
    }

    /**
     * Method that accepts a stream of location update requests and gives checks
     * to see if has been delivered, and responds with location update
     *
     * @param responseObserver used to send a response to the client
     * @return current latitude and longitude and the current status of the
     * delivery
     */
    @Override
    public StreamObserver<LocationUpdate> trackDelivery(StreamObserver<Location> responseObserver) {

        System.out.println("Streaming delivery location....");
        // we just check if a delivery has been accepted then we simulate the latitude longitude moving towards a delivery
        return new StreamObserver<LocationUpdate>() {
            // mock location as int -- Tralee town center
            // this would be sent via smart devices on the truck or van
            int currentLongitude = 522709;
            int currentLatitude = -96974;

            //  mock location as int for destination
            // this would be generated from the eircode
            int destinationLongitude = 532709;
            int destinationLatitude = -97974;

            @Override
            public void onNext(LocationUpdate value) {
                // we set the status of the current delivery
                String status = "Enroute";
                // we move the delivery towards the destination
                if (currentLongitude < destinationLongitude) {
                    currentLongitude++;
                }

                if (currentLatitude > destinationLatitude) {
                    currentLatitude--;
                }

                // We check if the delivery is at the destination, then change the status
                if (currentLongitude == destinationLongitude && currentLatitude == destinationLatitude) {
                    status = "Delivered";
                }

                // every on next we verify that the request is for a valid delivery
                if (!acceptedDeliveries.contains(value.getDeliveryId())) {
                    responseObserver.onError(NOT_FOUND.
                            withDescription("Delivery Request with an ID of " + value.
                                    getDeliveryId() + " not found").
                            asException());
                } else {
                    // we change the longitude to the proper string format
                    String longitude = (currentLongitude + "");
                    String formattedLongitude = longitude.substring(0, 2) + "." + longitude.
                            substring(2);
                    // we change the latitude to the proper string format
                    String latitude = (currentLatitude + "");
                    String formattedLatitude = latitude.substring(0, 2) + "." + latitude.
                            substring(2);

                    // we build the current location message
                    Location location = Location.newBuilder().
                            setDeliveryId(value.getDeliveryId()).
                            setLatitude(formattedLatitude).
                            setLongitude(formattedLongitude).
                            setStatus(status).
                            build();

                    // we stream the current location to the caller
                    responseObserver.onNext(location);
                }
            }

            @Override
            public void onError(Throwable t) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void onCompleted() {
                System.out.println("Request for delivery location update ended");
                responseObserver.onCompleted();
            }
        };

    }

}
