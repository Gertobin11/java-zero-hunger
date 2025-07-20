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
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

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

            JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
            mainContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
            mainContainer.setBackground(new Color(240, 240, 240));
            JLabel mainHeading = new JLabel("Zero Hunger - Smart Food Management Service", SwingConstants.CENTER);
            parentFrame.add(mainContainer);
            mainContainer.add(mainHeading);

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
