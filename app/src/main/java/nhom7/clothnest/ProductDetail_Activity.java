package nhom7.clothnest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
<<<<<<< Updated upstream:app/src/main/java/nhom7/clothnest/ProductDetail_Activity.java
import android.widget.ImageView;
=======
import android.widget.ImageButton;
import android.widget.LinearLayout;
>>>>>>> Stashed changes:app/src/main/java/nhom7/clothnest/activities/ProductDetail_Activity.java
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
<<<<<<< Updated upstream:app/src/main/java/nhom7/clothnest/ProductDetail_Activity.java
=======
import nhom7.clothnest.R;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.adapters.SliderAdapter;
<<<<<<< Updated upstream:app/src/main/java/nhom7/clothnest/ProductDetail_Activity.java
import nhom7.clothnest.fragments.CommentFragment;
import nhom7.clothnest.models.Product;
=======
import nhom7.clothnest.models.ProductSlider;
>>>>>>> Stashed changes:app/src/main/java/nhom7/clothnest/activities/ProductDetail_Activity.java
import nhom7.clothnest.models.SliderItem;
>>>>>>> Stashed changes:app/src/main/java/nhom7/clothnest/activities/ProductDetail_Activity.java

public class ProductDetail_Activity extends AppCompatActivity {
    //Slider
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    SliderAdapter sliderAdapter;
    //Similar products
    LinearLayout containersilder;
    ArrayList<Product> productArrayList;
    ProductSlider productSlider;


<<<<<<< Updated upstream:app/src/main/java/nhom7/clothnest/ProductDetail_Activity.java
    Button btnDescription, btnMaterial, btnReviews;
=======
    Button btnDescription, btnReviews;
    ImageButton btnReturn, btnFavorite;
>>>>>>> Stashed changes:app/src/main/java/nhom7/clothnest/activities/ProductDetail_Activity.java

    // Bo sung
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setNestedScrollingEnabled(true);

        viewPager = findViewById(R.id.viewPage);
        circleIndicator = findViewById(R.id.circleIndicator);
        gridView = findViewById(R.id.gridview);
        btnDescription = findViewById(R.id.productDetail_DescriptionBtn);
        btnMaterial = findViewById(R.id.productDetail_MaterialBtn);
        btnReviews = findViewById(R.id.productDetail_ReviewsBtn);

        btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)findViewById(R.id.productDetail_Information)).setText("Description");
            }
        });        btnMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)findViewById(R.id.productDetail_Information)).setText("Material");
            }
        });
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceFragment(new CommentFragment());
            }
        });

        GetProduct();
        ActiveSlider();

        productSlider = new ProductSlider(this, containersilder, productArrayList);
        productSlider.createProductSlider();

    }

    private List<SliderItem> GetListSliderItem() {
        List<SliderItem> list = new ArrayList<>();

        list.add(new SliderItem(R.drawable.product));
        list.add(new SliderItem(R.drawable.product));
        list.add(new SliderItem(R.drawable.product));
        list.add(new SliderItem(R.drawable.product));

        return list;
    }

    public void GetProduct() {
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
    }

    private void ActiveSlider() {
        sliderAdapter = new SliderAdapter(this, GetListSliderItem());
        viewPager.setAdapter(sliderAdapter);
        circleIndicator.setViewPager(viewPager);
<<<<<<< Updated upstream:app/src/main/java/nhom7/clothnest/ProductDetail_Activity.java
        sliderAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());    }
    public void ReplaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.productDetail_frame, fragment);
        transaction.commit();    }
=======
        sliderAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    public void ReplaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.productDetail_frame, fragment);
        transaction.commit();
    }

    public void Reference() {
        productArrayList = new ArrayList<>();

        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setNestedScrollingEnabled(true);

        viewPager = findViewById(R.id.viewPage);
        circleIndicator = findViewById(R.id.circleIndicator);

        btnDescription = findViewById(R.id.productDetail_DescriptionBtn);
        btnReviews = findViewById(R.id.productDetail_ReviewsBtn);
        btnReturn = findViewById(R.id.productDetail_returnBtn);
        btnFavorite = findViewById(R.id.productDetail_favoriteButton);

        containersilder = findViewById(R.id.productDetail_container_slider);
    }

    public void SetEventClick() {
        btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) findViewById(R.id.productDetail_Information)).setText("Description");
            }
        });
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceFragment(new CommentFragment());
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        btnFavorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btnFavorite.setBackgroundColor();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
>>>>>>> Stashed changes:app/src/main/java/nhom7/clothnest/activities/ProductDetail_Activity.java
}