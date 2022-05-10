package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
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
    Product_ThumbnailAdapter arrivalsAdapter;
    Animation scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_item);
        gridView = findViewById(R.id.gridviewSeeAllItem);

        intentData();
        GetProduct();
        AnimationScale();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(), ProductDetail_Activity.class));
            }
        });
    }

    private void AnimationScale()
    {
        scale= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale_list_anim);
        gridView.startAnimation(scale);
    }

    private void intentData()
    {
        Intent intent = getIntent();
        TextView textView = findViewById(R.id.textview2);
        String namearrival= intent.getStringExtra("name");
        textView.setText(""+namearrival);
    }

    private void GetProduct() {
        productArrayList = new ArrayList<>();
        arrivalsAdapter = new Product_ThumbnailAdapter(getApplicationContext(),productArrayList);
        gridView.setAdapter(arrivalsAdapter);
        Product_ThumbnailAdapter.getProductAndPushToGridView(productArrayList,arrivalsAdapter);
    }
}