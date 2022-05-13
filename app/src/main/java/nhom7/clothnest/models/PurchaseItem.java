package nhom7.clothnest.models;

import java.io.Serializable;

public class PurchaseItem implements Serializable {
    int image, quantity;
    String name, color, size;
    Double price;

    public PurchaseItem(int image, int quantity, String name, String color, String size, Double price) {
        this.image = image;
        this.quantity = quantity;
        this.name = name;
        this.color = color;
        this.size = size;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decreaseQuantity()  {
        if(quantity == 1) return;
        this.quantity--;
    }
}
