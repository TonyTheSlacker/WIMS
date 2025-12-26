import java.util.Scanner;

public class Warehouse {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        ProductList productList = new ProductList();
        ReceiptList receiptList = new ReceiptList();
        while(true) {
            int mainChoice = menu.displayMainMenu();
            switch(mainChoice) {
                case 1:
                    while(true) {
                        int productChoice = menu.displayProductManagementMenu();
                        switch(productChoice) {
                            case 1:
                                productList.addProduct();
                                break;
                            case 2:
                                productList.updateProduct();
                                break;
                            case 3:
                                productList.deleteProduct();
                                break;
                            case 4:
                                productList.showAllProducts();
                                break;
                            case 5:
                                System.out.println("Returning to the main menu...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                        if(productChoice == 5) {
                            break;
                        }
                    }
                    break;
                case 2:
                    while(true) {
                        int warehouseChoice = menu.displayWarehouseManagementMenu();
                        switch(warehouseChoice) {
                            case 1:
                                receiptList.createImportReceipt(productList);
                                break;
                            case 2:
                                receiptList.createExportReceipt(productList);
                                break;
                            case 3:  
                                System.out.println("Returning to the main menu...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                        if(warehouseChoice == 3) {
                            break;
                        }
                    }
                    break;
                case 3:
                    while(true) {
                        int reportChoice = menu.displayReportMenu();
                        switch(reportChoice) {
                            case 1:
                                productList.productExpired();
                                break;
                            case 2:
                                productList.productSelling();
                                break;
                            case 3:
                                productList.productOUS();
                                break;
                            case 4:
                                receiptList.showProductInReceipt();
                                break;
                            case 5:
                                System.out.println("Returning to the main menu...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        } 
                        if(reportChoice == 5) {
                            break;
                        }
                    }
                    break;
                case 4:
                    productList.storeProductsToFile();
                    receiptList.wareHouseFile();
                    break;
                case 5:
                    System.out.println("Closing the application.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}