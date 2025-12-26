import java.util.Date;
import java.util.List;

public class Receipt {
    private String code; 
    private String receiptCode;
    private Date date;
    private List<Product> products;

    public Receipt() {}

    public Receipt(String receiptCode, List<Product> products) {
        this.receiptCode = receiptCode;
        this.products = products;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for(Product product: products) {
            totalQuantity += product.getQuantity();
        }
        return totalQuantity;
    }

    @Override
    public String toString() {
        StringBuilder receiptStr = new StringBuilder();
        receiptStr.append("Receipt Code: ").append(code).append("\n");
        receiptStr.append("Products:\n");
        for(Product product: products) {
            receiptStr.append("- ").append(product.getName()).append(" (Quantity: ").append(product.getQuantity()).append(")\n");
        }
        receiptStr.append("Total Quantity: ").append(getTotalQuantity()).append("\n");
        return receiptStr.toString();
    }
}