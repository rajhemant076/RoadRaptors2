package rapido;

import java.io.Serializable;
import java.util.*;

// Rider class
class Rider extends User implements Serializable {
  private List<Ride> rideHistory;

  public Rider(String name, String phone, String username, String password) {
    super(name, phone, username, password, "RIDER");
    this.rideHistory = new ArrayList<>();
  }

  public void addRideToHistory(Ride ride) {
    rideHistory.add(ride);
  }

  public List<Ride> getRideHistory() {
    return rideHistory;
  }

  @Override
  public void showDashboard(Scanner scanner, RapidoSystem system) {
    system.showRiderMenu(scanner, this);
  }
}