package nhom7.clothnest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SettingsFragment extends Fragment {
    LinearLayout personalInformation, termsAndConditions, privacyPolicy, imprint, governmentApproval, about;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
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
}