package rapido;

import java.io.Serializable;
import java.util.*;

// Driver class
class Driver extends User implements Serializable {
  private String vehicleNo;
  private boolean approved;
  private boolean online;
  private double earnings;
  private List<Ride> assignedRides;

  public Driver(String name, String phone, String vehicleNo, String username, String password) {
    super(name, phone, username, password, "DRIVER");
    this.vehicleNo = vehicleNo;
    this.approved = false;
    this.online = false;
    this.earnings = 0.0;
    this.assignedRides = new ArrayList<>();
  }

  // Getters and setters
  public String getVehicleNo() {
    return vehicleNo;
  }

  public boolean isApproved() {
    return approved;
  }

  public boolean isOnline() {
    return online;
  }

  public double getEarnings() {
    return earnings;
  }

  public List<Ride> getAssignedRides() {
    return assignedRides;
  }

  public void setApproved(boolean approved) {
    this.approved = approved;
  }

  public void setOnline(boolean online) {
    this.online = online;
  }

  public void addEarnings(double amount) {
    this.earnings += amount;
  }

  public void addAssignedRide(Ride ride) {
    this.assignedRides.add(ride);
  }

  @Override
  public void showDashboard(Scanner scanner, RapidoSystem system) {
    system.showDriverMenu(scanner, this);
  }

  @Override
  public String toString() {
    return String.format("Name: %s, Phone: %s, Vehicle: %s, Approved: %s, Online: %s, Earnings: ₹%.2f",
        name, phone, vehicleNo, approved ? "Yes" : "No", online ? "Yes" : "No", earnings);
  }
}
