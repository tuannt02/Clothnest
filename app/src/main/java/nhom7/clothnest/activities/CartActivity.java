package nhom7.clothnest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SnapshotMetadata;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nhom7.clothnest.adapters.CartAdapter;
import nhom7.clothnest.adapters.ColorAdapter;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.models.CartItem;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.ProductSlider;
import nhom7.clothnest.models.PurchaseItem;
import nhom7.clothnest.models.SizeClass;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;
import nhom7.clothnest.util.StringNormalizer;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class CartActivity extends AppCompatActivity {

    ListView lv;
    ImageView btnBack;
    LinearLayout emptyView;
    CartAdapter cartAdapter;
    ArrayList<Product1> productArrayList;
    ArrayList<CartItem> cartItemArrayList;
    ScrollView scrollView;
    CustomProgressBar customProgressBar;
    TextView txtTotalPrice;
    Button btnCheckout;
    //You might like
    LinearLayout containersilder;
    ProductSlider productSlider;

    private ListenerRegistration cartListener;

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
        btnCheckout = findViewById(R.id.cart_btn_checkout);
    }


    private void getCartAndShowOnListview() {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartsRef = db.collection(User.COLLECTION_NAME)
                .document(userInfo.getUid())
                .collection(CartItem.COLLECTION_NAME);


        customProgressBar.show();
        cartsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot value) {
                cartItemArrayList.clear();

                if (value.size() == 0) {
                    customProgressBar.dismiss();
                    updateViewEmpty();
                    return;
                }

                for(DocumentSnapshot documentSnapshot : value.getDocuments())   {
                    CartItem cartItem = new CartItem();

                    String docID = documentSnapshot.getId();
                    cartItem.setKey(docID);

                    cartItem.setQty((int)Math.round(documentSnapshot.getDouble("quantity")));
                    DocumentReference productRef = documentSnapshot.getDocumentReference("product_id");
                    DocumentReference color_selectedRef = documentSnapshot.getDocumentReference("color_selected");
                    DocumentReference size_selectedRef = documentSnapshot.getDocumentReference("size_selected");

                    // Get ref
                    Task<QuerySnapshot> getListColor = productRef.collection("colors").get();
                    Task<QuerySnapshot> getListSize = productRef.collection("sizes").get();
                    Task getInfoCartItem = productRef.get();
                    Task getColorSelected = color_selectedRef.get();
                    Task getSizeSelected = size_selectedRef.get();

                    Task<List<QuerySnapshot>> allTask1 = Tasks.whenAllSuccess(getListColor, getListSize);
                    allTask1.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                        @Override
                        public void onSuccess(List<QuerySnapshot> querySnapshots) {
                            ArrayList<ColorClass> listColor = new ArrayList<>();
                            ArrayList<SizeClass> listSize = new ArrayList<>();

                            // get list color
                            for (DocumentSnapshot docColorSnapshot : querySnapshots.get(0).getDocuments())  {
                                ColorClass colorItem = new ColorClass();
                                colorItem.setName(docColorSnapshot.getString("name"));
                                colorItem.setHex(docColorSnapshot.getString("hex"));
                                colorItem.setColorRef(docColorSnapshot.getReference());

                                listColor.add(colorItem);
                            }
                            cartItem.setListColor(listColor);

                            // get list size
                            for (DocumentSnapshot docSizeSnapshot : querySnapshots.get(1).getDocuments())  {
                                SizeClass sizeItem = new SizeClass();
                                sizeItem.setName(docSizeSnapshot.getString("name"));
                                sizeItem.setShort_name(docSizeSnapshot.getString("short_name"));
                                sizeItem.setSizeRef(docSizeSnapshot.getReference());

                                listSize.add(sizeItem);
                            }
                            cartItem.setListSize(listSize);
                            cartAdapter.notifyDataSetChanged();


                            Task<List<DocumentSnapshot>> allTasks2 = Tasks.whenAllSuccess(getInfoCartItem, getColorSelected, getSizeSelected);
                            allTasks2.addOnSuccessListener(new OnSuccessListener<List<DocumentSnapshot>>() {
                                @Override
                                public void onSuccess(List<DocumentSnapshot> documentSnapshots) {
                                    // Ref Info cart
                                    cartItem.setName(documentSnapshots.get(0).getString("name"));
                                    cartItem.setPrice(documentSnapshots.get(0).getDouble("price"));
                                    cartItem.setImg(documentSnapshots.get(0).getString("main_img"));
                                    cartItem.setDiscount((int)Math.round(documentSnapshots.get(0).getDouble("discount")));

                                    // Ref color selected
                                    ColorClass colorItem = new ColorClass();
                                    colorItem.setHex(documentSnapshots.get(1).getString("hex"));
                                    colorItem.setName(documentSnapshots.get(1).getString("name"));

                                    cartItem.setColorSelected(colorItem);

                                    // Ref size selected
                                    SizeClass sizeItem = new SizeClass();
                                    sizeItem.setShort_name(documentSnapshots.get(2).getString("short_name"));
                                    sizeItem.setName(documentSnapshots.get(2).getString("name"));

                                    cartItem.setSizeSelected(sizeItem);

                                    cartItemArrayList.add(cartItem);
                                    cartAdapter.notifyDataSetChanged();
                                    UpdateTotalPrice();
                                    customProgressBar.dismiss();

                                }
                            });
                        }
                    });




                }
            }
        });



    }

    private void getLikeProducts() {
//        productSlider = new ProductSlider(this, containersilder, productArrayList);
//        productSlider.createProductSlider();
    }

    private void setupClickOnListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
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