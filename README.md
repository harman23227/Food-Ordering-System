# University Canteen Management System

![Java](https://img.shields.io/badge/Java-17-blue)
![Swing](https://img.shields.io/badge/GUI-Swing-orange)
![JUnit](https://img.shields.io/badge/Testing-JUnit5-success)
![OOP](https://img.shields.io/badge/Design-OOP-brightgreen)

A complete solution for managing university canteen operations, featuring:
- Role-based access (Admin/Customer)
- Real-time inventory tracking
- Priority order processing (VIP)
- Swing GUI interface
- Order history persistence

## ✨ Key Features
| Role        | Capabilities                                                                 |
|-------------|------------------------------------------------------------------------------|
| **Admin**   | Add/update menu items, process orders, generate sales reports, handle refunds|
| **Customer**| Browse menu, VIP registration, cart management, order tracking, reviews      |

**Technical Highlights**:
- 📊 PriorityQueue for VIP order prioritization
- 📁 File-based data persistence (`customers.txt`, order histories)
- 🧪 JUnit tests for critical workflows
- 🖥️ Swing GUI with CardLayout navigation
- 📈 Sales analytics and inventory alerts

## 🛠️ Code Structure
src/
├── Main.java # Entry point with login system
├── Admin.java # Inventory/order management
├── Customer.java # Customer operations
├── Orders.java # Order processing logic
├── CanteenGUI.java # Swing interface
├── tests/ # JUnit tests
│ ├── MainTest.java # Auth tests
│ └── OrderTest.java # Order validation

Copy

## 🚀 Getting Started
1. **Requirements**: Java 17+
2. **Run**: 
   ```bash
   javac Main.java && java Main
Test:

bash
Copy
javac -cp junit-platform-console-standalone-1.8.1.jar:. OrderTest.java && java -jar junit-platform-console-standalone-1.8.1.jar --class-path . --scan-class-path
