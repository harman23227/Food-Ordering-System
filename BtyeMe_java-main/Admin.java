import javax.swing.*;
import java.util.*;

// ADMIN OR STAFF
class Item {
    String name;
    Double price;
    int quantity;
    String category;

    public Item(String name, Double price, int quantity, String category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Name: " + name + "   Price: " + price;
    }
}

public class Admin {
    static double totalSale;
    Scanner sc = new Scanner(System.in);
    static ArrayList<Item> itemsList = new ArrayList<>();

    public void addItems() {
        System.out.println("\t--ADDING ITEM TO INVENTORY--");
        System.out.println("Enter name of the item: ");
        String name = sc.nextLine();
        System.out.println("Enter price of the item: ");
        Double price = sc.nextDouble();
        System.out.println("Enter quantity of item added to inventory: ");
        int qty = sc.nextInt();
        sc.nextLine();
        System.out.println("Pick category of item: ");
        System.out.println("1. Food Item\n2. Drink\n3. Snack");
        int input = sc.nextInt();
        sc.nextLine();
        String category = null;
        if (input == 1) category = "Food Item";
        else if (input == 2) category = "Drink";
        else if (input == 3) category = "Snack";
        else System.out.println("Wrong Input");
        System.out.println("Item added!");
        Item item = new Item(name, price, qty, category);
        itemsList.add(item);
    }

    public void updateItems() {
        char bigChoice = 'y';
        while (bigChoice == 'y') {
            System.out.println("Pick an item by name: ");
            displayItems();
            String itemPicked = sc.nextLine();
            for (Item item : Admin.itemsList) {
                if (Objects.equals(item.name, itemPicked)) {
                    char choice = 'y';
                    while (choice == 'y') {
                        System.out.println("What do you want to change?");
                        System.out.println("1. Name\n2. Price\n3. Quantity\n4. Category");
                        int input = sc.nextInt();
                        sc.nextLine();
                        if (input == 1) {
                            System.out.println("Enter new name: ");
                            item.name = sc.nextLine();
                            System.out.println("Name updated!");
                        } else if (input == 2) {
                            System.out.println("Enter new price: ");
                            item.price = sc.nextDouble();
                            System.out.println("Price updated!");
                        } else if (input == 3) {
                            System.out.println("Enter new quantity: ");
                            item.quantity = sc.nextInt();
                            System.out.println("Quantity updated!");
                        } else if (input == 4) {
                            System.out.println("Pick category of item: ");
                            System.out.println("1. Food Item\n2. Drink\n3. Snack");
                            int catInput = sc.nextInt();
                            sc.nextLine();
                            String category = null;
                            if (catInput == 1) category = "Food Item";
                            else if (catInput == 2) category = "Drink";
                            else if (catInput == 3) category = "Snack";
                            item.category = category;
                            System.out.println("Category updated!");
                        }
                        System.out.println("Do you want to update more?(y/n)");
                        choice = sc.next().charAt(0);
                        sc.nextLine();
                    }
                }
            }
            System.out.println("Do you want to update any other item? (y/n)");
            bigChoice = sc.next().charAt(0);
            sc.nextLine();
        }
        System.out.println("UPDATED LIST ->");
        for (Item item : Admin.itemsList) {
            System.out.println("Name: " + item.name +
                    "   Price: " + item.price +
                    "   Category: " + item.category +
                    "   Quantity: " + item.quantity);
        }
    }

    public void removeItems() {
        char choice = 'y';
        while (choice == 'y') {
            System.out.println("Enter name of item you want to remove from the menu: ");
            displayItems();
            String itemPicked = sc.nextLine();
            Admin.itemsList.removeIf(item -> Objects.equals(item.name, itemPicked));
            System.out.println("Item successfully removed!");
            System.out.println("Do you want to remove any more items?(y/n)");
            choice = sc.next().charAt(0);
            sc.nextLine();
        }
        displayItems();
    }

    public void viewPendingOrders() {
        System.out.println("Orders received are: ");
        displayOrders();
    }

    public void updateOrderStatus() {
        System.out.println("Pick an order by ID to mark their status: ");
        displayOrders();
        int orderPickChoice = sc.nextInt();
        sc.nextLine();
        for (Orders order : Customer.orders) {
            if (orderPickChoice == order.uid) {
                System.out.println("1. Preparing\n2. Out for delivery\n3. Delivered");
                int statusChoice = sc.nextInt();
                sc.nextLine();
                if (statusChoice == 1) {
                    order.orderStatus = "Preparing";
                } else if (statusChoice == 2) {
                    order.orderStatus = "Out for Delivery";
                } else if (statusChoice == 3) {
                    order.orderStatus = "Delivered";
                } else {
                    System.out.println("Wrong input!");
                }
            }
        }
        System.out.println("Order marked!");
    }

    public void viewSpecialOrders() {
        System.out.println("Special orders received are: ");
        PriorityQueue<Orders> tempQueue = new PriorityQueue<>(Customer.orders);
        for (Orders order : Customer.orders) {
            if (order.specialRequests != null)
                System.out.println(order);
        }
        System.out.println("Pick an order by ID to mark their status: ");
        int orderPickChoice = sc.nextInt();
        sc.nextLine();
        for (Orders order : Customer.orders) {
            if (orderPickChoice == order.uid) {
                System.out.println("1. Preparing\n2. Out for delivery\n3. Delivered");
                int statusChoice = sc.nextInt();
                sc.nextLine();
                if (statusChoice == 1) {
                    order.orderStatus = "Preparing";
                } else if (statusChoice == 2) {
                    order.orderStatus = "Out for Delivery";
                } else if (statusChoice == 3) {
                    order.orderStatus = "Delivered";
                } else {
                    System.out.println("Wrong input!");
                }
            }
        }
        System.out.println("Order marked!");
    }

    public void refund() {
        System.out.println("Pick an order by order ID to refund it: ");
        for (Orders order : Customer.orders) {
            if (!Objects.equals(order.orderStatus, "Delivered")) {
                System.out.println(order);
            }
        }
        int enteredOrderID = sc.nextInt();
        sc.nextLine();
        for (Orders order : Customer.orders) {
            if (order.uid == enteredOrderID) {
                order.orderStatus = "Cancelled";
            }
        }
        System.out.println("Order has been refunded");
    }

    public void reportGeneration() {
        int minQty = Integer.MAX_VALUE;
        String popularItem = null;
        System.out.println("Today's report is as follows: ");
        System.out.println("Total sales: " + totalSale);
        System.out.print("Most popular item for the day: ");
        for (Item value : itemsList) {
            minQty = Math.min(minQty, value.quantity);
        }
        for (Item item : itemsList) {
            if (item.quantity == minQty) {
                System.out.println(item.name);
            }
        }
        System.out.println("Total orders for today are: " + Customer.orders.size());
    }

    protected void displayItems() {
        Iterator<Item> it = itemsList.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    protected void displayOrders() {
        PriorityQueue<Orders> tempQueue = new PriorityQueue<>(Customer.orders);
        while (!tempQueue.isEmpty()) {
            System.out.println(tempQueue.poll());
        }
    }

    public void menu() {
        System.out.println("Select what you want to do: ");
        char redoChoice = 'y';
        while (redoChoice == 'y') {
            System.out.println("1. Menu Management.\n" +
                    "2. Order Management.\n" +
                    "3: Report Generation.\n" +
                    "4. Exit admin interface.");
            int customerMenuChoice = sc.nextInt();
            sc.nextLine();
            switch (customerMenuChoice) {
                case 1:
                    System.out.println("Select what management operation you want to perform:");
                    System.out.println("1. Add menu items\n2. Update menu items\n3. Remove items");
                    int case1Choice = sc.nextInt();
                    sc.nextLine();
                    if (case1Choice == 1) addItems();
                    else if (case1Choice == 2) updateItems();
                    else if (case1Choice == 3) removeItems();
                    break;
                case 2:
                    System.out.println("Select what order management functionality you want to perform: ");
                    System.out.println("1. View pending orders\n2. Update order status\n3. Process refund\n4. Manage special requests");
                    int case2Choice = sc.nextInt();
                    sc.nextLine();
                    if (case2Choice == 1){
                        SwingUtilities.invokeLater(() -> {
                            CanteenGUI gui = new CanteenGUI();
                            gui.setVisible(true);
                        });
                    }
                    else if (case2Choice == 2) updateOrderStatus();
                    else if (case2Choice == 3) refund();
                    else if (case2Choice == 4) viewSpecialOrders();
                    break;
                case 3:
                    reportGeneration();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid input! ");
            }
            System.out.println("Do you want to do more as a Admin? (y/n)"); // TODO SEE LATER TO CHANGE FUNCTIONALITY
            redoChoice = sc.next().charAt(0);
        }
    }
}