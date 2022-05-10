package nhom7.clothnest.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nhom7.clothnest.R;

public class Wishlist {

    public static final String COLLECTION_NAME = "wishlist";

    private String key;
    private String productName;
    private String productImage;
    private double regularCost;
    private int discount;
    private double discountCost;
    private Timestamp date_add;

    public Wishlist() {

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
        return regularCost * (100 - discount) / 100;
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
        return "$  " + String.valueOf(regularCost * (100 - discount) / 100);
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

    public static void addProductToWishlist(String keyProduct) {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("products").document(keyProduct);

        db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME)
                .whereEqualTo("product_id", docRef)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                Map<String, Object> newWishlistItem = new HashMap<>();
                                newWishlistItem.put("date_add", new Timestamp(new Date()));
                                newWishlistItem.put("product_id", docRef);

                                db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME)
                                        .add(newWishlistItem)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                System.out.println("Them vap wishlist thanh cong");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                System.out.println("Them vap wishlist that bai");
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    public static void removeWishlistItemFromFirestore(String wishlistID) {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME)
                .document(wishlistID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                        Toast.makeText(getContext(), "Delete success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), "Delete fails", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
