package nhom7.clothnest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class ProductDetail_Activity extends AppCompatActivity {
    //Slider
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    SliderAdapter sliderAdapter;
    //Similar products
    GridView gridView;
    ArrayList<Product> productArrayList;
    GridViewApdater gridViewApdater;

    Button btnDescription, btnMaterial, btnReviews;

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

    }
    private List<SliderItem> GetListSliderItem() {
        List<SliderItem> list = new ArrayList<>();

        list.add(new SliderItem(R.drawable.product));
        list.add(new SliderItem(R.drawable.product));
        list.add(new SliderItem(R.drawable.product));
        list.add(new SliderItem(R.drawable.product));

        return list;
    }
    public void GetProduct(){
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        gridViewApdater = new GridViewApdater(this, R.layout.thumbnail, productArrayList);
        gridView.setAdapter(gridViewApdater);
    }
    private void ActiveSlider(){
        sliderAdapter = new SliderAdapter(this, GetListSliderItem());
        viewPager.setAdapter(sliderAdapter);
        circleIndicator.setViewPager(viewPager);
        sliderAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());    }
    public void ReplaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.productDetail_frame, fragment);
        transaction.commit();    }
}