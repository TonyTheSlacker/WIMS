import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Date;
import java.util.Iterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.*;

public class ProductList/*<ImportExportInformation> extends ArrayList<Product>*/ {
    List<Product> products = new ArrayList<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // static: there's only 1 instance of the SimpleDateFormat. final ensures that the reference to the SimpleDateFormat cant be changed once it's initialized    
    public ProductList() {
        products = new ArrayList<>();
    }

    public void addProduct() {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        importProductsFromFile("product.dat");
        while(true) {
            Date manufacturingDate = null;
            Date expirationDate = null;
            int quantity = 0;
            System.out.println("Add new product: ");
            String code = "";
            String name = "";
            while(code.isEmpty() || isProductCodeDuplicate(code)) {
                System.out.print("Enter product code: ");
                code = sc.nextLine();
                if(code.isEmpty()) {
                    System.out.println("Product code cannot be empty.");
                }
                if(isProductCodeDuplicate(code)) {
                    System.out.println("Product with this code already exists. Please enter a unique code.");
                }
            }
            while(name.isEmpty()) {
                System.out.print("Enter product name: ");
                name = sc.nextLine();
                if(name.isEmpty()) {
                    System.out.println("Product name cannot be empty.");
                }
            }
            while(true) {
                System.out.print("Enter manufacturing date (yyyy-MM-dd): ");
                String manufacturingDateString = sc.nextLine();
                try {
                    manufacturingDate = dateFormat.parse(manufacturingDateString);
                    break;
                } catch(ParseException e) {
                    System.out.println("Invalid date format. Please use the suggested format");
                }
            }
            while (true) {
                System.out.print("Enter expiration date (yyyy-MM-dd): ");
                String expirationDateString = sc.nextLine();
                try {
                    expirationDate = dateFormat.parse(expirationDateString);
                    if(manufacturingDate.after(expirationDate)) { // Check manufacturing date > expiration date
                        System.out.println("Manufacturing date must be before the expiration date.");
                        continue; // loop again
                    }
                    break;
                } catch(ParseException e) {
                    System.out.println("Invalid date format. Please use the format: EEE MMM dd HH:mm:ss z yyyy");
                }
            }       
            while(true) {
                System.out.print("Enter quantity: ");
                try {
                    quantity = Integer.parseInt(sc.nextLine());
                    if(quantity <= 0) {
                        System.out.println("Quantity must be greater than 0.");
                        continue;
                    }
                    break;
                } catch(NumberFormatException e) {
                    System.out.println("Invalid quantity. Please enter a valid number.");
                }
            }
            Product product = new Product(code, name, manufacturingDate, expirationDate, quantity);
            products.add(product);
            System.out.println("Product added successfully!");
            System.out.println("Product: ");
            System.out.println("Code: " + product.getCode() + ", name: " + product.getName() + ", Manufacturing Date: " + dateFormat.format(product.getManufacturingDate()) + ", Expiration Date: " + dateFormat.format(product.getExpirationDate()) + ", quantity: " + product.getQuantity());
            System.out.print("Do you want to add another product? (yes/no): ");
            String choice = sc.nextLine().toLowerCase();
            if(!choice.equals("yes")) {
                break;
            }
        }
    }
    
    public boolean isProductCodeDuplicate(String code) {
        for(Product product: products) {
            if(product.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
    
    public void deleteProduct() {
        importProductsFromFile("product.dat");
        // Load exportReceipts and importReceipts
        List<String> exportReceiptLines = loadReceiptFile("exportReceipt.dat");
        List<String> importReceiptLines = loadReceiptFile("importReceipt.dat");
        Scanner sc = new Scanner(System.in);
        System.out.println("Delete a Product: ");
        System.out.print("Enter product code to delete: ");
        String productCodeToDelete = sc.nextLine();
        boolean productFound = false;
        Iterator<Product> iterator = products.iterator();
        while(iterator.hasNext()) {
            Product product = iterator.next();
            if(product.getCode().equals(productCodeToDelete)) {
                productFound = true;
                if(isProductImportedOrExported(product, exportReceiptLines, importReceiptLines)) {
                    System.out.println("Cannot delete the product as export information exists.");
                } else {
                    System.out.println("Are you sure you want to delete the product?(yes/no): ");
                    String confirmation = sc.nextLine().toLowerCase();
                    if(confirmation.equals("yes")) {
                        iterator.remove();
                        System.out.println("Product deleted successfully!");
                    } else {
                        System.out.println("Cancelled!");
                    }
                }
                break;
            }
        }
        if(!productFound) {
            System.out.println("Product " + productCodeToDelete + " does not exist.");
        }
    }
    
    private boolean isProductImportedOrExported(Product product, List<String> exportReceiptLines, List<String> importReceiptLines) {
        for(String line: exportReceiptLines) {
            if(line.contains(product.getCode())) {
                return true; // Product exported
            }
        }
        for(String line: importReceiptLines) {
            if(line.contains(product.getCode())) {
                return true; // Product imported
            }
        }
        return false;
    }
    
    private List<String> loadReceiptFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch(IOException e) {
            //e.printStackTrace();
            System.out.println("Error");
        }
        return lines;
    }
    
    public void updateProduct() {
        Scanner sc = new Scanner(System.in);
        boolean productFound = false;
        while(true) {
            System.out.println("Update Product Information");
            System.out.print("Enter product code to update: ");
            String productCodeToUpdate = sc.nextLine();
            for(Product product: products) {
                if(product.getCode().equals(productCodeToUpdate)) {
                    productFound = true;
                    System.out.println("Current Product Information");
                    System.out.println(product.toString());
                    System.out.print("Enter new product name(leave blank to keep current): ");
                    String newName = sc.nextLine();
                    if(!newName.trim().isEmpty()) {
                        product.setName(newName);
                    }
                    System.out.print("Enter new manufacturing date (yyyy-MM-dd) (leave blank to keep current): ");
                    String newManufacturingDateStr = sc.nextLine();
                    if(!newManufacturingDateStr.trim().isEmpty()) {
                        try {
                            Date newManufacturingDate = dateFormat.parse(newManufacturingDateStr);
                            product.setManufacturingDate(newManufacturingDate);
                        } catch(ParseException e) {
                            System.out.println("Invalid date format. Manufacturing date not updated.");
                        }
                    }
                    System.out.print("Enter new expiration date (yyyy-MM-dd) (leave blank to keep current): ");
                    String newExpirationDateStr = sc.nextLine();
                    if(!newExpirationDateStr.trim().isEmpty()) {
                        try {
                            Date newExpirationDate = dateFormat.parse(newExpirationDateStr);
                            product.setExpirationDate(newExpirationDate);
                        } catch (ParseException e) {
                            System.out.println("Invalid date format. Expiration date not updated.");
                        }
                    }
                    System.out.print("Enter new quantity(leave blank to keep current): ");
                    String newQuantityStr = sc.nextLine();
                    if(!newQuantityStr.trim().isEmpty()) {
                        try {
                            int newQuantity = Integer.parseInt(newQuantityStr);
                            if(newQuantity > 0) {
                                product.setQuantity(newQuantity);
                            } else {
                                System.out.println("Quantity must be greater than 0. Quantity not updated.");
                            }
                        } catch(NumberFormatException e) {
                            System.out.println("Invalid quantity format. Quantity not updated.");
                        }
                    }
                    System.out.println("Product information updated successfully!");
                    break;
                }
            }
            if(!productFound) {
                System.out.println("Product code " + productCodeToUpdate + " does not exist.");
            }
            System.out.println("Do you want to update another product? (yes/no): ");
            String choice = sc.nextLine().toLowerCase();
            if(!choice.equals("yes")) {
                break;
            }
        }
    }
    
    public void showAllProducts() {
        System.out.println("Product list");
        if (products.isEmpty()) {
            importProductsFromFile("product.dat");
        }
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-5s | %-15s | %-28s | %-30s | %-9s |\n", "Code", "Name", "Manufacturing Date", "Expiration Date", "Quantity");
            System.out.println("------------------------------------------------------------------------------------------------------");
            List<Product> uniqueProducts = new ArrayList<>();
            for(Product product: products) {
                boolean found = false;
                for(Product uniqueProduct: uniqueProducts) {
                    if(uniqueProduct.getCode().equals(product.getCode())) {
                        found = true;
                        if(product.getQuantity() > uniqueProduct.getQuantity()) {
                            uniqueProduct.setQuantity(product.getQuantity());
                        }
                        break;
                    }
                }
                if(!found) {
                    uniqueProducts.add(product);
                }
            }
            products.clear();
            products.addAll(uniqueProducts);
            exportProductsToFile("product.dat");
            for(Product uniqueProduct: uniqueProducts) {
                System.out.printf("| %-5s | %-15s | %-25s | %-30s | %-9s |\n", uniqueProduct.getCode(), uniqueProduct.getName(), uniqueProduct.getManufacturingDate(), uniqueProduct.getExpirationDate(), uniqueProduct.getQuantity());
            }
            System.out.println("------------------------------------------------------------------------------------------------------");
        }
        waitForUserInput();
    }
        
    
    public Product findProductByCode(String productCode) {
        for(Product product: products) {
            if(product.getCode().equalsIgnoreCase(productCode)) {
                return product;
            }
        }
        return null;
    }
    
    public void exportProductsToFile(String filename) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
            for(Product product: products) {
                String line = product.getCode() + "|" + product.getName() + "|" + dateFormat.format(product.getManufacturingDate()) + "|" + dateFormat.format(product.getExpirationDate()) + "|" + product.getQuantity();
                writer.write(line);
                writer.newLine();
            }
            //System.out.println("Product information exported to " + filename);
        } catch(IOException e) {
            //e.printStackTrace();
            System.out.println("An error occurred while exporting product information to " + filename);
        }
    }

    public void importProductsFromFile(String filename) {
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
            while((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if(parts.length == 5) {
                    String code = parts[0];
                    String name = parts[1];
                    Date manufacturingDate = dateFormat.parse(parts[2]);
                    Date expirationDate = dateFormat.parse(parts[3]);
                    int quantity = Integer.parseInt(parts[4]);
                    Product product = new Product(code, name, manufacturingDate, expirationDate, quantity);
                    products.add(product);
                }
            }
            System.out.println("Product information imported from " + filename);
        } catch(IOException | ParseException e) {
            //e.printStackTrace();
            System.out.println("An error occurred while importing product information from " + filename);
        }
    }
    
    public void productExpired() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(); //Get current date
        System.out.println("Products that have expired:");
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("| Code  | Name            | Manufacturing Date   | Expiration Date    | Quantity |");
        System.out.println("-------------------------------------------------------------------------------------------------");
        for(Product product: products) {
            if(product.getExpirationDate().before(currentDate)) { // exp date is b4 today
                System.out.printf("| %-5s | %-15s | %-20s | %-18s | %-8d |\n", product.getCode(), product.getName(), dateFormat.format(product.getManufacturingDate()), dateFormat.format(product.getExpirationDate()), product.getQuantity());
            }
        }
        System.out.println("-------------------------------------------------------------------------------------------------");
        waitForUserInput();
    }
    
    public void productSelling() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Product(s) that are selling: ");
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("| Code  | Name            | Manufacturing Date   | Expiration Date    | Quantity |");
        System.out.println("-------------------------------------------------------------------------------------------------");
        for(Product product: products) {
            if(product.getQuantity() > 3 && !product.getExpirationDate().before(product.getManufacturingDate())) {
                System.out.printf("| %-5s | %-15s | %-20s | %-18s | %-8d |\n", product.getCode(), product.getName(), dateFormat.format(product.getManufacturingDate()), dateFormat.format(product.getExpirationDate()), product.getQuantity());
            }
        }
        System.out.println("-------------------------------------------------------------------------------------------------");
        waitForUserInput();
    }
    
    private static Comparator<Product> quantityComparator = new Comparator<Product>() { //store instance of a comparator for comparing objects of Product class.
        @Override
        public int compare(Product product1, Product product2) {
            return product1.getQuantity() - product2.getQuantity();
        }
    };

    public void productOUS() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Product> outOfStockProducts = new ArrayList<>();
        System.out.println("Out of Stock Product(s): ");
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("| Code  | Name            | Manufacturing Date   | Expiration Date    | Quantity |");
        System.out.println("-------------------------------------------------------------------------------------------------");
        for(Product product: products) {
            if(product.getQuantity() <= 3) {
                outOfStockProducts.add(product);
            }
        }
        Collections.sort(outOfStockProducts, quantityComparator);
        for(Product product: outOfStockProducts) {
            System.out.printf("| %-5s | %-15s | %-20s | %-18s | %-8d |\n", product.getCode(), product.getName(), dateFormat.format(product.getManufacturingDate()), dateFormat.format(product.getExpirationDate()), product.getQuantity());
        }
        System.out.println("-------------------------------------------------------------------------------------------------");
        waitForUserInput();
    }

    public void storeProductsToFile() {
        String filename = "product.dat";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for(Product product: products) {
                writer.write(product.getCode() + "|" + product.getName() + "|" + product.getManufacturingDate() + "|" + product.getExpirationDate() + "|" + product.getQuantity());
                writer.newLine();
            }
            System.out.println("Product information exported to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while exporting product information to " + filename);
        }
    }

    private void waitForUserInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Press Enter to return to the main menu...");
        sc.nextLine();
    }
}