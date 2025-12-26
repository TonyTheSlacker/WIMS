import java.util.Scanner;

public class Menu {
    private Scanner sc;

    public Menu() {
        this.sc = new Scanner(System.in);
    }

    public int displayMainMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Manage Products");
        System.out.println("2. Manage Warehouse");
        System.out.println("3. Report");
        System.out.println("4. Store Data to Files");
        System.out.println("5. Close");
        return getUserChoice();
    }

    public int displayProductManagementMenu() {
        System.out.println("Product Management Menu:");
        System.out.println("1. Add a Product");
        System.out.println("2. Update Product Information");
        System.out.println("3. Delete Product");
        System.out.println("4. Show All Products");
        System.out.println("5. Back to Main Menu");
        return getUserChoice();
    }

    public int displayWarehouseManagementMenu() {
        System.out.println("Warehouse Management Menu:");
        System.out.println("1. Create an Import Receipt");
        System.out.println("2. Create an Export Receipt");
        System.out.println("3. Back to Main Menu");
        return getUserChoice();
    }

    public int displayReportMenu() {
        System.out.println("Report Menu:");
        System.out.println("1. Products that have expired or is about to expire(within 7 days)");
        System.out.println("2. Products that the store is selling");
        System.out.println("3. Products that are running out of stock");
        System.out.println("4. Import/Export receipt of a product");
        System.out.println("5. Back to Main Menu");
        return getUserChoice();
    }
    
    public int getUserChoice() {
        System.out.print("Enter your choice: ");
        return sc.nextInt();
    }
}