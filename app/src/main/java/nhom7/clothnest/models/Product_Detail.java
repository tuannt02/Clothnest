package nhom7.clothnest.models;

import android.app.ProgressDialog;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import nhom7.clothnest.activities.Admin_ProductDetailActivity;

public class Product_Detail {
    public static final String COLLECTION_NAME = "products";

    private String id;
    private String category;//để xét similar product
    private String name;
    private double price;
    private int discount;
    private ArrayList<String> imageList;
    private boolean isFavorite;
    private String description;
    private ArrayList<Stock> stockList;
    private String mainImage;

    private ProgressDialog pd;

    public Product_Detail() {
        imageList = new ArrayList<>();
    }

    public Product_Detail(String id, String category, String name, double price, int discount, ArrayList<String> imageList, boolean isFavorite, String description) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.imageList = imageList;
        this.isFavorite = isFavorite;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList.addAll(imageList);
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(ArrayList<Stock> stockList) {
        this.stockList = stockList;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public static ArrayList<ColorClass> getColorsOfProduct(String id) {
        ArrayList<ColorClass> colorList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //get data
        db.collection(Product_Thumbnail.COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<String> list = (ArrayList<String>) document.get("colors");

                                for (String colorUrl : list) {
                                    ColorClass color = new ColorClass();

                                    DocumentReference colorDoRef = db.document(colorUrl);
                                    colorDoRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            color.setHex(documentSnapshot.getString("hex"));
                                            color.setName(documentSnapshot.getString("name"));
                                            colorList.add(color);
                                        }
                                    });
                                }
                            }
                        }
                    }
                });

        return colorList;
    }

    public static ArrayList<SizeClass> getSizesOfProduct(String id) {
        ArrayList<SizeClass> sizeList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //get data
        db.collection(Product_Thumbnail.COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<String> list = (ArrayList<String>) document.get("sizes");

                                for (String sizeUrl : list) {
                                    SizeClass size = new SizeClass();

                                    DocumentReference sizeDoRef = db.document(sizeUrl);
                                    sizeDoRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            size.setName(documentSnapshot.getString("name"));
                                            size.setShort_name(documentSnapshot.getString("short_name"));
                                            sizeList.add(size);
                                        }
                                    });
                                }
                            }
                        }
                    }
                });

        return sizeList;
    }
}
