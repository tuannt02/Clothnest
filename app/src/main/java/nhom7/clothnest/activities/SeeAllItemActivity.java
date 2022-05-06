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
import nhom7.clothnest.models.Product;
import nhom7.clothnest.models.Product1;

public class SeeAllItemActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<Product1> productArrayList;
    GridViewApdater gridViewApdater;
    Animation scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_item);
        gridView = findViewById(R.id.gridviewSeeAllItem);


        Intent intent = getIntent();
        TextView textView = findViewById(R.id.textview2);
        String namearrival= intent.getStringExtra("name");
        textView.setText(""+namearrival);
        GetProduct();

        scale= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale_list_anim);
        gridView.startAnimation(scale);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(), ProductDetail_Activity.class));
            }
        });
    }

    private void GetProduct() {
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
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));

        gridViewApdater = new GridViewApdater(getApplication(), R.layout.thumbnail, productArrayList);
        gridView.setAdapter(gridViewApdater);

    }
}