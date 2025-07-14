/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import grpc.logistics.Delivery;
import grpc.logistics.DeliveryResponse;
import grpc.logistics.LogisticsServiceGrpc.LogisticsServiceImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
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

    public static void main(String[] args) throws IOException, InterruptedException {

        Logistics mainLogistics = new Logistics();

        int port = 50053;

        Server server = ServerBuilder.forPort(port).
                addService(mainLogistics).
                build().
                start();

        System.out.
                println("Main Logistics Service started, listening on " + port);

        server.awaitTermination();
    }

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

}
