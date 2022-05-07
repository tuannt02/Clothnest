package nhom7.clothnest.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import nhom7.clothnest.fragments.CommentFragment;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.R;
import nhom7.clothnest.adapters.SliderAdapter;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.ProductSlider;
import nhom7.clothnest.models.SliderItem;

public class ProductDetail_Activity extends AppCompatActivity {
    //Slider
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    SliderAdapter sliderAdapter;

    //Similar products
    LinearLayout containersilder;
    ArrayList<Product1> productArrayList;
    ProductSlider productSlider;

    Button btnDescription, btnReviews;
    ImageButton btnReturn, btnFavorite;


    // Bo sung
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Reference();
        SetEventClick();
        GetProduct();
        ActiveSlider();

        getSimilarProducts();
        //processDetail();

    }

    private void processDetail() {
        String key = (String) getIntent().getSerializableExtra("key");
        Toast.makeText(getApplicationContext(),"Key " + key, Toast.LENGTH_LONG).show();
    }


    private void getSimilarProducts() {
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

    private void ActiveSlider() {
        sliderAdapter = new SliderAdapter(this, GetListSliderItem());
        viewPager.setAdapter(sliderAdapter);
        circleIndicator.setViewPager(viewPager);
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
        btnReturn = findViewById(R.id.productDetail_returnButton);
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
}