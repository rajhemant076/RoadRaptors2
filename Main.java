package rapido;

import java.util.Scanner;

// Main class
public class Main {
  public static void main(String[] args) {
    try {
      RapidoSystem rapidoSystem = new RapidoSystem();
      Scanner scanner = new Scanner(System.in);
      rapidoSystem.showMainMenu(scanner);
      scanner.close();
    } catch (Exception e) {
      System.out.println("An unexpected error occurred: " + e.getMessage());
      e.printStackTrace();
    }
  }
}