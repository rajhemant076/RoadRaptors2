package rapido;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

// Main System Class
class RapidoSystem implements Serializable {
  private List<User> users;
  private List<Ride> rides;
  private double basePricePerKm;
  private Random random;

  // File names for data persistence
  private static final String DATA_FILE = "rapido_data.dat";

  public RapidoSystem() {
    this.users = new ArrayList<>();
    this.rides = new ArrayList<>();
    this.basePricePerKm = 8.0;
    this.random = new Random();
    loadData();
    initializeDefaultAdmin();
  }

  // File handling methods
  @SuppressWarnings("unchecked")
  private void loadData() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
      RapidoSystem savedSystem = (RapidoSystem) ois.readObject();
      this.users = savedSystem.users;
      this.rides = savedSystem.rides;
      this.basePricePerKm = savedSystem.basePricePerKm;
      System.out.println("Data loaded successfully!");
    } catch (FileNotFoundException e) {
      System.out.println("No existing data file found. Starting fresh...");
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Error loading data: " + e.getMessage());
      // Initialize fresh data if loading fails
      this.users = new ArrayList<>();
      this.rides = new ArrayList<>();
    }
  }

  private void saveData() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
      oos.writeObject(this);
    } catch (IOException e) {
      System.out.println("Error saving data: " + e.getMessage());
    }
  }

  private void initializeDefaultAdmin() {
    boolean adminExists = false;
    for (User user : users) {
      if (user.getRole().equals("ADMIN")) {
        adminExists = true;
        break;
      }
    }
    if (!adminExists) {
      users.add(new Admin("System Admin", "0000000000", "adminhemant", "hemant123"));
      System.out.println("Default admin created: adminhemant/hemant123");
      saveData();
    }
  }

  // User management methods
  public boolean usernameExists(String username) {
    for (User user : users) {
      if (user.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }

  public User login(String username, String password) {
    for (User user : users) {
      if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
        return user;
      }
    }
    return null;
  }

  public void signupRider(String name, String phone, String username, String password) {
    if (usernameExists(username)) {
      System.out.println("Username already exists!");
      return;
    }
    users.add(new Rider(name, phone, username, password));
    saveData();
    System.out.println("Rider registered successfully!");
  }

  public void signupDriver(String name, String phone, String vehicleNo, String username, String password) {
    if (usernameExists(username)) {
      System.out.println("Username already exists!");
      return;
    }
    users.add(new Driver(name, phone, vehicleNo, username, password));
    saveData();
    System.out.println("Driver registered successfully! Waiting for admin approval.");
  }

  // Ride management methods
  public List<Driver> getAvailableDrivers() {
    List<Driver> availableDrivers = new ArrayList<>();
    for (User user : users) {
      if (user instanceof Driver) {
        Driver driver = (Driver) user;
        if (driver.isApproved() && driver.isOnline()) {
          availableDrivers.add(driver);
        }
      }
    }
    return availableDrivers;
  }

  public List<Ride> getRideRequests(Driver driver) {
    List<Ride> requests = new ArrayList<>();
    for (Ride ride : rides) {
      if (ride.getStatus().equals("REQUESTED") && ride.getDriver() == null) {
        requests.add(ride);
      }
    }
    return requests;
  }

  public List<Ride> getDriverRides(Driver driver) {
    List<Ride> driverRides = new ArrayList<>();
    for (Ride ride : rides) {
      if (ride.getDriver() == driver) {
        driverRides.add(ride);
      }
    }
    return driverRides;
  }

  public void createRide(Ride ride) {
    rides.add(ride);
    saveData();
  }

  // Helper method to replace .toList() from streams
  private List<Ride> filterRidesByStatus(String status) {
    List<Ride> filteredRides = new ArrayList<>();
    for (Ride ride : rides) {
      if (ride.getStatus().equals(status)) {
        filteredRides.add(ride);
      }
    }
    return filteredRides;
  }

  private List<Driver> filterUnapprovedDrivers() {
    List<Driver> unapprovedDrivers = new ArrayList<>();
    for (User user : users) {
      if (user instanceof Driver) {
        Driver driver = (Driver) user;
        if (!driver.isApproved()) {
          unapprovedDrivers.add(driver);
        }
      }
    }
    return unapprovedDrivers;
  }

  // Admin methods
  public List<Rider> getRiders() {
    List<Rider> riders = new ArrayList<>();
    for (User user : users) {
      if (user instanceof Rider) {
        riders.add((Rider) user);
      }
    }
    return riders;
  }

  public List<Driver> getDrivers() {
    List<Driver> drivers = new ArrayList<>();
    for (User user : users) {
      if (user instanceof Driver) {
        drivers.add((Driver) user);
      }
    }
    return drivers;
  }

  public void approveDriver(String username) {
    for (User user : users) {
      if (user instanceof Driver && user.getUsername().equals(username)) {
        ((Driver) user).setApproved(true);
        saveData();
        System.out.println("Driver approved successfully!");
        return;
      }
    }
    System.out.println("Driver not found!");
  }

  public void removeUser(String username) {
    for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
      User user = iterator.next();
      if (user.getUsername().equals(username)) {
        iterator.remove();
        saveData();
        System.out.println("User removed successfully!");
        return;
      }
    }
    System.out.println("User not found!");
  }

  // Getters and setters
  public double getBasePricePerKm() {
    return basePricePerKm;
  }

  public void setBasePricePerKm(double basePricePerKm) {
    this.basePricePerKm = basePricePerKm;
    saveData();
  }

  public List<Ride> getRides() {
    return rides;
  }

  public Random getRandom() {
    return random;
  }

  // Menu methods
  public void showMainMenu(Scanner scanner) {
    while (true) {
      System.out.println("\n╔══════════════════════════════════════╗");
      System.out.println("║           RAPIDO SYSTEM              ║");
      System.out.println("╠══════════════════════════════════════╣");
      System.out.println("║ 1. Login                             ║");
      System.out.println("║ 2. Signup                            ║");
      System.out.println("║ 3. Exit                              ║");
      System.out.println("╚══════════════════════════════════════╝");
      System.out.print("Choose an option (1-3): ");

      try {
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
          case 1:
            loginMenu(scanner);
            break;
          case 2:
            signupMenu(scanner);
            break;
          case 3:
            saveData();
            System.out.println("Thank you for using Rapido System!");
            return;
          default:
            System.out.println("Invalid choice! Please try again.");
        }
      } catch (InputMismatchException e) {
        System.out.println("Please enter a valid number!");
        scanner.nextLine(); // clear invalid input
      }
    }
  }

  private void loginMenu(Scanner scanner) {
    System.out.println("\n╔══════════════════════════════════════╗");
    System.out.println("║               LOGIN                  ║");
    System.out.println("╚══════════════════════════════════════╝");

    System.out.print("Username: ");
    String username = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();

    User user = login(username, password);
    if (user != null) {
      System.out.println("Login successful! Welcome, " + user.getName() + "!");
      user.showDashboard(scanner, this);
    } else {
      System.out.println("Invalid username or password!");
    }
  }

  private void signupMenu(Scanner scanner) {
    System.out.println("\n╔══════════════════════════════════════╗");
    System.out.println("║              SIGNUP                  ║");
    System.out.println("╠══════════════════════════════════════╣");
    System.out.println("║ 1. Signup as Rider                   ║");
    System.out.println("║ 2. Signup as Driver                  ║");
    System.out.println("║ 3. Back to Main Menu                 ║");
    System.out.println("╚══════════════════════════════════════╝");
    System.out.print("Choose an option (1-3): ");

    try {
      int choice = scanner.nextInt();
      scanner.nextLine(); // consume newline

      switch (choice) {
        case 1:
          signupRiderMenu(scanner);
          break;
        case 2:
          signupDriverMenu(scanner);
          break;
        case 3:
          return;
        default:
          System.out.println("Invalid choice!");
      }
    } catch (InputMismatchException e) {
      System.out.println("Please enter a valid number!");
      scanner.nextLine();
    }
  }

  private void signupRiderMenu(Scanner scanner) {
    System.out.println("\n╔══════════════════════════════════════╗");
    System.out.println("║           RIDER SIGNUP               ║");
    System.out.println("╚══════════════════════════════════════╝");

    System.out.print("Name: ");
    String name = scanner.nextLine();
    System.out.print("Phone: ");
    String phone = scanner.nextLine();
    System.out.print("Username: ");
    String username = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();

    signupRider(name, phone, username, password);
  }

  private void signupDriverMenu(Scanner scanner) {
    System.out.println("\n╔══════════════════════════════════════╗");
    System.out.println("║           DRIVER SIGNUP              ║");
    System.out.println("╚══════════════════════════════════════╝");

    System.out.print("Name: ");
    String name = scanner.nextLine();
    System.out.print("Phone: ");
    String phone = scanner.nextLine();
    System.out.print("Vehicle Number: ");
    String vehicleNo = scanner.nextLine();
    System.out.print("Username: ");
    String username = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();

    signupDriver(name, phone, vehicleNo, username, password);
  }

  // Rider Menu
  public void showRiderMenu(Scanner scanner, Rider rider) {
    while (true) {
      System.out.println("\n╔══════════════════════════════════════╗");
      System.out.println("║           RIDER DASHBOARD            ║");
      System.out.println("╠══════════════════════════════════════╣");
      System.out.println("║ 1. Book a Ride                       ║");
      System.out.println("║ 2. View Nearby Drivers               ║");
      System.out.println("║ 3. Make Payment                      ║");
      System.out.println("║ 4. Ride History                      ║");
      System.out.println("║ 5. Logout                            ║");
      System.out.println("╚══════════════════════════════════════╝");
      System.out.print("Choose an option (1-5): ");

      try {
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
          case 1:
            bookRideMenu(scanner, rider);
            break;
          case 2:
            viewNearbyDrivers();
            break;
          case 3:
            makePaymentMenu(scanner, rider);
            break;
          case 4:
            viewRideHistory(rider);
            break;
          case 5:
            System.out.println("Logging out...");
            return;
          default:
            System.out.println("Invalid choice!");
        }
      } catch (InputMismatchException e) {
        System.out.println("Please enter a valid number!");
        scanner.nextLine();
      }
    }
  }

  private void bookRideMenu(Scanner scanner, Rider rider) {
    System.out.println("\n╔══════════════════════════════════════╗");
    System.out.println("║            BOOK A RIDE               ║");
    System.out.println("╚══════════════════════════════════════╝");

    System.out.print("Enter pickup location: ");
    String pickup = scanner.nextLine();
    System.out.print("Enter drop location: ");
    String drop = scanner.nextLine();

    // Generate random ride details
    double distance = Math.round((random.nextDouble() * 9 + 1) * 10.0) / 10.0;
    double fare = distance * basePricePerKm;
    int eta = random.nextInt(9) + 2; // 2-10 minutes

    System.out.println("\nRide Details:");
    System.out.println("Distance: " + distance + " km");
    System.out.println("Fare: ₹" + String.format("%.2f", fare));
    System.out.println("ETA: " + eta + " minutes");

    // Show available drivers
    List<Driver> availableDrivers = getAvailableDrivers();
    if (availableDrivers.isEmpty()) {
      System.out.println("No drivers available at the moment!");
      return;
    }

    System.out.println("\nAvailable Drivers:");
    for (int i = 0; i < availableDrivers.size(); i++) {
      Driver driver = availableDrivers.get(i);
      System.out.printf("%d. %s - %s (%.1f km away)\n",
          i + 1, driver.getName(), driver.getVehicleNo(),
          random.nextDouble() * 5 + 0.5);
    }

    System.out.print("Select driver (1-" + availableDrivers.size() + "): ");
    try {
      int driverChoice = scanner.nextInt();
      scanner.nextLine();

      if (driverChoice < 1 || driverChoice > availableDrivers.size()) {
        System.out.println("Invalid driver selection!");
        return;
      }

      Driver selectedDriver = availableDrivers.get(driverChoice - 1);
      Ride ride = new Ride(pickup, drop, distance, fare, eta, rider);
      ride.setDriver(selectedDriver);
      ride.setStatus("ONGOING");
      createRide(ride);
      rider.addRideToHistory(ride);
      selectedDriver.addAssignedRide(ride);

      System.out.println("Ride booked successfully!");
      System.out.println(ride.generateReceipt());

    } catch (InputMismatchException e) {
      System.out.println("Please enter a valid number!");
      scanner.nextLine();
    }
  }

  private void viewNearbyDrivers() {
    List<Driver> availableDrivers = getAvailableDrivers();

    if (availableDrivers.isEmpty()) {
      System.out.println("No drivers available nearby!");
      return;
    }

    System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
    System.out.println("║                      NEARBY DRIVERS                             ║");
    System.out.println("╠══════════════════════════════════════════════════════════════════╣");

    for (int i = 0; i < availableDrivers.size(); i++) {
      Driver driver = availableDrivers.get(i);
      double distance = Math.round((random.nextDouble() * 5 + 0.5) * 10.0) / 10.0;
      System.out.printf("%d. %s - %s | %.1f km away | Rating: %.1f ⭐\n",
          i + 1, driver.getName(), driver.getVehicleNo(), distance, 4.0 + random.nextDouble());
    }
    System.out.println("╚══════════════════════════════════════════════════════════════════╝");
  }

  private void makePaymentMenu(Scanner scanner, Rider rider) {
    List<Ride> ongoingRides = new ArrayList<>();
    for (Ride ride : rides) {
      if (ride.getRider() == rider && ride.getStatus().equals("ONGOING")) {
        ongoingRides.add(ride);
      }
    }

    if (ongoingRides.isEmpty()) {
      System.out.println("No ongoing rides found!");
      return;
    }

    System.out.println("\nOngoing Rides:");
    for (int i = 0; i < ongoingRides.size(); i++) {
      Ride ride = ongoingRides.get(i);
      System.out.printf("%d. %s to %s - ₹%.2f\n",
          i + 1, ride.getPickupLocation(), ride.getDropLocation(), ride.getFare());
    }

    System.out.print("Select ride to pay (1-" + ongoingRides.size() + "): ");
    try {
      int rideChoice = scanner.nextInt();
      scanner.nextLine();

      if (rideChoice < 1 || rideChoice > ongoingRides.size()) {
        System.out.println("Invalid selection!");
        return;
      }

      Ride selectedRide = ongoingRides.get(rideChoice - 1);

      System.out.println("\nPayment Methods:");
      System.out.println("1. UPI");
      System.out.println("2. Cash");
      System.out.println("3. Wallet");
      System.out.print("Choose payment method (1-3): ");

      int paymentChoice = scanner.nextInt();
      scanner.nextLine();

      String paymentMethod = "";
      String upiId = "";

      switch (paymentChoice) {
        case 1:
          paymentMethod = "UPI";
          System.out.print("Enter UPI ID: ");
          upiId = scanner.nextLine();
          break;
        case 2:
          paymentMethod = "Cash";
          break;
        case 3:
          paymentMethod = "Wallet";
          break;
        default:
          System.out.println("Invalid choice!");
          return;
      }

      selectedRide.setPaymentMethod(paymentMethod);
      selectedRide.setUpiId(upiId);
      selectedRide.setStatus("COMPLETED");
      selectedRide.setCompletionTime(new Date());

      // Add earnings to driver
      if (selectedRide.getDriver() != null) {
        selectedRide.getDriver().addEarnings(selectedRide.getFare());
      }

      saveData();
      System.out.println("Payment successful!");
      System.out.println(selectedRide.generateReceipt());

    } catch (InputMismatchException e) {
      System.out.println("Please enter a valid number!");
      scanner.nextLine();
    }
  }

  private void viewRideHistory(Rider rider) {
    List<Ride> history = rider.getRideHistory();

    if (history.isEmpty()) {
      System.out.println("No ride history found!");
      return;
    }

    System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════╗");
    System.out.println("║                                RIDE HISTORY                                      ║");
    System.out.println("╠══════════════════════════════════════════════════════════════════════════════════╣");

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    for (int i = 0; i < history.size(); i++) {
      Ride ride = history.get(i);
      System.out.printf("║ %-2d. %-12s | %-15s | %-8.1fkm | ₹%-6.2f | %-10s | %-16s ║\n",
          i + 1, ride.getRideId(),
          ride.getDriver() != null ? ride.getDriver().getName() : "N/A",
          ride.getDistance(), ride.getFare(), ride.getStatus(),
          sdf.format(ride.getBookingTime()));
    }
    System.out.println("╚══════════════════════════════════════════════════════════════════════════════════╝");
  }

  // Driver Menu
  public void showDriverMenu(Scanner scanner, Driver driver) {
    while (true) {
      System.out.println("\n╔══════════════════════════════════════╗");
      System.out.println("║           DRIVER DASHBOARD           ║");
      System.out.println("╠══════════════════════════════════════╣");
      System.out.println("║ 1. Go Online/Offline                 ║");
      System.out.println("║ 2. View Ride Requests                ║");
      System.out.println("║ 3. Accept Ride                       ║");
      System.out.println("║ 4. Mark Ride as Completed            ║");
      System.out.println("║ 5. View Earnings                     ║");
      System.out.println("║ 6. Logout                            ║");
      System.out.println("╚══════════════════════════════════════╝");
      System.out.print("Choose an option (1-6): ");

      try {
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
          case 1:
            toggleOnlineStatus(scanner, driver);
            break;
          case 2:
            viewRideRequests(driver);
            break;
          case 3:
            acceptRideMenu(scanner, driver);
            break;
          case 4:
            markRideCompletedMenu(scanner, driver);
            break;
          case 5:
            viewEarnings(driver);
            break;
          case 6:
            System.out.println("Logging out...");
            return;
          default:
            System.out.println("Invalid choice!");
        }
      } catch (InputMismatchException e) {
        System.out.println("Please enter a valid number!");
        scanner.nextLine();
      }
    }
  }

  private void toggleOnlineStatus(Scanner scanner, Driver driver) {
    if (!driver.isApproved()) {
      System.out.println("You are not approved by admin yet!");
      return;
    }

    driver.setOnline(!driver.isOnline());
    saveData();
    System.out.println("You are now " + (driver.isOnline() ? "ONLINE" : "OFFLINE"));
  }

  private void viewRideRequests(Driver driver) {
    if (!driver.isApproved() || !driver.isOnline()) {
      System.out.println("You need to be approved and online to view ride requests!");
      return;
    }

    List<Ride> requests = getRideRequests(driver);

    if (requests.isEmpty()) {
      System.out.println("No ride requests available!");
      return;
    }

    System.out.println("\nRide Requests:");
    for (int i = 0; i < requests.size(); i++) {
      Ride ride = requests.get(i);
      System.out.printf("%d. %s to %s - ₹%.2f - %.1f km\n",
          i + 1, ride.getPickupLocation(), ride.getDropLocation(),
          ride.getFare(), ride.getDistance());
    }
  }

  private void acceptRideMenu(Scanner scanner, Driver driver) {
    if (!driver.isApproved() || !driver.isOnline()) {
      System.out.println("You need to be approved and online to accept rides!");
      return;
    }

    List<Ride> requests = getRideRequests(driver);

    if (requests.isEmpty()) {
      System.out.println("No ride requests available!");
      return;
    }

    System.out.println("\nAvailable Ride Requests:");
    for (int i = 0; i < requests.size(); i++) {
      Ride ride = requests.get(i);
      System.out.printf("%d. %s to %s - ₹%.2f - %.1f km\n",
          i + 1, ride.getPickupLocation(), ride.getDropLocation(),
          ride.getFare(), ride.getDistance());
    }

    System.out.print("Select ride to accept (1-" + requests.size() + "): ");
    try {
      int rideChoice = scanner.nextInt();
      scanner.nextLine();

      if (rideChoice < 1 || rideChoice > requests.size()) {
        System.out.println("Invalid selection!");
        return;
      }

      Ride selectedRide = requests.get(rideChoice - 1);
      selectedRide.setDriver(driver);
      selectedRide.setStatus("ONGOING");
      driver.addAssignedRide(selectedRide);
      saveData();

      System.out.println("Ride accepted successfully!");
      System.out.println(selectedRide.generateReceipt());

    } catch (InputMismatchException e) {
      System.out.println("Please enter a valid number!");
      scanner.nextLine();
    }
  }

  private void markRideCompletedMenu(Scanner scanner, Driver driver) {
    List<Ride> ongoingRides = new ArrayList<>();
    for (Ride ride : getDriverRides(driver)) {
      if (ride.getStatus().equals("ONGOING")) {
        ongoingRides.add(ride);
      }
    }

    if (ongoingRides.isEmpty()) {
      System.out.println("No ongoing rides found!");
      return;
    }

    System.out.println("\nOngoing Rides:");
    for (int i = 0; i < ongoingRides.size(); i++) {
      Ride ride = ongoingRides.get(i);
      System.out.printf("%d. %s to %s - ₹%.2f\n",
          i + 1, ride.getPickupLocation(), ride.getDropLocation(), ride.getFare());
    }

    System.out.print("Select ride to mark as completed (1-" + ongoingRides.size() + "): ");
    try {
      int rideChoice = scanner.nextInt();
      scanner.nextLine();

      if (rideChoice < 1 || rideChoice > ongoingRides.size()) {
        System.out.println("Invalid selection!");
        return;
      }

      Ride selectedRide = ongoingRides.get(rideChoice - 1);
      selectedRide.setStatus("COMPLETED");
      selectedRide.setCompletionTime(new Date());
      driver.addEarnings(selectedRide.getFare());
      saveData();

      System.out.println("Ride marked as completed!");
      System.out.println(selectedRide.generateReceipt());

    } catch (InputMismatchException e) {
      System.out.println("Please enter a valid number!");
      scanner.nextLine();
    }
  }

  private void viewEarnings(Driver driver) {
    System.out.println("\n╔══════════════════════════════════════╗");
    System.out.println("║              EARNINGS                ║");
    System.out.println("╠══════════════════════════════════════╣");
    System.out.println("║ Total Earnings: ₹" + String.format("%-18.2f", driver.getEarnings()) + "║");
    System.out.println("║ Approved: " + String.format("%-25s", driver.isApproved() ? "Yes" : "No") + "║");
    System.out.println("║ Online: " + String.format("%-26s", driver.isOnline() ? "Yes" : "No") + "║");
    System.out.println("║ Total Rides: " + String.format("%-22d", driver.getAssignedRides().size()) + "║");
    System.out.println("╚══════════════════════════════════════╝");
  }

  // Admin Menu
  public void showAdminMenu(Scanner scanner, Admin admin) {
    while (true) {
      System.out.println("\n╔══════════════════════════════════════╗");
      System.out.println("║           ADMIN DASHBOARD            ║");
      System.out.println("╠══════════════════════════════════════╣");
      System.out.println("║ 1. View all Riders                   ║");
      System.out.println("║ 2. View all Drivers                  ║");
      System.out.println("║ 3. View all Rides                    ║");
      System.out.println("║ 4. Change Base Price Per KM          ║");
      System.out.println("║ 5. Approve Drivers                   ║");
      System.out.println("║ 6. Remove User                       ║");
      System.out.println("║ 7. Logout                            ║");
      System.out.println("╚══════════════════════════════════════╝");
      System.out.print("Choose an option (1-7): ");

      try {
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
          case 1:
            viewAllRiders();
            break;
          case 2:
            viewAllDrivers();
            break;
          case 3:
            viewAllRides();
            break;
          case 4:
            changeBasePriceMenu(scanner);
            break;
          case 5:
            approveDriverMenu(scanner);
            break;
          case 6:
            removeUserMenu(scanner);
            break;
          case 7:
            System.out.println("Logging out...");
            return;
          default:
            System.out.println("Invalid choice!");
        }
      } catch (InputMismatchException e) {
        System.out.println("Please enter a valid number!");
        scanner.nextLine();
      }
    }
  }

  private void viewAllRiders() {
    List<Rider> riders = getRiders();

    if (riders.isEmpty()) {
      System.out.println("No riders found!");
      return;
    }

    System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
    System.out.println("║                          ALL RIDERS                              ║");
    System.out.println("╠══════════════════════════════════════════════════════════════════╣");

    for (int i = 0; i < riders.size(); i++) {
      Rider rider = riders.get(i);
      System.out.printf("%d. %s | %s | %s | Rides: %d\n",
          i + 1, rider.getName(), rider.getPhone(),
          rider.getUsername(), rider.getRideHistory().size());
    }
    System.out.println("╚══════════════════════════════════════════════════════════════════╝");
  }

  private void viewAllDrivers() {
    List<Driver> drivers = getDrivers();

    if (drivers.isEmpty()) {
      System.out.println("No drivers found!");
      return;
    }

    System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════╗");
    System.out.println("║                                 ALL DRIVERS                                      ║");
    System.out.println("╠══════════════════════════════════════════════════════════════════════════════════╣");

    for (int i = 0; i < drivers.size(); i++) {
      Driver driver = drivers.get(i);
      System.out.printf("%d. %s | %s | %s | Approved: %s | Online: %s | Earnings: ₹%.2f\n",
          i + 1, driver.getName(), driver.getPhone(), driver.getVehicleNo(),
          driver.isApproved() ? "Yes" : "No", driver.isOnline() ? "Yes" : "No",
          driver.getEarnings());
    }
    System.out.println("╚══════════════════════════════════════════════════════════════════════════════════╝");
  }

  private void viewAllRides() {
    if (rides.isEmpty()) {
      System.out.println("No rides found!");
      return;
    }

    System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════╗");
    System.out.println("║                                  ALL RIDES                                       ║");
    System.out.println("╠══════════════════════════════════════════════════════════════════════════════════╣");

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    for (int i = 0; i < rides.size(); i++) {
      Ride ride = rides.get(i);
      System.out.printf("%d. %s | %s → %s | ₹%.2f | %s | %s\n",
          i + 1, ride.getRideId(), ride.getPickupLocation(), ride.getDropLocation(),
          ride.getFare(), ride.getStatus(), sdf.format(ride.getBookingTime()));
    }
    System.out.println("╚══════════════════════════════════════════════════════════════════════════════════╝");
  }

  private void changeBasePriceMenu(Scanner scanner) {
    System.out.println("\nCurrent base price per km: ₹" + basePricePerKm);
    System.out.print("Enter new base price per km: ");

    try {
      double newPrice = scanner.nextDouble();
      scanner.nextLine();

      if (newPrice <= 0) {
        System.out.println("Price must be positive!");
        return;
      }

      setBasePricePerKm(newPrice);
      System.out.println("Base price updated successfully!");

    } catch (InputMismatchException e) {
      System.out.println("Please enter a valid number!");
      scanner.nextLine();
    }
  }

  private void approveDriverMenu(Scanner scanner) {
    List<Driver> unapprovedDrivers = filterUnapprovedDrivers();

    if (unapprovedDrivers.isEmpty()) {
      System.out.println("No drivers pending approval!");
      return;
    }

    System.out.println("\nDrivers Pending Approval:");
    for (int i = 0; i < unapprovedDrivers.size(); i++) {
      Driver driver = unapprovedDrivers.get(i);
      System.out.printf("%d. %s | %s | %s\n",
          i + 1, driver.getName(), driver.getVehicleNo(), driver.getUsername());
    }

    System.out.print("Select driver to approve (1-" + unapprovedDrivers.size() + "): ");
    try {
      int driverChoice = scanner.nextInt();
      scanner.nextLine();

      if (driverChoice < 1 || driverChoice > unapprovedDrivers.size()) {
        System.out.println("Invalid selection!");
        return;
      }

      Driver selectedDriver = unapprovedDrivers.get(driverChoice - 1);
      approveDriver(selectedDriver.getUsername());

    } catch (InputMismatchException e) {
      System.out.println("Please enter a valid number!");
      scanner.nextLine();
    }
  }

  private void removeUserMenu(Scanner scanner) {
    System.out.print("Enter username to remove: ");
    String username = scanner.nextLine();

    if (username.equals("admin")) {
      System.out.println("Cannot remove admin user!");
      return;
    }

    removeUser(username);
  }
}