# Zero Hunger

## Prerequisites

* Java Development Kit (JDK) installed.
* IDE installed.

## Build the Project

Before running the services, you must compile the source code to ensure all changes are included.

1. Select the project (ZeroHunger)
2. Select **Clean and Build** from the menu.
3. Wait for the build to complete successfully..

## How to Run from an IDE

After building the project, you must start the three backend services first, followed by the client application. The services rely on each other, so the order is important.

### Step 1: Start the Logistics Server

1. In the NetBeans "Projects" window, navigate to `Source Packages -> services.Logistics`.
2. Right-click on the `LogisticsServer.java` file.
3. Select **Run File** from the menu.
4. You should see output in the console indicating the server has started.

### Step 2: Start the Food Source Server

1. Navigate to `Source Packages -> services.FoodSource`.
2. Right-click on the `FoodSourceServer.java` file.
3. Select **Run File**.
4. A new console tab will open, showing that the Food Source server has started.

### Step 3: Start the Smart Hub Server

1. Navigate to `Source Packages -> services.SmartHub`.
2. Right-click on the `SmartHubServer.java` file.
3. Select **Run File**.
4. A new console tab will open, showing that the Smart Hub server has started.

### Step 4: Start the Client Application

Once all three servers are running and have successfully registered with the network, you can start the client GUI.

1. Navigate to `Source Packages -> client`.
2. Right-click on the `Client.java` file.
3. Select **Run File**.
4. The client GUI should appear, allowing you to interact with the system.
