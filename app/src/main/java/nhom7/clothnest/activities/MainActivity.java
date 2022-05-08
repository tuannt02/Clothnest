package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import nhom7.clothnest.fragments.HomeFragment;
import nhom7.clothnest.fragments.ProfileFragment;
import nhom7.clothnest.R;
import nhom7.clothnest.fragments.SearchFragment;
import nhom7.clothnest.fragments.WishlistFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    SearchFragment searchFragment;
    WishlistFragment wishlistFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("setupBnv", "Setup Bottom Navigation View");
        setContentView(R.layout.activity_main);

        // init fragments
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        wishlistFragment = new WishlistFragment();
        profileFragment = new ProfileFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.class, null).commit();
        setupBottomNavigationView();
    }

    private void setupBottomNavigationView(){
        Log.i("setupBnv", "Setup Bottom Navigation View");

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = homeFragment;
                        break;
                    case R.id.nav_search:
                        selectedFragment = searchFragment;
                        break;
                    case R.id.nav_wishlist:
                        selectedFragment = wishlistFragment;
                        break;
                    case R.id.nav_profile:
                        selectedFragment = profileFragment;
                        break;
                }
                if (selectedFragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;            }
        });
    }
}