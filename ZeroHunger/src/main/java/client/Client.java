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
import grpc.common.SavedFoodRequest;
import grpc.logistics.LogisticsServiceGrpc;
import grpc.logistics.LogisticsServiceGrpc.LogisticsServiceStub;
import grpc.smart_hub.SavedFoodRequests;
import grpc.smart_hub.SmartHubServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author gerto
 */
public class Client {

    // the current list of requests
    public static List<SavedFoodRequest> currentRequests = new ArrayList<>();

    public static void main(String[] args) {
        // declare channels here so can be checked and shutdown
        ManagedChannel logisticsChannel = null;
        ManagedChannel smartHubChannel = null;

        ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
        try {
            serviceDiscovery.start();

            // create connection to smart hub
            smartHubChannel = Common.
                    getChannel("smart-hub-service", serviceDiscovery);

            if (smartHubChannel == null) {
                System.err.println("Unable to find Smart hub service.... ");
                return;
            }
            SmartHubServiceGrpc.SmartHubServiceBlockingStub smartHub;
            smartHub = SmartHubServiceGrpc.newBlockingStub(smartHubChannel);

            // create connection to logistics
            logisticsChannel = Common.
                    getChannel("logistics-service", serviceDiscovery);

            if (logisticsChannel == null) {
                System.err.println("Unable to find Logistics service.... ");
                return;
            }

            LogisticsServiceStub logistics;
            logistics = LogisticsServiceGrpc.newStub(logisticsChannel);

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

                JPanel statusPanel = new JPanel(new BorderLayout());

                // create a list from the current request saved on the server
                JScrollPane requestList = buildCurrentRequestDisplay(smartHub);
                statusPanel.add(requestList);
                
                JPanel trackingPanel = buildTrackingPanel(smartHub);

                JPanel requestPanel = createRequestPanel(smartHub, statusPanel);

                // add column for showing status of requests
                statusPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Current Request Status",
                        TitledBorder.CENTER,
                        TitledBorder.TOP
                ));

                statusPanel.setOpaque(false);

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

            // on loading, trigger a system check
            Empty emptyRequest = Empty.newBuilder().
                    build();
            smartHub.triggerChecks(emptyRequest);
        } catch (StatusRuntimeException | IOException ex) {
            System.out.println("Error when running the client. Error: " + ex.
                    getMessage());
        } finally {
            // if the channel is still active, shut it down
            if (logisticsChannel != null) {
                System.out.
                        println("Shutting down the Logistics channel channel.");
                try {
                    logisticsChannel.shutdown().
                            awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    System.err.
                            println("Interrupted while waiting for channel shutdown: " + e.
                                    getMessage());
                    // if an error ocurred in shutdown call interupt on the thread
                    Thread.currentThread().
                            interrupt();
                }
            }
            // check to see if the smarthub channel is still active
            if (smartHubChannel != null) {
                System.out.println("Shutting down the Food Source channel.");
                try {
                    smartHubChannel.shutdown().
                            awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    System.err.
                            println("Interrupted while waiting for channel shutdown: " + e.
                                    getMessage());
                    // if an error ocurred in shutdown call interupt on the thread
                    Thread.currentThread().
                            interrupt();
                }
            }
        }
    }

    public static void updateCurrentRequests(SmartHubServiceGrpc.SmartHubServiceBlockingStub smartHub) {
        Empty emptyRequest = Empty.newBuilder().
                build();
        SavedFoodRequests requestsOnServer = smartHub.
                getCurrentRequests(emptyRequest);

        // remove all items from the list and get add the up to date items from the server
        currentRequests.clear();
        currentRequests.addAll(requestsOnServer.getItemsList());
    }

    public static JScrollPane buildCurrentRequestDisplay(SmartHubServiceGrpc.SmartHubServiceBlockingStub smartHub) {
        updateCurrentRequests(smartHub);
        DefaultListModel<String> listData = new DefaultListModel<>();
        for (SavedFoodRequest request : currentRequests) {
            listData.
                    addElement("ID: " + request.getRequestId() + " | Status: " + request.
                            getStatus());
        }
        JList<String> requestList = new JList<>((ListModel<String>) listData);

        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(requestList);
        return scrollPane;
    }

    public static JScrollPane buildCurrentDeliveriesDisplay(SmartHubServiceGrpc.SmartHubServiceBlockingStub smartHub, JPanel locationpanel) {
        updateCurrentRequests(smartHub);
        DefaultListModel<String> listData = new DefaultListModel<>();
        for (SavedFoodRequest request : currentRequests) {
            if (request.getDeliveryId() != 0) {
                listData.
                        addElement("ID: " + request.getRequestId() + " | DeliveryID: " + request.
                                getDeliveryId() + " | Pick up time: " + request.
                                        getPickupTime());
            }

        }
        JList<String> requestList = new JList<>((ListModel<String>) listData);

        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
         requestList.addListSelectionListener(e -> {
             // Add event listener so when a delivery is selected, it can start streaming the location
             // ensure that an item is selected
            if (!e.getValueIsAdjusting() && requestList.getSelectedValue() != null) {
                // get the value and extract the delivery id
                String value = requestList.getSelectedValue();
                int start = value.indexOf("DeliveryID: ") + 12;
                int end = value.lastIndexOf("|");
                String stringDeliveryID = value.substring(start, end).trim();
                int deliveryID = Integer.parseInt(stringDeliveryID);
                System.out.println(deliveryID);
            }
        });

        JScrollPane scrollPane = new JScrollPane(requestList);
        return scrollPane;
    }

    public static JPanel createRequestPanel(SmartHubServiceGrpc.SmartHubServiceBlockingStub smartHub, JPanel statusPanel) {
        JPanel requestPanel = new JPanel(new BorderLayout());
        // add column for making a requests
        requestPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Make Request",
                TitledBorder.CENTER,
                TitledBorder.TOP
        ));

        // set the layout to 2 rows, 1 column
        requestPanel.setLayout(new GridLayout(2, 1));

        // create an array of essential items
        List<String> essentialGroceries = Common.getEssentialGroceriesList();
        // create a list for holding the qunatity inputs
        List<JSpinner> quantitySpinners = new ArrayList<>();
        JPanel foodGridPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        // loop over the items and create an input for each item in the grocery list
        for (String item : essentialGroceries) {
            foodGridPanel.add(new JLabel(item));
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            quantitySpinners.add(quantitySpinner);
            foodGridPanel.add(quantitySpinner);
        }

        // the list of items is big , so make the pane scrollable to handle multiple screen sizes
        JScrollPane foodScrollPane = new JScrollPane(foodGridPanel);
        foodScrollPane.setBorder(BorderFactory.
                createTitledBorder("Select Food & Quantity"));

        // create a panel to hold the address elements
        JPanel addressPanel = new JPanel(new BorderLayout(3, 1));
        addressPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Delivery Address",
                TitledBorder.CENTER,
                TitledBorder.TOP
        ));

        // Create eircode input with label
        JPanel eircodeInputPanel = new JPanel(new BorderLayout(2, 1));
        JTextArea eircodeArea = new JTextArea(1, 5);
        JLabel eircodeLabel = new JLabel("Enter Eircode: ");
        eircodeLabel.setLabelFor(eircodeArea);
        eircodeInputPanel.add(eircodeLabel, BorderLayout.NORTH);
        eircodeInputPanel.add(eircodeArea, BorderLayout.SOUTH);

        // add eircode input panel to address panel
        addressPanel.add(eircodeInputPanel, BorderLayout.NORTH);

        // Create address input with label
        JPanel addressInputPanel = new JPanel(new BorderLayout(2, 1));
        JTextArea addressArea = new JTextArea(1, 5);
        JLabel addressLabel = new JLabel("Enter Delivery Address: ");
        addressLabel.setLabelFor(addressArea);
        addressInputPanel.add(addressLabel, BorderLayout.NORTH);
        addressInputPanel.add(addressArea, BorderLayout.CENTER);

        // add address input panel to address panel
        addressPanel.add(addressInputPanel, BorderLayout.CENTER);

        // create a submit button
        JButton submitButton = new JButton("Submit Order");
        submitButton.addActionListener((ActionEvent e) -> {
            // Get the entered address data
            String address = addressArea.getText().
                    trim();
            String eircode = eircodeArea.getText().
                    trim();

            // check if an address and eircode have been entered
            if (address.isEmpty() || eircode.isEmpty()) {
                JOptionPane.showMessageDialog(requestPanel,
                        "Please fill in both the Delivery Address and Eircode.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // create the address for the request from the inputs
            Address addressRequest = Address.newBuilder().
                    setAddress(address).
                    setEircode(eircode).
                    build();

            List<FoodItemQuantity> foodRequests = new ArrayList<>();
            // loop over the list of quantity inputs, and if they are higher than 0
            // add them to the food list
            for (int i = 0; i < essentialGroceries.size(); i++) {
                // Get the value from the spinner
                int quantity = (int) quantitySpinners.get(i).
                        getValue();
                // Only add items with a quantity greater than 0
                if (quantity > 0) {
                    FoodItem item = FoodItem.newBuilder().
                            setId(i).
                            setName(essentialGroceries.get(i)).
                            build();
                    FoodItemQuantity itemQuantity = FoodItemQuantity.
                            newBuilder().
                            setItem(item).
                            setQuantity(1).
                            build();
                    foodRequests.add(itemQuantity);
                }
            }

            // ensure that there are at least one item with a quantity higher than 0
            if (foodRequests.isEmpty()) {
                JOptionPane.showMessageDialog(requestPanel,
                        "Please select a quantity for at least one item.",
                        "Empty Order",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // create a request for sending to the smarthub
            FoodRequest request = FoodRequest.newBuilder().
                    addAllItems(foodRequests).
                    setDestination(addressRequest).
                    build();

            // send the request to the smarthub
            SavedFoodRequest response = smartHub.handleFoodRequests(request);

            System.out.println(response.getStatus());

            // show the response to the user
            JOptionPane.showMessageDialog(requestPanel,
                    "Request saved with an id of: " + response.
                            getRequestId() + " , current status: " + response.
                            getStatus(),
                    "Request Received",
                    JOptionPane.PLAIN_MESSAGE);
            // make the status panel reactive by deleting old requests and rebuilding with the updated list
            JScrollPane requestList = buildCurrentRequestDisplay(smartHub);
            statusPanel.removeAll();
            statusPanel.add(requestList);
        });
        addressPanel.add(submitButton, BorderLayout.SOUTH);
        requestPanel.setOpaque(false);

        requestPanel.add(foodScrollPane);
        requestPanel.add(addressPanel);

        return requestPanel;
    }

    public static JPanel buildTrackingPanel(SmartHubServiceGrpc.SmartHubServiceBlockingStub smartHub) {
        JPanel trackingPanel = new JPanel(new BorderLayout());
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

        JScrollPane currentDeliveries = buildCurrentDeliveriesDisplay(smartHub, locationPanel);
        
        deliveryPanel.add(currentDeliveries);


        // add the 2 inner containers to the tracking panel
        trackingPanel.add(deliveryPanel);
        trackingPanel.add(locationPanel);
        trackingPanel.setOpaque(false);
        
        return trackingPanel;
        
    }
}
