package rapido;

import java.io.Serializable;
import java.util.Scanner;

// Abstract User class
abstract class User implements Serializable {
    protected String name;
    protected String phone;
    protected String username;
    protected String password;
    protected String role;

    public User(String name, String phone, String username, String password, String role) {
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // Abstract method for dashboard
    public abstract void showDashboard(Scanner scanner, RapidoSystem system);

    @Override
    public String toString() {
        return String.format("Name: %s, Phone: %s, Username: %s, Role: %s", name, phone, username, role);
    }
}