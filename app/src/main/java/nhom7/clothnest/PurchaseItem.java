package nhom7.clothnest;

import java.io.Serializable;

public class PurchaseItem implements Serializable {
    int image, qty;
    String title, purchaseDate, color, size;
    Double price;

    public PurchaseItem(int image, int qty, String title, String purchaseDate, String color, String size, Double price) {
        this.image = image;
        this.qty = qty;
        this.title = title;
        this.purchaseDate = purchaseDate;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
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
}
