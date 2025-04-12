import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.PriorityQueue;

public class CanteenGUI extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTable menuTable;
    private JTable ordersTable;

    public CanteenGUI() {
        setTitle("Canteen Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Card layout for switching pages
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Menu Page
        JPanel menuPanel = createMenuPage();
        // Orders Page
        JPanel ordersPanel = createOrdersPage();

        // Add pages to the main panel
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(ordersPanel, "Orders");

        add(mainPanel);
        cardLayout.show(mainPanel, "Menu"); // Start with the Menu page
    }

    public JPanel createMenuPage() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Canteen Menu", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table for displaying menu items
        menuTable = new JTable();
        updateMenuTable(); // Dynamically populate table
        JScrollPane scrollPane = new JScrollPane(menuTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Navigation button to go to Orders Page
        JButton viewOrdersButton = new JButton("View Pending Orders");
        viewOrdersButton.addActionListener(e -> cardLayout.show(mainPanel, "Orders"));
        panel.add(viewOrdersButton, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel createOrdersPage() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Pending Orders", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table for displaying orders
        ordersTable = new JTable();
        updateOrdersTable();
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Navigation button to go back to Menu Page
        JButton viewMenuButton = new JButton("View Menu");
        viewMenuButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        panel.add(viewMenuButton, BorderLayout.SOUTH);

        return panel;
    }

    private void updateMenuTable() {
        // Dynamically retrieve data from Admin.itemsList
        String[] columnNames = {"Name", "Price", "Available"};
        Object[][] data = new Object[Admin.itemsList.size()][3];

        for (int i = 0; i < Admin.itemsList.size(); i++) {
            Item item = Admin.itemsList.get(i);
            data[i][0] = item.name;
            data[i][1] = item.price;
            data[i][2] = item.quantity;
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        menuTable.setModel(model);
    }

    private void updateOrdersTable() {
        // Retrieve pending orders from Customer.orders
        String[] columnNames = {"Order ID", "Status", "Special Requests", "VIP"};
        PriorityQueue<Orders> ordersQueue = new PriorityQueue<>(Customer.orders); // Clone the queue

        // Prepare data array
        Object[][] data = new Object[ordersQueue.size()][4];
        int i = 0;

        // Iterate through the cloned queue to avoid modifying the original
        while (!ordersQueue.isEmpty()) {
            Orders order = ordersQueue.poll(); // Remove one element at a time
            data[i][0] = order.uid;
            data[i][1] = order.orderStatus;
            data[i][2] = order.specialRequests != null ? order.specialRequests : "None";
            data[i][3] = order.isVip ? "Yes" : "No";
            i++;
        }

        // Update table model
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        ordersTable.setModel(model);
    }


    public static void main(String[] args) {
        // Ensure Admin.itemsList is initialized in Main before launching GUI
        SwingUtilities.invokeLater(() -> {
            CanteenGUI gui = new CanteenGUI();
            gui.setVisible(true);
        });
    }
}
