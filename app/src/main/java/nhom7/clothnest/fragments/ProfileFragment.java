package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.EditProfileActivity;
import nhom7.clothnest.util.Constants;

public class ProfileFragment extends Fragment {
    ImageButton ibEditProfile;
    PurchasesFragment purchasesFragment;
    SettingsFragment settingsFragment;
    Fragment selectedFragment = null;
    TextView tvFullName;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        purchasesFragment = new PurchasesFragment();
        settingsFragment = new SettingsFragment();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Assign UI Views
        tvFullName = view.findViewById(R.id.textview_fullname);
        FirebaseFirestore.getInstance().collection("users")
                .document(Constants.getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tvFullName.setText(documentSnapshot.getString("name").toUpperCase());
                    }
                });

        if (selectedFragment == null)
            selectedFragment = purchasesFragment;

        getChildFragmentManager().beginTransaction().replace(R.id.profile_fragment_container, selectedFragment).commit();

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
            switch (item.getItemId()) {
                case R.id.nav_purchases:
                    selectedFragment = purchasesFragment;
                    break;
                case R.id.nav_settings:
                    selectedFragment = settingsFragment;
                    break;
            }

            getChildFragmentManager().beginTransaction().replace(R.id.profile_fragment_container, selectedFragment).commit();

            return true;
        }
    };
}