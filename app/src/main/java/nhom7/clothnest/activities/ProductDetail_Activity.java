package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import nhom7.clothnest.fragments.CommentFragment;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.adapters.SliderAdapter;
import nhom7.clothnest.models.SliderItem;

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
    ImageButton btnReturn;

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

    public void Reference(){
        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setNestedScrollingEnabled(true);

        viewPager = findViewById(R.id.viewPage);
        circleIndicator = findViewById(R.id.circleIndicator);
        gridView = findViewById(R.id.gridview_GroupThumbnail);
        btnDescription = findViewById(R.id.productDetail_DescriptionBtn);
        btnMaterial = findViewById(R.id.productDetail_MaterialBtn);
        btnReviews = findViewById(R.id.productDetail_ReviewsBtn);
        btnReturn = findViewById(R.id.productDetail_returnBtn);
    }
    public void SetEventClick(){
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
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}