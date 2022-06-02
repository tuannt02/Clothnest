package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import nhom7.clothnest.R;
import nhom7.clothnest.fragments.Admin_ProductsFragment;
import nhom7.clothnest.fragments.ChatListFragment;
import nhom7.clothnest.fragments.HomeFragment;
import nhom7.clothnest.fragments.MoreFragment;
import nhom7.clothnest.fragments.TransactionFragment;
import nhom7.clothnest.notifications.NetworkChangeReceiver;

public class Admin_MainActivity extends AppCompatActivity {
    BottomNavigationView adminBnv;
    // Declare fragment objects here
    TransactionFragment  transactionFragment;
    MoreFragment moreFragment;
    ChatListFragment chatListFragment;
    Admin_ProductsFragment adminProductsFragment;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        adminBnv = findViewById(R.id.bottom_navigation_view);

        // Init fragment objects here
        transactionFragment = new TransactionFragment();
        moreFragment = new MoreFragment();
        chatListFragment = new ChatListFragment();
        adminProductsFragment = new Admin_ProductsFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Admin_ProductsFragment.class, null).commit();
        // Replace fragment with Product Fragment on start up
        setupBnv();

        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void setupBnv() {
        adminBnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Change fragment here
                switch (item.getItemId()) {
                    case R.id.nav_product:
                        selectedFragment = adminProductsFragment;
                        break;
                    case R.id.nav_chat:
                        selectedFragment = chatListFragment;
                        break;
                    case R.id.nav_transaction:
                        selectedFragment = transactionFragment;
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