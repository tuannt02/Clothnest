package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.adapters.Product_ThumbnailAdapter;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.Product_Thumbnail;

public class SeeAllItemActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<Product_Thumbnail> productArrayList;
    Product_ThumbnailAdapter adapter;
    Animation scale;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_item);
        reference();
        intentData();
        GetProduct();
        AnimationScale();

    }


    private void reference() {
        gridView = findViewById(R.id.gridviewSeeAllItem);

    }

    private void AnimationScale() {
        scale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_list_anim);
        gridView.startAnimation(scale);
    }

    private void intentData() {
        Intent intent = getIntent();
        TextView textView = findViewById(R.id.textview2);
        name = intent.getStringExtra("name");
        textView.setText("" + name);
    }

    private void GetProduct() {
        productArrayList = new ArrayList<>();
        adapter = new Product_ThumbnailAdapter(getApplicationContext(), productArrayList);
        gridView.setAdapter(adapter);
        if (name.compareTo("NEW ARRIVALS") == 0)
            Product_ThumbnailAdapter.getProductArrivalAndPushToGridView(productArrayList, adapter);
        else
            Product_ThumbnailAdapter.getProductSalesAndPushToGridView(productArrayList, adapter);
    }
}