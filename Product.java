import java.util.Date;

public class Product {
    private String code, name;
    private Date manufacturingDate, expirationDate;
    private int quantity;

    public Product() {}

    public Product(String code, String name, Date manufacturingDate, Date expirationDate, int quantity) {
        this.code = code;
        this.name = name;
        this.manufacturingDate = manufacturingDate;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(Date manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product [" + "code='" + code + '\'' + ", name='" + name + '\'' + ", manufacturingDate=" + manufacturingDate + ", expirationDate=" + expirationDate + ", quantity=" + quantity + ']' + "\n";
    }
}