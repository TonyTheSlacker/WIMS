# Warehouse Inventory Manager System (WIMS)

![Language](https://img.shields.io/badge/Language-Java-ED8B00?logo=java&logoColor=white)
![Type](https://img.shields.io/badge/Type-CLI_Application-4EAA25)
![Storage](https://img.shields.io/badge/Storage-File_Persistence-blue)

A robust command-line interface (CLI) application designed to simulate backend warehouse operations. This system manages the full product lifecycle—from import receipts to export logs—while enforcing business rules for stock levels and expiration dates.

It demonstrates a pure **Object-Oriented Design (OOP)** approach without reliance on external frameworks, utilizing custom file parsing for data persistence.

---

## 📦 Key Features

### 🛠️ Inventory Control
* **CRUD Operations:** Create, Update, and Delete product records.
* **Stock Alerts:** Automated reporting for items running low on stock (Out of Stock logic).
* **Shelf-Life Management:** proactively identifies products that are expired or expiring within 7 days.

### 📝 Transaction Logging
* **Import/Export Receipts:** Generates unique transaction IDs for every movement of goods.
* **Audit Trails:** All movements are committed to `wareHouse.dat`, creating a permanent history of inventory changes.

### 💾 Custom Persistence
Instead of a heavy database, the system uses a lightweight, portable file database structure:
* `product.dat`: Stores active inventory state.
* `importReceipt.dat` / `exportReceipt.dat`: Stores transaction histories using pipe-delimited (`|`) serialization.

---

## 🛠️ Technical Implementation

### Architecture
The project follows a modular **Model-View-Controller (MVC)** separation:
* **Models:** `Product`, `Receipt` (POJOs representing entities).
* **Services:** `ProductList`, `ReceiptList` (Business logic and File I/O).
* **Interface:** `Menu` (Console UI handler).

### Code Snippet: Expiration Logic
The system filters and sorts inventory based on date comparisons to generate safety reports.

```java
// From ProductList.java
public void productExpired() {
    // Logic to calculate items expiring within 7 days
    Date today = new Date();
    // ... comparison logic ...
    System.out.println("Products expiring soon:");
    // ...
}
```

## 🚀 How to Run
1. Clone the repository:

```git clone https://github.com/TonyTheSlacker/WarehouseManager.git```

2. Compile the Java files:

```javac Warehouse.java```

3. Run the application:

```java Warehouse```

---
*****Note: Ensure the .dat files are in the same directory as the class files to load existing data*****
