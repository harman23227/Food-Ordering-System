import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

class Reviews{
    String review;
    static ArrayList<Reviews> reviews = new ArrayList<>();
    Reviews(String review){
        this.review = review;
        reviews.add(this);
    }

    @Override
    public String toString() {
        return "Review: " + review ;
    }
}

// CUSTOMER INTERFACE
class Orders {
    int uid;
    static int nextId = 1;
    boolean isVip;
    String orderStatus;
    String specialRequests;


    public Orders(boolean isVip, String specialRequests) {
        this.uid = nextId;
        this.isVip = isVip;
        Customer.orders.add(this);
        this.orderStatus = "Order Placed";
        this.specialRequests = specialRequests;
        nextId++;
    }

    @Override
    public String toString() {
        if (isVip) {
            return "Order ID: " + uid + " Order status: " + orderStatus + " *VIP ORDER* " + " Special Request: " + specialRequests;
        } else {
            return "Order ID: " + uid + " Order status: " + orderStatus + " Special Request: " + specialRequests;
        }
    }
}
class SortingByPrice implements Comparator<Item>{
    @Override
    public int compare(Item i1, Item i2) {
        return Double.compare(i1.price,i2.price);
    }
}

public class Customer {
    String username;
    String password;
    String email;
    boolean isVip;
    static ArrayList<Customer> registeredCustomers = new ArrayList<>();
    double amountToPay = 0;
    HashMap<Item, Integer> cart = new HashMap<>();

    Customer(String username,String email,String password){
        this.username = username;
        this.email = email;
        this.password = password;
        registeredCustomers.add(this);
    }
    Customer(){}

    Admin admin = new Admin();
    Scanner sc = new Scanner(System.in);

    static PriorityQueue<Orders> orders = new PriorityQueue<>(new Comparator<Orders>() {
        @Override
        public int compare(Orders o1, Orders o2) {
            if (o1.isVip && !o2.isVip)
                return -1;
            else if (!o1.isVip && o2.isVip) {
                return 1;
            } else {
                return 0;
            }
        }
    });

    public String getUsername() {
        return username;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    public String placeOrder(String itemName, int quantity) {
        for (Item item : Admin.itemsList) {
            if (Objects.equals(item.name, itemName)) {
                if (item.quantity == 0) {
                    return "Item is out of stock and cannot be ordered.";
                }
                if (item.quantity < quantity) {
                    return "Insufficient stock available.";
                }
                // Deduct the quantity from the inventory
                item.quantity -= quantity;
                Admin.totalSale += item.price * quantity;
                return "Order placed successfully!";
            }
        }
        return "Item not found in the inventory.";
    }

    public void saveOrderHistory() {
        try (FileWriter writer = new FileWriter(this.username + "_orders.txt", true)) {
            writer.write("Order ID: " + (Orders.nextId - 1) + "\n");
            writer.write("Items:\n");
            for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
                writer.write("  - " + entry.getKey().name + " x " + entry.getValue() +
                        " @ Rs." + entry.getKey().price + "\n");
            }
            writer.write("Total: Rs." + amountToPay + "\n");
            writer.write("Special Request: " + (isVip ? "Yes (VIP)" : "No") + "\n");
            writer.write("----------------------------------------------------\n");
            System.out.println("Order history saved!");
        } catch (IOException e) {
            System.out.println("Error saving order history: " + e.getMessage());
        }
    }

    public void viewOrderHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(username + "_orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No order history found for " + username + ".");
        } catch (IOException e) {
            System.out.println("Error reading order history: " + e.getMessage());
        }
    }

    private void checkout() {
        System.out.println("Your cart is: ");
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            System.out.println(entry.getKey().name + " x " + entry.getValue());
        }
        System.out.println("Do you have any additional requests with the order? (y/n)");
        char specialRequestChoice = sc.nextLine().charAt(0);
        String specialRequest = null;
        if (specialRequestChoice == 'y') {
            System.out.println("Write your request: ");
            specialRequest = sc.nextLine();
        }

        System.out.println("Do you want to become a VIP customer for Rs. 150 to get faster delivery? (y/n)");
        char vipChoice = sc.nextLine().charAt(0);
        if (vipChoice == 'y') {
            amountToPay += 150;
            isVip = true;
        } else {
            isVip = false;
        }

        System.out.print("Your total payable amount is: Rs." + amountToPay + "\n");
        Admin.totalSale += amountToPay;

        System.out.println("Select a payment method:");
        System.out.println("1. Cash on Delivery\n2. UPI\n3. Credit/Debit Card");
        int inputMethod = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter your address: ");
        String address = sc.nextLine();

        System.out.println("Your order has been placed!");
        System.out.println("Order ID: " + (Orders.nextId - 1));
        Orders order = new Orders(isVip, specialRequest);
        orders.add(order);

        saveOrderHistory(); // Save to file
        cart.clear(); // Clear cart after checkout
        amountToPay = 0; // Reset payable amount
    }


    @Override
    public String toString() {
        return "Customer{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void browseMenu() {
        System.out.println("Select what you want to do");
        char choice = 'y';
        while (choice == 'y') {
            System.out.println("1. View all items.\n2. Search\n3. Filter by category\n4. Sort by price");
            int bigInput = sc.nextInt();
            sc.nextLine();
            if (bigInput == 1) {
                SwingUtilities.invokeLater(() -> {
                    CanteenGUI gui = new CanteenGUI();
                    gui.setVisible(true);
                });
            } else if (bigInput == 2) {
                System.out.println("Enter name of item you want to lookup: ");
                String searchEntry = sc.nextLine();
                for (Item item : Admin.itemsList) {
                    if (item.name.contains(searchEntry)) {
                        System.out.println(item);
                    }
                }
            } else if (bigInput == 3) {
                System.out.println("Pick category of item: ");
                System.out.println("1. Food Item\n2. Drink\n3. Snack");
                int categoryInput = sc.nextInt();
                sc.nextLine();
                for (Item item : Admin.itemsList) {
                    if (categoryInput == 1) {
                        if (Objects.equals(item.category, "Food Item")) {
                            System.out.println(item);
                        }
                    } else if (categoryInput == 2) {
                        if (Objects.equals(item.category, "Drink")) {
                            System.out.println(item);
                        }
                    } else if (categoryInput == 3) {
                        if (Objects.equals(item.category, "Snack")) {
                            System.out.println(item);

                        }
                    }
                }
            } else if (bigInput == 4) {
                ArrayList<Item> itemsListCopy = new ArrayList<>(Admin.itemsList);
                itemsListCopy.sort(new SortingByPrice());
                for (Item item: itemsListCopy) {
                    System.out.println(item);
                }
            }
            System.out.println("Do you want to redo? (y/n)"); //  change the msg later
            choice = sc.next().charAt(0);
            sc.nextLine();
        }
    }

    public void cartOperations() {
        String specialRequest = null;
        int itemQty = 0;
        char choice = 'y';
        while (choice == 'y') {
            System.out.println("Select what cart operation you want to do: ");
            System.out.println("1. Add items\n2. Modify quantity\n3. Remove items\n4. View total\n5. Checkout");
            int cartChoice = sc.nextInt();
            sc.nextLine();
            switch (cartChoice) {
                case 1:
                    char itemAddChoice = 'y';
                    while (itemAddChoice == 'y') {
                        System.out.println("Enter name and quantity of items you want to add. ");
                        admin.displayItems();
                        System.out.println("Name: ");
                        String itemName = sc.nextLine();
                        System.out.println("Quantity: ");
                        itemQty = sc.nextInt();
                        sc.nextLine();
                        for (Item item : Admin.itemsList) {
                            if (!(itemQty <= item.quantity)) {
                                System.out.println("Not in stock try different quantity.");
                            }
                            if (Objects.equals(itemName, item.name) && itemQty <= item.quantity) {
                                cart.put(item, itemQty);
                                item.quantity -= itemQty;
                                System.out.println("Item Added!!");
                            }
                        }
                        int flag = 0;
                        for (Item item : Admin.itemsList) {
                            if (!(Objects.equals(itemName, item.name))) {
                                flag++;
                            }
                        }
                        if (flag == Admin.itemsList.size()) {
                            System.out.println("Item not found");
                        }
                        System.out.println("Do you want to add more? (y/n)");
                        itemAddChoice = sc.nextLine().charAt(0);
                    }
                    break;
                case 2:
                    System.out.println("Pick an item from your cart to modify its quantity: ");
                    for (int i = 0; i < cart.size(); i++) {
                        System.out.print(cart.keySet().toArray()[i]);
                        System.out.println("\tOrder quantity: " + cart.values().toArray()[i]);
                    }
                    String modifyInput = sc.nextLine();
                    for (Item item : cart.keySet()) { //todo
                        if (Objects.equals(item.name, modifyInput)) {
                            System.out.println("Enter new quantity: ");
                            int qtyInput = sc.nextInt();
                            cart.put(item, qtyInput);
                            sc.nextLine();
                        }
                    }
                    for (int i = 0; i < cart.size(); i++) {
                        System.out.print(cart.keySet().toArray()[i]);
                        System.out.println("\tOrder quantity: " + cart.values().toArray()[i]);
                    }
                    break;
                case 3:
                    char redoChoice = 'y';
                    while (redoChoice == 'y') {
                        System.out.println("Enter name of item you want to remove from cart: ");
                        String itemToRemove = sc.nextLine();
                        for (Item item : cart.keySet()) {
                            if (Objects.equals(item.name, itemToRemove)) {
                                cart.remove(item);
                            }
                        }
                        System.out.println("Item removed successfully! ");
                        System.out.println("Cart: ");
                        for (int i = 0; i < cart.size(); i++) {
                            System.out.print(cart.keySet().toArray()[i]);
                            System.out.println("\tOrder quantity: " + cart.values().toArray()[i]);
                        }
                        System.out.println("DO you want to remove any more items?(y/n)");
                        redoChoice = sc.nextLine().charAt(0);
                    }
                    break;
                case 4:
                    System.out.print("Your total payable amount is: "); // item price * quantity | iterate through the values of cart to get price of each item in the cart then multiply it its respective quantity
                    for (Item item : cart.keySet()) {
                        amountToPay += item.price * cart.get(item);
                    }
                    System.out.println("Rs. " + amountToPay);
                    break;
                case 5:
                    checkout();
                    break;

                default:
                    System.out.println("Wrong input");
                    break;
            }
            System.out.println("Do you want to do more cart operations?(y/n)");
            choice = sc.nextLine().charAt(0);
        }
    }

    public void orderTracking() {
        char choice = 'y';
        while (choice == 'y') {
            System.out.println("1. View order status\n2.Cancel Order\n3. Order history");
            int bigInput = sc.nextInt();
            sc.nextLine();
            switch (bigInput) {
                case 1:
                    System.out.println("Enter your order id to view it's status: ");
                    int statusCheckerID = sc.nextInt();
                    sc.nextLine();
                    for (Orders orders : orders) {
                        if (statusCheckerID == orders.uid) {
                            System.out.println("Your order status is: " + orders.orderStatus);
                        }
                    }
                    break;
                case 2:
                    System.out.println("Enter your order ID to cancel it: ");
                    int cancellationID = sc.nextInt();
                    sc.nextLine();
                    for (Orders orders : orders) {
                        if (cancellationID == orders.uid) {
                            orders.orderStatus = "Cancelled";
                        }
                    }
                    System.out.println("Your order has been cancelled!");
                    break;
                case 3:
                    System.out.println("Your order history is: ");
                    for (Orders orders : orders) {
                        if (Objects.equals(orders.orderStatus, "Delivered")) {
                            System.out.println(orders);
                        }
                    }
            }
            System.out.println("Do you want to redo order tracking operations? (y/n)");
            choice = sc.nextLine().charAt(0);
        }
    }

    public void submitReviews() {
        System.out.println("Enter your review of the order: ");
        String review = sc.nextLine();
        Reviews reviews = new Reviews(review);
        System.out.println("Review submitted! Thanks for your feedback!");
    }
    public void readReviews() {
        for (Reviews reviews : Reviews.reviews) {
            System.out.println(reviews);
        }
    }

    public void menu() {
        System.out.println("Select what you want to do: ");
        char redoChoice = 'y';
        while (redoChoice == 'y') {
            System.out.println("1. Browse Menu.\n" +
                    "2. Cart operations.\n" +
                    "3. Order Tracking.\n" +
                    "4. Give review.\n" +
                    "5. Review Section.\n" +
                    "6. View Saved Order History.\n" +
                    "7. Exit Customer interface");
            int customerMenuChoice = sc.nextInt();
            sc.nextLine();
            switch (customerMenuChoice) {
                case 1:
                    browseMenu();
                    break;
                case 2:
                    cartOperations();
                    break;
                case 3:
                    orderTracking();
                    break;
                case 4:
                    submitReviews();
                    break;
                case 5:
                    readReviews();
                    break;
                case 6:
                    viewOrderHistory(); // Call the method here
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid input!");
            }
            System.out.println("Do you want to do more as a Customer? (y/n)");
            redoChoice = sc.next().charAt(0);
        }
    }
}