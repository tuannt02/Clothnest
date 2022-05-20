package nhom7.clothnest.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CustomWishlistAdapter;
import nhom7.clothnest.adapters.SliderAdapter;
import nhom7.clothnest.fragments.CommentFragment;
import nhom7.clothnest.fragments.HomeFragment;
import nhom7.clothnest.models.ProductSlider;
import nhom7.clothnest.models.Product_Detail;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.SliderItem;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;

public class ProductDetailActivity extends AppCompatActivity {
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


    //Bo sung
    NestedScrollView nestedScrollView;

    //Detail của product và các view liên quan
    Product_Detail productDetail;
    TextView tvName, tvDiscountPrice, tvDiscount, tvDescription;
    ImageButton ibFavorite;
    Fragment reviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        reference();
        //Lấy và xử lý dữ liệu
        processDetail();

        setEventsClick();
    }

    private void processDetail() {
        String productID = (String) getIntent().getSerializableExtra("selected_Thumbnail");

        getProductDetailFromFirestore(productID);
    }

    private void getSimilarProducts() {
        productSlider = new ProductSlider(this, containersilder, productArrayList);
        productSlider.createProductSlider();
        productSlider.getSimilarProduct(productArrayList, productDetail.getCategory());
    }

    private List<SliderItem> GetListSliderItem() {
        List<SliderItem> list = new ArrayList<>();
        try{
            for (String imgUrl : productDetail.getImageList()) {
                list.add(new SliderItem(imgUrl));
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return list;
    }

    private void ActiveSlider() {
        sliderAdapter = new SliderAdapter(this, GetListSliderItem());
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

        reviewFragment = new CommentFragment();
    }

    public void setEventsClick() {
        btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFragment(reviewFragment);
                ((TextView) findViewById(R.id.productDetail_Description)).setText(productDetail.getDescription());
            }
        });
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            if(task.isSuccessful()){
                                if(!task.getResult().isEmpty()){
                                    for(QueryDocumentSnapshot document: task.getResult()){
                                        Map<String, Object> object = document.getData();
                                        Wishlist.removeWishlistItemFromFirestore(document.getId());
                                        productDetail.setFavorite(false);
                                        ibFavorite.setImageResource(R.drawable.favorite);
                                    }
                                }
                            }
                        }
                    });
        } else {
            Wishlist.addProductToWishlist(productDetail.getId());
            productDetail.setFavorite(true);
            ibFavorite.setImageResource(R.drawable.is_favorite);
        }
    }

    public void getProductDetailFromFirestore(String id) {
        productDetail = new Product_Detail();

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
                                //set ID
                                productDetail.setId(id);

                                //set category
                                DocumentReference categoryDoRef = document.getDocumentReference("category");
                                categoryDoRef
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                productDetail.setCategory(documentSnapshot.getString("name"));
                                                getSimilarProducts();
                                            }
                                        });

                                //set name
                                productDetail.setName(document.getString("name"));
                                tvName.setText(productDetail.getName());

                                //set discount
                                int discount = (int) Math.round(document.getDouble("discount"));
                                productDetail.setDiscount(discount);
                                tvDiscount.setText("-" + discount + "%");

                                //set price
                                Double price = Double.parseDouble(String.valueOf(document.getDouble("price")));
                                productDetail.setPrice(price);

                                Double discountPrice = price * (1 - discount / 100.0);
                                tvDiscountPrice.setText("$" + discountPrice.toString().replaceAll("\\.?[0-9]*$", ""));


                                //set imageList
                                ArrayList<String> list = (ArrayList<String>) document.get("imgs");
                                productDetail.setImageList(list);
                                ActiveSlider();

                                //set isFavorite
                                FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
                                DocumentReference doRef = db.document(Product_Thumbnail.COLLECTION_NAME + '/' + document.getId());

                                CollectionReference coRef_wishList = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);
                                Query query = coRef_wishList.whereEqualTo("product_id", doRef);
                                query.limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            productDetail.setFavorite(!task.getResult().isEmpty());
                                            if (productDetail.isFavorite())
                                                ibFavorite.setImageResource(R.drawable.is_favorite);
                                        }
                                    }
                                });

                                //set description
                                productDetail.setDescription(document.getString("desc"));
                                tvDescription.setText(productDetail.getDescription());
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}