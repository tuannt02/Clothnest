package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.AboutActivity;
import nhom7.clothnest.activities.AddressBookActivity;
import nhom7.clothnest.activities.EditProfileActivity;
import nhom7.clothnest.activities.GovernmentApprovalActivity;
import nhom7.clothnest.activities.ImprintActivity;
import nhom7.clothnest.activities.PrivacyPolicyActivity;
import nhom7.clothnest.activities.SignInActivity;
import nhom7.clothnest.activities.TermsAndConditionsActivity;

public class SettingsFragment extends Fragment {
    LinearLayout personalInformation, termsAndConditions, privacyPolicy, imprint, governmentApproval, about;
    Button btnLogout;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupClickListener(view);
    }


    private void setupClickListener(View view) {
        // Set up click event cho cac danh muc
        setupPersonalInformation(view);
        setupAddressBook(view);
        setupTermsAndConditions(view);
        setupPrivacyPolicy(view);
        setupImprint(view);
        setupGovernmentApproval(view);
        setupAbout(view);
        setupBtnLogout(view);

    }

    // Thiet lap About button
    private void setupAbout(View view) {
        about = view.findViewById(R.id.settings_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    // Thiet lap personal information button
    private void setupPersonalInformation(View view) {
        // setup personal information click
        personalInformation = view.findViewById(R.id.settings_personalInfomation);
        personalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    // Thiet lap address book button
    private void setupAddressBook(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.settings_addressBook);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddressBookActivity.class);
                startActivity(intent);
            }
        });
    }

    // Thiet lap Terms and Conditions button
    private void setupTermsAndConditions(View view) {
        termsAndConditions = view.findViewById(R.id.settings_termsAndConditions);
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TermsAndConditionsActivity.class);
                startActivity(intent);
            }
        });
    }

    // Thiet lap Privacy Policy button
    private void setupPrivacyPolicy(View view) {
        privacyPolicy = view.findViewById(R.id.settings_privacyPolicy);
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
    }

    // Thiet lap Imprint button
    private void setupImprint(View view) {
        imprint = view.findViewById(R.id.settings_imprint);
        imprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImprintActivity.class);
                startActivity(intent);
            }
        });
    }

    // Thiet lap Government Approval button
    private void setupGovernmentApproval(View view) {
        governmentApproval = view.findViewById(R.id.settings_governmentApproval);
        governmentApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GovernmentApprovalActivity.class);
                startActivity(intent);
            }
        });
    }

    // Thiet lap su kien onclick cho btn Logout
    private void setupBtnLogout(View view)  {
        btnLogout = view.findViewById(R.id.btn_Logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}