package nhom7.clothnest.models;

import java.io.Serializable;

public class CartItem implements Serializable {

    public static final String COLLECTION_NAME = "cart";

    private String keyProduct;
    private String key;
    private String img;
    private int qty;
    private String name, color, size;
    private Double price;
    private int discount;
    private double discountPrice;

    public CartItem()   {

    }

    public CartItem(String keyProduct,String key, String img, int qty, String name, String color, String size, Double price, int discount, Double discountPrice) {
        this.keyProduct = keyProduct;
        this.key = key;
        this.img = img;
        this.qty = qty;
        this.name = name;
        this.color = color;
        this.size = size;
        this.price = price;
        this.discount = discount;
        this.discountPrice = discountPrice;
    }

    public String getKeyProduct() {
        return keyProduct;
    }

    public int getDiscountPrice() {
        return (int)(price*(100-discount)/100)*qty;
    }

    public int getDiscount() {
        return discount;
    }

    public String getKey() {
        return key;
    }

    public String getImg() {
        return img;
    }

    public int getQty() {
        return qty;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public Double getPrice() {
        return price;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void setKeyProduct(String keyProduct) {
        this.keyProduct = keyProduct;
    }

    public void incrementQuantity() {
        this.qty++;
    }

    public void decreaseQuantity()  {
        if(qty == 1) return;
        this.qty--;
    }
}
