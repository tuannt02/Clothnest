package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import nhom7.clothnest.R;
import nhom7.clothnest.fragments.MoreFragment;

public class Admin_MainActivity extends AppCompatActivity {
    BottomNavigationView adminBnv;

    // Declare fragment objects here
    MoreFragment moreFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        adminBnv = findViewById(R.id.bottom_navigation_view);

        // Init fragment objects here
        moreFragment = new MoreFragment();

        // Replace fragment with Product Fragment on start up

        setupBnv();
    }

    private void setupBnv() {
        adminBnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Change fragment here
                switch (item.getItemId()) {
                    case R.id.nav_product:
                        selectedFragment = null;
                        break;
                    case R.id.nav_chat:
                        selectedFragment = null;
                        break;
                    case R.id.nav_transaction:
                        selectedFragment = null;
                        break;
                    case R.id.nav_more:
                        selectedFragment = moreFragment;
                        break;
                }

                if (selectedFragment != null)
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();

                return true;
            }
        });
    }
}