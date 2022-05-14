package nhom7.clothnest.models;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class PurchaseItem implements Serializable {
    int quantity;
    String name, color, size, image;
    DocumentReference colorRef, productRef, sizeRef;
    Double price;

    public PurchaseItem() {}

    public PurchaseItem(String image, int quantity, String name, Double price) {
        this.image = image;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public DocumentReference getColorRef() {
        return colorRef;
    }

    public void setColorRef(DocumentReference colorRef) {
        this.colorRef = colorRef;
    }

    public DocumentReference getSizeRef() {
        return sizeRef;
    }

    public void setSizeRef(DocumentReference sizeRef) {
        this.sizeRef = sizeRef;
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

    public void setProductRef(DocumentReference productRef) {
        this.productRef = productRef;
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

    public DocumentReference getProductRef() {
        return productRef;
    }
}
