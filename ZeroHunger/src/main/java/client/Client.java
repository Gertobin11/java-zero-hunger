/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import com.google.protobuf.Empty;
import common.Common;
import dns.ServiceDiscovery;
import grpc.common.Address;
import grpc.common.FoodItem;
import grpc.common.FoodItemQuantity;
import grpc.common.FoodRequest;
import grpc.smart_hub.SmartHubServiceGrpc;
import grpc.smart_hub.StatusResponse;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author gerto
 */
public class Client {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame parentFrame = new JFrame();
            // how to make a Jframe fullscreen https://stackoverflow.com/questions/11570356/jframe-in-full-screen-java
            parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            parentFrame.setUndecorated(true);
            parentFrame.setVisible(true);

            // create basic layout with heading
            JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
            mainContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
            mainContainer.setBackground(new Color(240, 240, 240));
            JLabel mainHeading = new JLabel("Zero Hunger - Smart Food Management Service", SwingConstants.CENTER);
            mainHeading.setOpaque(false);
            parentFrame.add(mainContainer);
            mainContainer.add(mainHeading, BorderLayout.NORTH);

            // create inner layout that holds the 3 columns of data
            JPanel innerLayout = new JPanel(new GridLayout(1, 3, 15, 15));
            innerLayout.setOpaque(false);

            // define the inner columns that display data
            JPanel requestPanel = new JPanel(new BorderLayout());
            JPanel statusPanel = new JPanel(new BorderLayout());
            JPanel trackingPanel = new JPanel(new BorderLayout());

            // add column for making a requests
            requestPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(),
                    "Make Request",
                    TitledBorder.CENTER,
                    TitledBorder.TOP
            ));

            requestPanel.setOpaque(false);

            // add column for showing status of requests
            statusPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(),
                    "Current Request Status",
                    TitledBorder.CENTER,
                    TitledBorder.TOP
            ));

            statusPanel.setOpaque(false);

            // add column to track deliveries
            trackingPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(),
                    "Track Delivery",
                    TitledBorder.CENTER,
                    TitledBorder.TOP
            ));
            
            // set the layout of the tracking panel to have 2 rows
            trackingPanel.setLayout(new GridLayout(2, 1));
            
            // add container for current deliveries
            JPanel deliveryPanel = new JPanel(new BorderLayout());
            
            deliveryPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(),
                    "Current Deliveries",
                    TitledBorder.CENTER,
                    TitledBorder.TOP
            ));
            deliveryPanel.setOpaque(false);
            
            // add panel for showing current location
            JPanel locationPanel = new JPanel(new BorderLayout());
            
            locationPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(),
                    "Current Location",
                    TitledBorder.CENTER,
                    TitledBorder.TOP
            ));
            locationPanel.setOpaque(false);
            
            // add the 2 inner containers to the tracking panel
            trackingPanel.add(deliveryPanel);
            trackingPanel.add(locationPanel);
            trackingPanel.setOpaque(false);
            // add the columns to the inner layout
            innerLayout.add(requestPanel);
            innerLayout.add(statusPanel);
            innerLayout.add(trackingPanel);

            mainContainer.add(innerLayout);
            
            // create a panel to hold the close button
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.setOpaque(false);
            JButton exitButton = new JButton("Close Application");
            bottomPanel.add(exitButton);
            mainContainer.add(bottomPanel, BorderLayout.SOUTH);
            // add a listener to close the application when clicked
            exitButton.addActionListener(e -> {
                parentFrame.dispose();
                System.exit(0);
            });
            parentFrame.setVisible(true);
        });

        try {
            // on loading, trigger a system check
            ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
            serviceDiscovery.start();

            ManagedChannel smartHubChannel = Common.
                    getChannel("smart-hub-service", serviceDiscovery);

            if (smartHubChannel == null) {
                System.err.println("Unable to find Smart hub service.... ");
                return;
            }
            SmartHubServiceGrpc.SmartHubServiceBlockingStub smartHub;
            smartHub = SmartHubServiceGrpc.newBlockingStub(smartHubChannel);
            Empty emptyRequest = Empty.newBuilder().
                    build();
            Address address = Address.newBuilder().
                    setAddress("test").
                    setEircode("v92 1234").
                    build();
            FoodItem item = FoodItem.newBuilder().
                    setId(1).
                    setName("Milk").
                    build();
            FoodItemQuantity itemQuantity = FoodItemQuantity.newBuilder().
                    setItem(item).
                    setQuantity(1).
                    build();
            FoodRequest request = FoodRequest.newBuilder().
                    addItems(itemQuantity).
                    setDestination(address).
                    build();
            StatusResponse response = smartHub.handleFoodRequests(request);
            System.out.println(response.getStatus());
            smartHub.triggerChecks(emptyRequest);
        } catch (IOException | StatusRuntimeException ex) {
            System.out.println("Error when running the client. Error: " + ex.
                    getMessage());
        }

    }

}
