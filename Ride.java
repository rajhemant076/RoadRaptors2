package rapido;

import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;

// Ride class
class Ride implements Serializable {
  private String rideId;
  private String pickupLocation;
  private String dropLocation;
  private double distance;
  private double fare;
  private int eta;
  private String status; // REQUESTED, ONGOING, COMPLETED, CANCELLED
  private Rider rider;
  private Driver driver;
  private Date bookingTime;
  private Date completionTime;
  private String paymentMethod;
  private String upiId;

  public Ride(String pickupLocation, String dropLocation, double distance,
      double fare, int eta, Rider rider) {
    this.rideId = "RIDE" + System.currentTimeMillis();
    this.pickupLocation = pickupLocation;
    this.dropLocation = dropLocation;
    this.distance = distance;
    this.fare = fare;
    this.eta = eta;
    this.rider = rider;
    this.status = "REQUESTED";
    this.bookingTime = new Date();
  }

  // Getters and setters
  public String getRideId() {
    return rideId;
  }

  public String getPickupLocation() {
    return pickupLocation;
  }

  public String getDropLocation() {
    return dropLocation;
  }

  public double getDistance() {
    return distance;
  }

  public double getFare() {
    return fare;
  }

  public int getEta() {
    return eta;
  }

  public String getStatus() {
    return status;
  }

  public Rider getRider() {
    return rider;
  }

  public Driver getDriver() {
    return driver;
  }

  public Date getBookingTime() {
    return bookingTime;
  }

  public Date getCompletionTime() {
    return completionTime;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public String getUpiId() {
    return upiId;
  }

  public void setDriver(Driver driver) {
    this.driver = driver;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setCompletionTime(Date completionTime) {
    this.completionTime = completionTime;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public void setUpiId(String upiId) {
    this.upiId = upiId;
  }

  public String generateReceipt() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    return "\n" +
        "╔══════════════════════════════════════╗\n" +
        "║            RIDE RECEIPT              ║\n" +
        "╠══════════════════════════════════════╣\n" +
        "║ Ride ID: " + String.format("%-25s", rideId) + "║\n" +
        "║ Rider:   " + String.format("%-25s", rider.getName()) + "║\n" +
        "║ Driver:  " + String.format("%-25s", driver != null ? driver.getName() : "N/A") + "║\n" +
        "║ Vehicle: " + String.format("%-25s", driver != null ? driver.getVehicleNo() : "N/A") + "║\n" +
        "║ From:    " + String.format("%-25s", pickupLocation) + "║\n" +
        "║ To:      " + String.format("%-25s", dropLocation) + "║\n" +
        "║ Distance:" + String.format("%-25s", String.format("%.1f km", distance)) + "║\n" +
        "║ Fare:    " + String.format("%-25s", "₹" + String.format("%.2f", fare)) + "║\n" +
        "║ ETA:     " + String.format("%-25s", eta + " minutes") + "║\n" +
        "║ Status:  " + String.format("%-25s", status) + "║\n" +
        "║ Payment: " + String.format("%-25s", paymentMethod != null ? paymentMethod : "Pending") + "║\n" +
        "║ Time:    " + String.format("%-25s", sdf.format(bookingTime)) + "║\n" +
        "╚══════════════════════════════════════╝\n";
  }
}
