package nhom7.clothnest;

public class Product {
    public String productName;
    public int productImage;
    public String regularCost;
    public String discount;

    public Product(String productName, int productImage, String regularCost, String discount) {
        this.productName = productName;
        this.productImage = productImage;
        this.regularCost = regularCost;
        this.discount = discount;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductImage() {
        return productImage;
    }

    public String getRegularCost() {
        return regularCost;
    }

    public String getDiscount() {
        return discount;
    }
}