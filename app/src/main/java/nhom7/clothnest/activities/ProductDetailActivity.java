package nhom7.clothnest.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import nhom7.clothnest.R;
import nhom7.clothnest.adapters.SliderAdapter;
import nhom7.clothnest.fragments.CommentFragment;
import nhom7.clothnest.fragments.WriteCommentFragment;
import nhom7.clothnest.models.ProductSlider;
import nhom7.clothnest.models.Product_Detail;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;
import nhom7.clothnest.notifications.NetworkChangeReceiver;
import nhom7.clothnest.util.AddToCart;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class ProductDetailActivity extends AppCompatActivity {
    public static String productID;
    //Slider
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    SliderAdapter sliderAdapter;

    //Similar products
    LinearLayout containersilder;
    ArrayList<Product_Thumbnail> productArrayList;
    ProductSlider productSlider;

    Button btnDescription, btnReviews;
    ImageButton btnReturn, btnFavorite;
    ImageView ivAddToCart;


    //Bo sung
    NestedScrollView nestedScrollView;

    //Detail của product và các view liên quan
    Product_Detail productDetail;
    TextView tvName, tvDiscountPrice, tvDiscount;
    public static TextView tvDescription, tvStarNumber, tvReviewNumber;
    ImageButton ibFavorite;
    Fragment reviewFragment;
    CustomProgressBar dialog;
    static Double starNumber;


    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        dialog = new CustomProgressBar(ProductDetailActivity.this);

        reference();
        //Lấy và xử lý dữ liệu
        processDetail();

        setEventsClick();

        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void processDetail() {
        productID = (String) getIntent().getSerializableExtra("selected_Thumbnail");

        getProductDetail(productID);

        String write_review = (String) getIntent().getSerializableExtra("write_review");
        if (write_review.compareTo("write_review") == 0) {
            Fragment fragment = new WriteCommentFragment();
            replaceFragment(fragment);
        }
    }

    public static void writeAReview(Context context, String productID){
        Intent intent_productDetail = new Intent(context, ProductDetailActivity.class);
        intent_productDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent_productDetail.putExtra("selected_Thumbnail", productID);
        intent_productDetail.putExtra("write_review", "write_review");
        context.startActivity(intent_productDetail);
    }

    private void getSimilarProducts() {
        productSlider = new ProductSlider(this, containersilder, productArrayList);
        productSlider.createProductSlider();
        productSlider.getSimilarProduct(productArrayList, productDetail.getCategory());
    }

    private void ActiveSlider() {
        sliderAdapter = new SliderAdapter(this, productDetail.getImageList());
        viewPager.setAdapter(sliderAdapter);
        circleIndicator.setViewPager(viewPager);
        sliderAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.productDetail_frame, fragment);
        transaction.commit();
    }

    public void removeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public void reference() {
        productArrayList = new ArrayList<>();

        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setNestedScrollingEnabled(true);

        viewPager = findViewById(R.id.viewPage);
        circleIndicator = findViewById(R.id.circleIndicator);

        btnDescription = findViewById(R.id.productDetail_DescriptionBtn);
        btnReviews = findViewById(R.id.productDetail_ReviewsBtn);
        btnReturn = findViewById(R.id.productDetail_returnButton);
        btnFavorite = findViewById(R.id.productDetail_favoriteButton);

        containersilder = findViewById(R.id.productDetail_container_slider);

        //Detail của product và các view liên quan
        productDetail = new Product_Detail();

        tvName = findViewById(R.id.productDetail_ProductName);
        tvDiscountPrice = findViewById(R.id.productDetail_DiscountPrice);
        tvDiscount = findViewById(R.id.productDetail_discount);
        tvDescription = findViewById(R.id.productDetail_Description);
        ibFavorite = findViewById(R.id.productDetail_favoriteButton);

        ivAddToCart = findViewById(R.id.productDetail_ivAddToCart);
        tvStarNumber = findViewById(R.id.productDetail_starNummber);
        tvReviewNumber = findViewById(R.id.productDetail_reviewNumber);

        reviewFragment = new CommentFragment();
    }

    public void setEventsClick() {
        btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDescription.setVisibility(View.VISIBLE);
                removeFragment(reviewFragment);
                removeFragment(CommentFragment.writeCommentFragment);
                tvDescription.setText(productDetail.getDescription());
            }
        });
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDescription.setVisibility(View.INVISIBLE);
                replaceFragment(reviewFragment);
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFavorite();
            }
        });
        ivAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCart addToCartDialog = new AddToCart(ProductDetailActivity.this, productID);
            }
        });
    }

    private void setFavorite() {
        if (productDetail.isFavorite()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference docRefProduct = db.document(Product_Thumbnail.COLLECTION_NAME + '/' + productDetail.getId());

            db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME)
                    .whereEqualTo("product_id", docRefProduct)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!task.getResult().isEmpty()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Map<String, Object> object = document.getData();
                                        Wishlist.removeWishlistItemFromFirestore(ProductDetailActivity.this, document.getId());
                                        productDetail.setFavorite(false);
                                        ibFavorite.setImageResource(R.drawable.favorite);
                                    }
                                }
                            }
                        }
                    });
        } else {
            Wishlist.addProductToWishlist(ProductDetailActivity.this, productDetail.getId());
            productDetail.setFavorite(true);
            ibFavorite.setImageResource(R.drawable.is_favorite);
        }
    }

    public void getProductDetail(String productId) {
        dialog.show();

        productDetail = new Product_Detail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference product_Ref = db.collection(Product_Thumbnail.COLLECTION_NAME).document(productId);

        product_Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot product_Doc) {
                StorageReference storage_Ref = storage.getReference("products/" + productId);
                DocumentReference category_Ref = product_Doc.getDocumentReference("category");
                CollectionReference wishList_Ref = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);

                Task<ListResult> getImages = storage_Ref.listAll();
                Task<QuerySnapshot> getFavorite = wishList_Ref.whereEqualTo("product_id", product_Ref).limit(1).get();

                //set category
                category_Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        productDetail.setCategory(documentSnapshot.getString("name"));

                        //set favorite
                        getFavorite.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    productDetail.setFavorite(!task.getResult().isEmpty());
                                    //set other info
                                    productDetail.setId(product_Doc.getId());
                                    productDetail.setName(product_Doc.getString("name"));
                                    productDetail.setDiscount((int) Math.round(product_Doc.getDouble("discount")));
                                    productDetail.setPrice(Double.parseDouble(String.valueOf(product_Doc.getDouble("price"))));
                                    productDetail.setDescription(product_Doc.getString("desc"));
                                    //set images
                                    getImages.addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                        @Override
                                        public void onSuccess(ListResult listResult) {
                                            ArrayList<String> listUri = new ArrayList<>();
                                            for (StorageReference image_Ref : listResult.getItems()) {
                                                image_Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        listUri.add(uri.toString());
                                                        if (listUri.size() == listResult.getItems().size()) {
                                                            productDetail.setImageList(listUri);
                                                            showData();
                                                            if (dialog.isShowing())
                                                                dialog.dismiss();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });

        getReviewInfo(productId);
    }

    public static void getReviewInfo(String productID) {
        FirebaseFirestore.getInstance().collection("products").document(productID).collection("reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            tvReviewNumber.setText("(" + task.getResult().size() + " reviews)");
                            starNumber = 0.0;
                            for (QueryDocumentSnapshot review_Ref : task.getResult()) {
                                DocumentReference review = review_Ref.getDocumentReference("review_ref");
                                review.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot review_Doc) {
                                        starNumber += review_Doc.getDouble("star_number");

                                        if (task.getResult().getDocuments().indexOf(review_Ref) == task.getResult().size() - 1) {
                                            Double rate = starNumber * 10 / task.getResult().size();
                                            tvStarNumber.setText((double) Math.round(rate) / 10 + "");
                                        }
                                    }
                                });
                            }
                        }
                    }
                });


    }

    private void showData() {
        ActiveSlider();
        getSimilarProducts();
        tvName.setText(productDetail.getName());
        tvDiscount.setText("-" + productDetail.getDiscount() + "%");
        Double discountPrice = productDetail.getPrice() * (1 - productDetail.getDiscount() / 100.0);
        tvDiscountPrice.setText("$" + discountPrice.toString().replaceAll("\\.?[0-9]*$", ""));
        tvDescription.setText(productDetail.getDescription());
        if (productDetail.isFavorite())
            ibFavorite.setImageResource(R.drawable.is_favorite);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}