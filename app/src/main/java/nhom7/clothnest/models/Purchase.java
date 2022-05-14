package nhom7.clothnest.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Purchase implements Serializable {
    ArrayList<PurchaseItem> items;
    String transactionID;
    Double total, discount, deliveryFee;
    String orderDate, status;

    public Purchase() {}

    public Purchase(ArrayList<PurchaseItem> items) {
        this.items = items;
    }

    public Purchase(ArrayList<PurchaseItem> items, Double total, Double discount, Double deliveryFee, String orderDate, String status) {
        this.items = items;
        this.total = total;
        this.discount = discount;
        this.deliveryFee = deliveryFee;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Purchase(ArrayList<PurchaseItem> items, Double total) {
        this.items = items;
        this.total = total;
    }

    public void calcTotal() {
        if (items == null) {
            return;
        }

        Double sum = 0d;
        for (int i = 0; i < items.size(); i++) {
            PurchaseItem item = items.get(i);
            sum += item.getPrice() * item.getQuantity();
        }
        this.total = sum + deliveryFee - discount;
    }

    public ArrayList<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<PurchaseItem> items) {
        this.items = items;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    public Double getDiscount() {
        return discount;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
