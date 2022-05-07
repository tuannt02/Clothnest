package nhom7.clothnest.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import nhom7.clothnest.adapters.CartAdapter;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.models.CartItem;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.ProductSlider;
import nhom7.clothnest.models.PurchaseItem;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;
import nhom7.clothnest.util.StringNormalizer;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class CartActivity extends AppCompatActivity {

    public ListView lv;
    ImageView btnBack;
    LinearLayout emptyView;
    CartAdapter cartAdapter;
    ArrayList<Product1> productArrayList;
    ArrayList<CartItem> cartItemArrayList;
    ScrollView scrollView;
    CustomProgressBar customProgressBar;
    TextView txtTotalPrice;
    //You might like
    LinearLayout containersilder;
    ProductSlider productSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initUi();
        setupClickOnListener();
        setupScrollViewListener();
        getProductPropose();

//        cartItemArrayList = getProductToDisplayListview();

        cartAdapter = new CartAdapter(CartActivity.this, R.layout.cart_item, cartItemArrayList, new CartAdapter.ICLickListenerOnItemListview() {
            @Override
            public void addItemToWishlist(String keyProduct) {
                Wishlist.addProductToWishlist(keyProduct);
            }
            @Override
            public void removeItem(int position, String docID) {
                cartItemArrayList.remove(position);
                cartAdapter.notifyDataSetChanged();
                removeCartItemFromFirestore(docID);
                updateViewEmpty();
            }
            @Override
            public void updateTotalPrice()  {
                UpdateTotalPrice();
            }
        });
        lv.setAdapter(cartAdapter);

        getCartAndShowOnListview();
        getLikeProducts();
    }

    private void initUi()   {
        lv = (ListView) findViewById(R.id.listview_cart);
        btnBack = findViewById(R.id.btnBackCart);
        emptyView = findViewById(R.id.cart_view_empty);
        scrollView = findViewById(R.id.cart_main_scrview);
        containersilder = findViewById(R.id.cart_container_slider);
        cartItemArrayList = new ArrayList<>();
        customProgressBar = new CustomProgressBar(CartActivity.this);
        txtTotalPrice = findViewById(R.id.cart_txt_total_price);
    }

    private void getCartAndShowOnListview() {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        customProgressBar.show();
        // Lấy tất cả các cartItem của một user
        db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + CartItem.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Lặp qua từng document
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartItem cartItem = new CartItem();
                                Map<String, Object> tempObject;
                                tempObject = document.getData();

                                String docID = document.getId();
                                cartItem.setKey(docID);

                                // Lặp qua từng field của một document
                                Iterator myVeryOwnIterator = tempObject.keySet().iterator();
                                while(myVeryOwnIterator.hasNext()) {
                                    String key=(String)myVeryOwnIterator.next();

                                    if(key.equals("quantity"))  {
                                        int qtt = (int)Math.round(document.getDouble("quantity"));
                                        cartItem.setQty(qtt);
                                    }

                                    if (key.equals("product_id"))    {
                                        DocumentReference docRef = (DocumentReference) tempObject.get(key);
                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String keyProduct = documentSnapshot.getId();
                                                String name = documentSnapshot.getString("name");
                                                String main_img = documentSnapshot.getString("main_img");
                                                Double price = documentSnapshot.getDouble("price");
                                                int discount = (int)Math.round(documentSnapshot.getDouble("discount"));

                                                cartItem.setKeyProduct(keyProduct);
                                                cartItem.setName(name);
                                                cartItem.setImg(main_img);
                                                cartItem.setPrice(price);
                                                cartItem.setDiscount(discount);
                                                cartItemArrayList.add(cartItem);
                                                cartAdapter.notifyDataSetChanged();
                                                updateViewEmpty();
                                                UpdateTotalPrice();
                                            }
                                        });
                                    }


                                }

                            }
                            customProgressBar.dismiss();
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void getLikeProducts() {
        productSlider = new ProductSlider(this, containersilder, productArrayList);
        productSlider.createProductSlider();
    }

    private void setupClickOnListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupScrollViewListener()  {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if(scrollY > 270) {
                    if(emptyView.getVisibility() == View.VISIBLE)
                        emptyView.setVisibility(View.GONE);
                }
                else    {
                    updateViewEmpty();
                }
            }
        });
    }

    private void getProductPropose()    {
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));

    }

    private void updateViewEmpty()  {
        if(cartItemArrayList.size() == 0)    {
            emptyView.setVisibility(View.VISIBLE);
        }
        else    {
            emptyView.setVisibility(View.GONE);
        }
    }

    private void UpdateTotalPrice() {
        int totalPrice = 0;
        for(int i = 0; i < cartItemArrayList.size(); i++)   {
            CartItem cartItem = cartItemArrayList.get(i);
            totalPrice += cartItem.getDiscountPrice();
        }

        txtTotalPrice.setText("$ " + StringNormalizer.convertPrice(totalPrice));
    }

    private void removeCartItemFromFirestore(String docID)  {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + CartItem.COLLECTION_NAME)
                .document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CartActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, "Delete fails", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}