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
import grpc.logistics.Location;
import grpc.logistics.LocationUpdate;
import grpc.logistics.LogisticsServiceGrpc;
import grpc.logistics.LogisticsServiceGrpc.LogisticsServiceStub;
import grpc.smart_hub.SavedFoodRequests;
import grpc.smart_hub.SmartHubServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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
 * @author gertobin
 */
public class Client {

    // the current list of requests
    public static List<SavedFoodRequest> currentRequests = new ArrayList<>();

    private static StreamObserver<LocationUpdate> activeRequestObserver;
    private static int activeDeliveryId = -1;

    // use of schedulers https://mkyong.com/java/java-scheduledexecutorservice-examples/
    private static final ScheduledExecutorService scheduler = Executors.
            newSingleThreadScheduledExecutor();
    private static Future<?> activePollingFuture;

    // innerLayout so as it can be accessed throught the app
    private static JPanel innerLayout;

    private static JPanel trackingPanel;
    private static JPanel statusPanel;
    private static JPanel deliveryPanel;
    private static JPanel locationPanel;

    // Make the grpc stubs static so as can be accessed throughout the client app
    private static SmartHubServiceGrpc.SmartHubServiceBlockingStub smartHub;
    private static LogisticsServiceStub logistics;

    public static void main(String[] args) {
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
        try {
            serviceDiscovery.start();

            // create connection to smart hub
            final ManagedChannel smartHubChannel = Common.
                    getChannel("smart-hub-service", serviceDiscovery);

            if (smartHubChannel == null) {
                System.err.println("Unable to find Smart hub service.... ");
                return;
            }

            smartHub = SmartHubServiceGrpc.newBlockingStub(smartHubChannel);

            // create connection to logistics
            final ManagedChannel logisticsChannel = Common.
                    getChannel("logistics-service", serviceDiscovery);

            if (logisticsChannel == null) {
                System.err.println("Unable to find Logistics service.... ");
                return;
            }

            logistics = LogisticsServiceGrpc.newStub(logisticsChannel);

            SwingUtilities.invokeLater(() -> {
                JFrame parentFrame = new JFrame();
                // how to make a Jframe fullscreen
                // https://stackoverflow.com/questions/11570356/jframe-in-full-screen-java
                parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                parentFrame.setUndecorated(true);
                parentFrame.setVisible(true);

                // create basic layout with heading
                JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
                mainContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
                mainContainer.setBackground(new Color(240, 240, 240));
                JLabel mainHeading = new JLabel("Zero Hunger - Smart Food Management Service",
                        SwingConstants.CENTER);
                mainHeading.setOpaque(false);
                parentFrame.add(mainContainer);
                mainContainer.add(mainHeading, BorderLayout.NORTH);

                // create inner layout that holds the 3 columns of data
                innerLayout = new JPanel(new GridLayout(1, 3, 15, 15));
                innerLayout.setOpaque(false);

                statusPanel = new JPanel(new BorderLayout());

                // create a list from the current request saved on the server
                JScrollPane requestList = buildCurrentRequestDisplay(smartHub);
                statusPanel.add(requestList);

                trackingPanel = buildTrackingPanel();

                JPanel requestPanel = createRequestPanel(statusPanel);

                // add column for showing status of requests
                statusPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Current Request Status",
                        TitledBorder.CENTER,
                        TitledBorder.TOP));

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

                    System.out.
                            println("Shutting down the Logistics channel channel.");
                    try {
                        logisticsChannel.shutdown().
                                awaitTermination(5, TimeUnit.SECONDS);
                    } catch (InterruptedException ex) {
                        System.err.
                                println("Interrupted while waiting for channel shutdown: "
                                        + ex.getMessage());
                        // if an error ocurred in shutdown call interupt on the thread
                        Thread.currentThread().
                                interrupt();
                    }

                    System.out.println("Shutting down the Food Source channel.");
                    try {
                        smartHubChannel.shutdown().
                                awaitTermination(5, TimeUnit.SECONDS);
                    } catch (InterruptedException ex) {
                        System.err.
                                println("Interrupted while waiting for channel shutdown: "
                                        + ex.getMessage());
                        // if an error ocurred in shutdown call interupt on the thread
                        Thread.currentThread().
                                interrupt();
                    }

                    parentFrame.dispose();
                    System.exit(0);
                });
                parentFrame.setVisible(true);
            });

            // on loading, trigger a system check
            triggerUpdates();
        } catch (StatusRuntimeException | IOException ex) {
            System.out.println("Error when running the client. Error: " + ex.
                    getMessage());
        }
    }

    /**
     * Function that makes a call to the smart hub and updates the current
     * requests with the latest from the server
     *
     * @param smartHub the smart hub gprc stub
     */
    public static void updateCurrentRequests(SmartHubServiceGrpc.SmartHubServiceBlockingStub smartHub) {
        Empty emptyRequest = Empty.newBuilder().
                build();
        SavedFoodRequests requestsOnServer = smartHub.
                getCurrentRequests(emptyRequest);

        // remove all items from the list and get add the up to date items from the
        // server
        currentRequests.clear();
        currentRequests.addAll(requestsOnServer.getItemsList());
    }

    /**
     * Function that creates a list with all the current requests that have been
     * accepted for delivery
     *
     * @param smartHub
     * @return JScrollPane
     */
    public static JScrollPane buildCurrentRequestDisplay(SmartHubServiceGrpc.SmartHubServiceBlockingStub smartHub) {
        // Get the latest requests from the server to ensure an up to date list
        updateCurrentRequests(smartHub);
        DefaultListModel<String> listData = new DefaultListModel<>();
        // loop over the list and build a string repesentation of each request
        for (SavedFoodRequest request : currentRequests) {
            listData.
                    addElement("ID: " + request.getRequestId() + " | Status: " + request.
                            getStatus());
        }
        JList<String> requestList = new JList<>((ListModel<String>) listData);

        // only alow a single selection - this allows us to track each order
        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(requestList);

        JButton updateButton = new JButton("Perform System Checks");

        updateButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(innerLayout,
                    "Running System updates on the server, layout and processes are being checked and updated",
                    "Updates Started",
                    JOptionPane.PLAIN_MESSAGE);
            triggerUpdates();
        });
        
        statusPanel.add(updateButton, BorderLayout.NORTH);

        return scrollPane;
    }

    /**
     * Function that creates the components of the track delivery display,
     * implements the bi directional stream that updates the delivery location
     * with each message received
     *
     * @param locationPanel the parent panel that we attach the created
     * components to
     * @return JScrollPane
     */
    public static JScrollPane buildCurrentDeliveriesDisplay(
            JPanel locationPanel) {
        updateCurrentRequests(smartHub);
        DefaultListModel<String> listData = new DefaultListModel<>();
        for (SavedFoodRequest request : currentRequests) {
            if (request.getDeliveryId() != 0) {
                listData.
                        addElement("ID: " + request.getRequestId() + " | DeliveryID: "
                                + request.getDeliveryId() + " | Pick up time: "
                                + request.getPickupTime());
            }
        }
        JList<String> requestList = new JList<>(listData);
        JTextArea coordinates = new JTextArea(3, 20);
        coordinates.setEditable(false);
        coordinates.setLineWrap(true);
        coordinates.setWrapStyleWord(true);
        locationPanel.add(new JScrollPane(coordinates));

        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        requestList.addListSelectionListener(e -> {
            // Exit early if the selection is still changing or nothing is selected
            if (e.getValueIsAdjusting() || requestList.getSelectedValue() == null) {
                return;
            }

            // Extract the new delivery ID
            String value = requestList.getSelectedValue();
            int start = value.indexOf("DeliveryID: ") + 12;
            int end = value.lastIndexOf("|");
            int newDeliveryId;
            try {
                String stringDeliveryID = value.substring(start, end).
                        trim();
                newDeliveryId = Integer.parseInt(stringDeliveryID);
            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                coordinates.setText("Error parsing DeliveryID.");
                return;
            }

            // Stop previous stream and polling task ---
            if (newDeliveryId == activeDeliveryId) {
                System.out.
                        println("Delivery " + newDeliveryId + " is already being tracked.");
                return; // Do nothing
            }

            // Stop the old polling loop if it's running
            if (activePollingFuture != null) {
                activePollingFuture.cancel(true);
            }
            // Close the previous gRPC stream if one exists
            if (activeRequestObserver != null) {
                System.out.
                        println("Closing previous stream for delivery " + activeDeliveryId);
                activeRequestObserver.onCompleted();
            }

            // Prepare the new bidirectional streaM
            activeDeliveryId = newDeliveryId;
            coordinates.
                    setText("Opening stream for delivery: " + activeDeliveryId + "...");
            System.out.
                    println("Opening stream for delivery: " + activeDeliveryId);

            final AtomicBoolean initialMessageSent = new AtomicBoolean(false);

            ClientResponseObserver<LocationUpdate, Location> responseObserver = new ClientResponseObserver<LocationUpdate, Location>() {
                @Override
                public void beforeStart(ClientCallStreamObserver<LocationUpdate> requestStream) {
                    activeRequestObserver = requestStream;
                    requestStream.setOnReadyHandler(() -> {
                        if (requestStream.isReady() && !initialMessageSent.
                                getAndSet(true)) {
                            System.out.println(
                                    "Stream is ready. Sending initial message for delivery: "
                                    + activeDeliveryId);
                            LocationUpdate initialRequest = LocationUpdate.
                                    newBuilder().
                                    setDeliveryId(activeDeliveryId).
                                    build();
                            requestStream.onNext(initialRequest);

                            // This will send a message every 1.5 seconds for an update
                            activePollingFuture = scheduler.
                                    scheduleAtFixedRate(() -> {
                                        try {
                                            System.out.println(
                                                    "Polling for update on delivery: "
                                                    + activeDeliveryId);
                                            LocationUpdate pollingRequest = LocationUpdate.
                                                    newBuilder().
                                                    setDeliveryId(activeDeliveryId).
                                                    build();
                                            // Send the next request for an update
                                            activeRequestObserver.
                                                    onNext(pollingRequest);
                                        } catch (Exception ex) {
                                            System.err.
                                                    println("Polling task failed: "
                                                            + ex.getMessage());
                                            // Cancel the task if it fails
                                            activePollingFuture.cancel(true);
                                        }
                                    }, 1500, 1500, TimeUnit.MILLISECONDS);

                        }
                    });
                }

                @Override
                public void onNext(Location locationUpdate) {
                    // update the text with the latitude and longitude returned from logistics
                    String locationText = "Lat: " + locationUpdate.getLatitude() + " Lon: " + locationUpdate.
                            getLongitude() + " Status: " + locationUpdate.
                                    getStatus();
                    SwingUtilities.invokeLater(() -> coordinates.
                            setText(locationText));
                }

                @Override
                public void onError(Throwable t) {
                    // get the status of the error and print it to the output of the tracking
                    Status status = Status.fromThrowable(t);
                    String errorText = "Stream Error: " + status.getCode();
                    System.err.println(errorText + ": " + status.
                            getDescription());
                    SwingUtilities.invokeLater(() -> coordinates.
                            setText(errorText));
                    if (activePollingFuture != null) {
                        activePollingFuture.cancel(true);
                    }
                    activeRequestObserver = null;
                    activeDeliveryId = -1;
                }

                @Override
                public void onCompleted() {
                    System.out.
                            println("Server has completed the stream for delivery "
                                    + activeDeliveryId);
                    SwingUtilities.invokeLater(() -> coordinates
                            .setText("Delivery " + activeDeliveryId + " complete."));
                    if (activePollingFuture != null) {
                        activePollingFuture.cancel(true);
                    }
                    activeRequestObserver = null;
                    activeDeliveryId = -1;
                }
            };

            // start the call
            logistics.trackDelivery(responseObserver);
        });

        JScrollPane scrollPane = new JScrollPane(requestList);
        return scrollPane;
    }

    /**
     * Method that creates a form that allows a user to make a request to the
     * service
     *
     * @param statusPanel the JPanel that we update with the current requests to
     * keep it up to date
     * @return JPanel s
     */
    public static JPanel createRequestPanel(
            JPanel statusPanel) {
        JPanel requestPanel = new JPanel(new BorderLayout());
        // add column for making a requests
        requestPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Make Request",
                TitledBorder.CENTER,
                TitledBorder.TOP));

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

        // the list of items is big , so make the pane scrollable to handle multiple
        // screen sizes
        JScrollPane foodScrollPane = new JScrollPane(foodGridPanel);
        foodScrollPane.setBorder(BorderFactory.
                createTitledBorder("Select Food & Quantity"));

        // create a panel to hold the address elements
        JPanel addressPanel = new JPanel(new BorderLayout(3, 1));
        addressPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Delivery Address",
                TitledBorder.CENTER,
                TitledBorder.TOP));

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
                    "Request saved with an id of: " + response.getRequestId()
                    + " , current status: " + response.getStatus(),
                    "Request Received",
                    JOptionPane.PLAIN_MESSAGE);
            // make the status panel reactive by deleting old requests and rebuilding with
            // the updated list
            triggerUpdates();
        });
        addressPanel.add(submitButton, BorderLayout.SOUTH);
        requestPanel.setOpaque(false);

        requestPanel.add(foodScrollPane);
        requestPanel.add(addressPanel);

        return requestPanel;
    }

    public static JPanel buildTrackingPanel() {
        trackingPanel = new JPanel(new BorderLayout());
        // add column to track deliveries
        trackingPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Track Delivery",
                TitledBorder.CENTER,
                TitledBorder.TOP));

        // set the layout of the tracking panel to have 2 rows
        trackingPanel.setLayout(new GridLayout(2, 1));

        // add container for current deliveries
        deliveryPanel = new JPanel(new BorderLayout());

        deliveryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Current Deliveries",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        deliveryPanel.setOpaque(false);

        // add panel for showing current location
        locationPanel = new JPanel(new BorderLayout());

        locationPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Current Location",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        locationPanel.setOpaque(false);

        JScrollPane currentDeliveries = buildCurrentDeliveriesDisplay(locationPanel);

        deliveryPanel.add(currentDeliveries);

        trackingPanel.add(deliveryPanel);
        trackingPanel.add(locationPanel);
        trackingPanel.setOpaque(false);

        return trackingPanel;
    }

    /**
     * Function that that triggers the checks performed across the distributed
     * system and updates the the food requests and delivery requests to the
     * most up to date version on the server
     */
    public static void triggerUpdates() {

        // get the updated list of requests
        if (smartHub != null) {
            updateCurrentRequests(smartHub);
        }

        // if the status panel is defined , redraw coluns 2 and 3
        if (statusPanel != null) {
            // remove columns 2 and 3
            innerLayout.remove(1);
            innerLayout.remove(1);
            statusPanel = new JPanel(new BorderLayout());

            // create a list from the current request saved on the server
            JScrollPane requestList = buildCurrentRequestDisplay(smartHub);
            statusPanel.add(requestList);

            trackingPanel = buildTrackingPanel();

            // add column for showing status of requests
            statusPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(),
                    "Current Request Status",
                    TitledBorder.CENTER,
                    TitledBorder.TOP));

            statusPanel.setOpaque(false);

            // add the columns to the inner layout
            innerLayout.add(statusPanel);
            innerLayout.add(trackingPanel);
        }

        // trigger the systems checks to run in the background
        Empty emptyRequest = Empty.newBuilder().
                build();

        CompletableFuture.runAsync(() -> {
            try {
                // Make the call in the background to make the gui responsive
                smartHub.triggerChecks(emptyRequest);

                System.out.println("System check finished");

            } catch (Exception e) {
                // Print an error message if an error occurred
                System.err.
                        println("Error during 'triggerChecks' background call: " + e.
                                getMessage());
            }
        });
    }
}
