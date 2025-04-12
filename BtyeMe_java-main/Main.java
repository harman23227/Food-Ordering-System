import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static final String customerFilePath = "customers.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Admin admin = new Admin();
        Customer customer = new Customer();
        Item item1 = new Item("A",10.00,100,"Snack");
        Item item2 = new Item("B",20.00,100,"Snack");
        Item item3 = new Item("C",25.00,100,"Drink");
        Item item4 = new Item("D",100.00,100,"Food Item");
        Admin.itemsList.add(item1);
        Admin.itemsList.add(item2);
        Admin.itemsList.add(item3);
        Admin.itemsList.add(item4);

        String adminUsername = "123";
        String adminPassword = "123";


        System.out.println("WELCOME TO BYTE ME");
        while (true) {
            System.out.println("Pick an interface: ");
            System.out.println("1. Admin Interface\n2. Customer Interface\n3. Exit application");
            int interfaceChoice = sc.nextInt();
            sc.nextLine();
            if (interfaceChoice == 1) {
                System.out.println("Login to access admin interface: ");
                System.out.println("Enter username: ");
                String enteredAdminUsername = sc.nextLine();
                System.out.println("Enter password: ");
                String enteredAdminPassword = sc.nextLine();
                if (Objects.equals(enteredAdminUsername, adminUsername) && Objects.equals(enteredAdminPassword, adminPassword)) {
                    System.out.println("Welcome to admin interface!");
                    admin.menu();
                } else {
                    System.out.println("Wrong credentials!");
                    return;
                }
            } else if (interfaceChoice == 2) {
                System.out.println("1. Register\n2. Login");
                int customerSignInChoice = sc.nextInt();
                sc.nextLine();
                if (customerSignInChoice == 1) {
                    System.out.println("Enter your email: ");
                    String enteredCustomerEmail = sc.nextLine();;
                    System.out.println("Enter a username: ");
                    String enteredCustomerUsername = sc.nextLine();
                    System.out.println("Enter a password: ");
                    String enteredCustomerPassword = sc.nextLine();
                    Customer customer1 = new Customer(enteredCustomerUsername,enteredCustomerEmail,enteredCustomerPassword);
                    saveCustomerToFile(customer1, customerFilePath);
                    System.out.println("You've been successfully registered!");
                    continue;
                } else {
                    System.out.println("Enter your username: ");
                    String enteredUsername = sc.nextLine();
                    System.out.println("Enter password: ");
                    String enteredPassword = sc.nextLine();

                    Customer loggedInCustomer = findCustomerFromFile(enteredUsername,enteredPassword , customerFilePath);

                    if (loggedInCustomer != null) {
                        System.out.println("Welcome to Customer interface!");
                        customer.menu();
                    } else {
                        System.out.println("Invalid credentials!");

                    }

                }
            } else if (interfaceChoice == 3) {
                System.out.println("Exiting application...");
                break;
            }
        }
    }
    public static void saveCustomerToFile(Customer customer, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(customer.getUsername() + "," +
                    customer.getEmail() + "," +
                    customer.getPassword());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }

    // Retrieve customer details from file
    public static Customer findCustomerFromFile(String username, String password, String filePath) {
        Customer customer = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(username) && data[2].equals(password)) {
                    customer = new Customer(data[0], data[1], data[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customer file: " + e.getMessage());
        }
        return customer;
    }

}