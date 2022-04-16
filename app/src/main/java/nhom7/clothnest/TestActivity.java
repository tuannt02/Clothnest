package nhom7.clothnest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    private ArrayList<Product> products;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // set data
        setDataForList();

        // review this method to know how we can implement a slider
        createProductSlider();
    }

    private void createProductSlider() {
        // init UI
        container = findViewById(R.id.linearLayout_container_testActivity);

        // create product slider
        ProductSlider productSlider = new ProductSlider(this, container, products);
        productSlider.createProductSlider();
    }

    private void setDataForList() {
        products = new ArrayList<>();
        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
    }
}