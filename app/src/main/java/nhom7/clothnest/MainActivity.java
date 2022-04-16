package nhom7.clothnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

//    Load product lên GridView
//    GridView gridView;
//    ArrayList<Product> productArrayList;
//    GridViewApdater gridViewApdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("setupBnv", "Setup Bottom Navigation View");
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.class, null).commit();
        setupBottomNavigationView();
//        gridView = (GridView)findViewById(R.id.gridview);
//        GetProduct();
    }

    private void setupBottomNavigationView() {
        Log.i("setupBnv", "Setup Bottom Navigation View");

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        break;
                    case R.id.nav_wishlist:
                        selectedFragment = new WishlistFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }

                if (selectedFragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                return true;
            }
        });
    }
//    Thêm sản phẩm vào GridView
//    public void GetProduct(){
//        productArrayList = new ArrayList<>();
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        gridViewApdater = new GridViewApdater(this, R.layout.thumbnail, productArrayList);
//        gridView.setAdapter(gridViewApdater);
//    }
}