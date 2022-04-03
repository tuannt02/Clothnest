package nhom7.clothnest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileFragment extends Fragment {
    ImageButton ibEditProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getChildFragmentManager().beginTransaction().replace(R.id.profile_fragment_container, new PurchasesFragment()).commit();

        BottomNavigationView profileNavigationView = view.findViewById(R.id.profile_navigation_view);
        profileNavigationView.setOnItemSelectedListener(navListener);

        setupEditProfile(view);
    }

    private void setupEditProfile(View view) {
        ibEditProfile = view.findViewById(R.id.imagebutton_editProfile);
        ibEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_purchases:
                    selectedFragment = new PurchasesFragment();
                    break;
                case R.id.nav_settings:
                    selectedFragment = new SettingsFragment();
                    break;
            }

            getChildFragmentManager().beginTransaction().replace(R.id.profile_fragment_container, selectedFragment).commit();

            return true;
        }
    };
}