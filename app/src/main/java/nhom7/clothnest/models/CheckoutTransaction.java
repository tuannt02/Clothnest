package nhom7.clothnest.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;

public class CheckoutTransaction {
    private long deliveryFee, discount, total;
    private DocumentReference addressRef, userRef;
    private String orderDate, status;

    public long getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(long deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @PropertyName("address_ref")
    public DocumentReference getAddressRef() {
        return addressRef;
    }

    @PropertyName("address_ref")
    public void setAddressRef(DocumentReference addressRef) {
        this.addressRef = addressRef;
    }

    public DocumentReference getUserRef() {
        return userRef;
    }

    public void setUserRef(DocumentReference userRef) {
        this.userRef = userRef;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
