import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiptList {
    private List<Receipt> importReceipts;
    private List<Receipt> exportReceipts;
    private int lastImportCode = 0;
    private int lastExportCode = 1000000;
    
    public ReceiptList() {
        this.importReceipts = new ArrayList<>();
        this.exportReceipts = new ArrayList<>();
    }

    public void createImportReceipt(ProductList productList) {
        String importReceiptCode = String.format("%07d", lastImportCode);
        Receipt importReceipt = new Receipt();
        List<Product> importedProducts = new ArrayList<>();
        importReceipt.setCode(importReceiptCode);
        importReceipt.setDate(new Date());
        lastImportCode++;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Add a Product to Import Receipt: ");
            System.out.print("Enter product code: ");
            String productCode = sc.nextLine();
            Product existingProduct = productList.findProductByCode(productCode);
            if (existingProduct != null) {
                updateQuantity(existingProduct, sc);
                importedProducts.add(existingProduct);
            } else {
                productList.addProduct();
            }
            System.out.print("Add another product? (yes/no): ");
            String choice = sc.nextLine().toLowerCase();
            if (!choice.equals("yes")) {
                break;
            }
        } while(true);
        if(!importedProducts.isEmpty()) {
            importReceipt.setProducts(importedProducts);
            importReceipts.add(importReceipt);
            System.out.println("Import receipt created successfully!");
            productList.exportProductsToFile("product.dat");
            ReceiptToImportFile(importReceipt, "importReceipt.dat");
        } else {
            System.out.println("No products added to the import receipt.");
        }
    }

    private void updateQuantity(Product existingProduct, Scanner sc) {
        System.out.println("Product with code " + existingProduct.getCode() + " already exists in the product list.");
        System.out.print("Enter quantity to add: ");
        try {
            int quantityToAdd = Integer.parseInt(sc.nextLine());
            if(quantityToAdd <= 0) {
                System.out.println("Quantity to add must be greater than 0.");
            } else {
                existingProduct.setQuantity(existingProduct.getQuantity() + quantityToAdd);
                System.out.println("Product quantity updated.");
            }
        } catch(NumberFormatException e) {
            System.out.println("Invalid input for quantity. Please try again.");
        }
    }
                   
    public void ReceiptToImportFile(Receipt importReceipt, String filename) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try(FileWriter fileWriter = new FileWriter(filename, true); 
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Import Receipt - Code: " + importReceipt.getCode());
            bufferedWriter.newLine();
            bufferedWriter.write("Import Date: " + dateFormat.format(importReceipt.getDate()));
            bufferedWriter.newLine();
            for(Product product: importReceipt.getProducts()) {
                bufferedWriter.write(product.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            System.out.println("Import receipt information exported to " + filename);
            } catch(IOException e) {
                System.out.println("An error occurred while handling import information to " + filename);
        }
    }

    public void createExportReceipt(ProductList productList) {
        Scanner sc = new Scanner(System.in);
        lastExportCode++;
        String exportReceiptCode = "1" + String.format("%06d", lastExportCode);
        Receipt exportReceipt = new Receipt();
        List<Product> exportedProducts = new ArrayList<>();
        exportReceipt.setCode(exportReceiptCode);
        exportReceipt.setDate(new Date());
        boolean validProductEntered = false;
        while (!validProductEntered) {
            System.out.println("Add a Product to Export Receipt (Enter <done> to finish): ");
            System.out.print("Enter product code: ");
            String productCode = sc.nextLine();
            if (productCode.equalsIgnoreCase("done")) {
                validProductEntered = true;
                break;
            }
            Product product = productList.findProductByCode(productCode);
            if (product == null) {
                System.out.println("Product with code " + productCode + " does not exist in the product list.");
                continue;
            }
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(sc.nextLine());
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0.");
                continue;
            }
            if (product.getQuantity() < quantity) {
                System.out.println("Not enough quantity in stock for product with code " + productCode);
                continue;
            }
            product.setQuantity(product.getQuantity() - quantity);
    
            Product exportedProduct = new Product(productCode, product.getName(), product.getManufacturingDate(), product.getExpirationDate(), quantity);
            exportedProducts.add(exportedProduct);
            System.out.println("Product added to export receipt.");
            System.out.print("Add another product? (yes/no): ");
            String choice = sc.nextLine().toLowerCase();
            if (!choice.equals("yes")) {
                validProductEntered = true;
            }
        }
        if (!exportedProducts.isEmpty()) {
            exportReceipt.setProducts(exportedProducts);
            exportReceipts.add(exportReceipt);
            System.out.println("Export receipt created successfully!");
        } else {
            System.out.println("No products added to the export receipt.");
        }
        ReceiptToExportFile(exportReceipt, "exportReceipt.dat");
        productList.exportProductsToFile("product.dat");
    }       

    public void ReceiptToExportFile(Receipt importReceipt, String filename) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try(FileWriter fileWriter = new FileWriter(filename, true); 
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Export Receipt - Code: " + importReceipt.getCode());
            bufferedWriter.newLine();
            bufferedWriter.write("Export Date: " + dateFormat.format(importReceipt.getDate()));
            bufferedWriter.newLine();
            for(Product product: importReceipt.getProducts()) {
                bufferedWriter.write(product.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            System.out.println("Export receipt information exported to " + filename);
            } catch(IOException e) {
                System.out.println("An error occurred while handling export information to " + filename);
        }
    }

    public void showProductInReceipt() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter product code to check: ");
        String productCode = sc.nextLine();
        boolean foundInImportReceipt = false;
        boolean foundInExportReceipt = false;
        List<String> importReceiptCodes = new ArrayList<>();
        List<String> exportReceiptCodes = new ArrayList<>();
        try(BufferedReader importReader = new BufferedReader(new FileReader("importReceipt.dat"))) {
            String line;
            String currentReceiptCode = "";
            while((line = importReader.readLine()) != null) {
                if(line.startsWith("Import Receipt - Code: ")) {
                    currentReceiptCode = line.split(": ")[1];
                } else if(line.contains("Product [code='" + productCode + "'")) {
                    foundInImportReceipt = true;
                    importReceiptCodes.add(currentReceiptCode);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        try(BufferedReader exportReader = new BufferedReader(new FileReader("exportReceipt.dat"))) {
            String line;
            String currentReceiptCode = "";
            while((line = exportReader.readLine()) != null) {
                if(line.startsWith("Export Receipt - Code: ")) {
                    currentReceiptCode = line.split(": ")[1];
                } else if(line.contains("Product [code='" + productCode + "'")) {
                    foundInExportReceipt = true;
                    exportReceiptCodes.add(currentReceiptCode);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println("Product found at:");
        if(foundInImportReceipt || foundInExportReceipt) {
            if(foundInImportReceipt && !importReceiptCodes.isEmpty()) {
                System.out.print("Import Receipt " + importReceiptCodes.toString());
            }
            if(foundInExportReceipt && !exportReceiptCodes.isEmpty()) {
                System.out.print("Export Receipt" + exportReceiptCodes.toString());
            }
        } else {
            System.out.println("No receipt(s) found for product with code " + productCode);
        }
    }    

    public void wareHouseFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try(BufferedWriter warehouseWriter = new BufferedWriter(new FileWriter("wareHouse.dat", true));
        BufferedReader importReader = new BufferedReader(new FileReader("importReceipt.dat"));
        BufferedReader exportReader = new BufferedReader(new FileReader("exportReceipt.dat"))) {
            String line;
            warehouseWriter.newLine();
            warehouseWriter.write("Warehouse Data - Date: " + dateFormat.format(new Date()));
            warehouseWriter.newLine();
            warehouseWriter.write("Import Receipt(s):");
            warehouseWriter.newLine();
            while((line = importReader.readLine()) != null) {
                warehouseWriter.write(line);
                warehouseWriter.newLine();
            }
            warehouseWriter.write("Export Receipt(s):");
            warehouseWriter.newLine();
            while((line = exportReader.readLine()) != null) {
                warehouseWriter.write(line);
                warehouseWriter.newLine();
            }
            System.out.println("Data exported to wareHouse.dat");
        } catch(IOException e) {
            System.out.println("An error occurred while exporting data to wareHouse.dat");
        }
    }
}