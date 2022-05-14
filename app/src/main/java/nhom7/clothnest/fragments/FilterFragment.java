package nhom7.clothnest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.Filter_BrandAdapter;
import nhom7.clothnest.models.Filter_CheckItem;

public class FilterFragment extends Fragment {
    private View mView;
    //view
    private ListView lvBrand;
    private GridView gvSize, gvSizeInches, gvSizeCentimeters, gvColor;
    private SeekBar seekBar;
    //Brand
    private ArrayList<Filter_CheckItem> brandList;
    private Filter_BrandAdapter brandAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_filter, container, false);

        reference();
        getBrands();

        return mView;
    }

    private void getBrands() {
        brandList = new ArrayList<>();
        brandAdapter = new Filter_BrandAdapter(mView.getContext(), brandList);
//        Filter_BrandAdapter.getBrandAndNotify(brandList, brandAdapter);
    }

    private void reference() {
        lvBrand = mView.findViewById(R.id.filter_BrandListView);
        gvSize = mView.findViewById(R.id.filter_sizeGridView);
        gvSizeInches = mView.findViewById(R.id.filter_sizeInchesGridView);
        gvSizeCentimeters = mView.findViewById(R.id.filter_sizeCentimeterGridView);
        gvColor = mView.findViewById(R.id.filter_ColorGridView);
        seekBar = mView.findViewById(R.id.filter_seekbar);
    }

}