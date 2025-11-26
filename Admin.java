package rapido;

import java.io.Serializable;
import java.util.Scanner;

// Admin class
class Admin extends User implements Serializable {
  public Admin(String name, String phone, String username, String password) {
    super(name, phone, username, password, "ADMIN");
  }

  @Override
  public void showDashboard(Scanner scanner, RapidoSystem system) {
    system.showAdminMenu(scanner, this);
  }
}