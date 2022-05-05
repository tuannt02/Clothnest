package nhom7.clothnest.models;
public class Product1 {
    private String id;
    private String productName;
    private int productImage;
    private double regularCost;
    private int discount;

    public Product1(String id, String productName, int productImage, double regularCost, int discount) {
        this.id = id;
        this.productName = productName;
        this.productImage = productImage;
        this.regularCost = regularCost;
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public double getRegularCost() {
        return regularCost;
    }

    public void setRegularCost(double regularCost) {
        this.regularCost = regularCost;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}