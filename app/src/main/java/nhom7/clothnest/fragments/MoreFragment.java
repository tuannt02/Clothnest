package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.Admin_GrantPermissionsActivity;
import nhom7.clothnest.activities.Admin_ModifyActivity;
import nhom7.clothnest.activities.Admin_StatisticActivity;
import nhom7.clothnest.adapters.CategoryAdapter;
import nhom7.clothnest.models.CategoryItem;

public class MoreFragment extends Fragment {

    LinearLayout itemModify;
    LinearLayout itemStatistic;
    LinearLayout itemGrantPermission;
    Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_more, container, false);

        //Init view
        itemModify = mView.findViewById(R.id.more_ItemModify);
        itemStatistic = mView.findViewById(R.id.more_ItemStatistics);
        itemGrantPermission = mView.findViewById(R.id.more_ItemGrantPermisssion);
        btnLogout = mView.findViewById(R.id.more_Btn_Logout);


        // Set on click
        SetOnClick();


        return mView;
    }


    private void SetOnClick()   {
        itemModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Admin_ModifyActivity.class);
                startActivity(intent);
            }
        });

        itemStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Admin_StatisticActivity.class);
                startActivity(intent);
            }
        });

        itemGrantPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Admin_GrantPermissionsActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}