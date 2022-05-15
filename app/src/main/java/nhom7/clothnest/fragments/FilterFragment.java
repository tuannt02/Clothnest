package nhom7.clothnest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Filter_CheckItem;

public class FilterFragment extends Fragment {
    private View mView;
    //view
    private GridView gvSize, gvSizeInches, gvSizeCentimeters, gvColor;
    private SeekBar seekBar;
    private Button btnApply;
    //Brand
    private ArrayList<Filter_CheckItem> brandList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_filter, container, false);

        reference();
        getBrands();
        getEvents();

        return mView;
    }

    private void getEvents() {
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getBrands() {
        brandList = new ArrayList<>();
//        Filter_BrandAdapter.getBrandAndNotify(brandList, brandAdapter);
    }

    private void reference() {
        gvSize = mView.findViewById(R.id.filter_sizeGridView);
        gvSizeInches = mView.findViewById(R.id.filter_sizeInchesGridView);
        gvSizeCentimeters = mView.findViewById(R.id.filter_sizeCentimeterGridView);
        gvColor = mView.findViewById(R.id.filter_ColorGridView);
        seekBar = mView.findViewById(R.id.filter_seekbar);
        btnApply = mView.findViewById(R.id.filter_ApplyButton);
    }

}