package nhom7.clothnest.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class PurchaseItem implements Serializable {
    int quantity;
    String name;

    @Exclude
    String color, size, image;

    DocumentReference colorRef, productRef, sizeRef;
    Double price, salePrice;


    public PurchaseItem() {}

    public PurchaseItem(String image, int quantity, String name, Double price) {
        this.image = image;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public PurchaseItem(int quantity, DocumentReference productRef, DocumentReference colorRef, DocumentReference sizeRef, Double price, Double salePrice) {
        this.quantity = quantity;
        this.colorRef = colorRef;
        this.productRef = productRef;
        this.sizeRef = sizeRef;
        this.price = price;
        this.salePrice = salePrice;
    }

    @PropertyName("sale_price")
    public Double getSalePrice() {
        return salePrice;
    }

    @PropertyName("sale_price")
    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }


    @Exclude
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

    @Exclude
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

    @Exclude
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Exclude
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
