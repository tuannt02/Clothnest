package nhom7.clothnest.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Purchase implements Serializable {
    ArrayList<PurchaseItem> items;
    Double total;

    public Purchase(ArrayList<PurchaseItem> items) {
        this.items = items;
    }

    public Purchase(ArrayList<PurchaseItem> items, Double total) {
        this.items = items;
        this.total = total;
    }

    public void calcTotal() {
        Double sum = 0d;
        for (int i = 0; i < items.size(); i++) {
            sum += items.get(i).getPrice();
        }
        this.total = sum;
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
}
