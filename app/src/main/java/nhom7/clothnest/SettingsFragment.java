package nhom7.clothnest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SettingsFragment extends Fragment {
    LinearLayout personalInformation;

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
        setupPersonalInformation(view);
        setupAddressBook(view);
    }

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

    private void setupAddressBook(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.settings_addressBook);

    }

}