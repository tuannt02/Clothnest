package nhom7.clothnest.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Wishlist {

    public static final String COLLECTION_NAME = "wishlist";

    private String key;
    private String productName;
    private String productImage;
    private double regularCost;
    private int discount;
    private double discountCost;
    private Timestamp date_add;

    public Wishlist()   {

    }

    public Wishlist(String productName, String productImage, double regularCost, int discount, Timestamp date_add) {
        this.productName = productName;
        this.productImage = productImage;
        this.regularCost = regularCost;
        this.discount = discount;
        this.date_add = date_add;
    }

    public String getKey() {
        return key;
    }

    public double getDiscountCost() {
        return regularCost*(100-discount)/100;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getRegularCost() {
        return "$  " + String.valueOf(regularCost);
    }

    public String getDiscount() {
        return "$  " + String.valueOf(regularCost*(100-discount)/100);
    }

    public Timestamp getDate_add() {
        return date_add;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setRegularCost(double regularCost) {
        this.regularCost = regularCost;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setDate_add(Timestamp date_add) {
        this.date_add = date_add;
    }

    public void setDiscountCost(double discountCost) {
        this.discountCost = discountCost;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "productName='" + productName + '\'' +
                ", productImage=" + productImage +
                ", regularCost=" + regularCost +
                ", discount=" + discount +
                ", date_add=" + date_add +
                '}';
    }
}
