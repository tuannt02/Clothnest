package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;
import nhom7.clothnest.R;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.models.Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class SeeAllitemActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<Product> productArrayList;
    GridViewApdater gridViewApdater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_allitem);
        gridView = findViewById(R.id.gridviewSeeAllItem);
        GetProduct();
    }

    public void GetProduct(){
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        gridViewApdater = new GridViewApdater(getApplication(), R.layout.activity_see_allitem, productArrayList);
        gridView.setAdapter(gridViewApdater);
    }
}