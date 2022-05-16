package nhom7.clothnest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.Filter_colorAdapter;
import nhom7.clothnest.adapters.Filter_sizeAdapter;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.SizeClass;

public class FilterFragment extends Fragment {
    private View mView;
    //view
    private GridView gvSize, gvSizeInches, gvSizeCentimeters, gvColor;
    private SeekBar seekBar;
    private Button btnApply;
    //Color
    private ArrayList<ColorClass> colorList;
    private Filter_colorAdapter colorAdapter;
    //Size
    private ArrayList<SizeClass> sizeList, inchSizeList, centimeterSizeList;
    private Filter_sizeAdapter sizeAdapter, inchSizeAdapter, centimeterSizeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_filter, container, false);

        reference();

        getColors();
        getSizes();

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

    private void getSizes() {
        sizeList = new ArrayList<>();
        sizeAdapter = new Filter_sizeAdapter(getContext(), sizeList);
        gvSize.setAdapter(sizeAdapter);
        Filter_sizeAdapter.getSizeAndNotify(sizeList, sizeAdapter);

        inchSizeList = new ArrayList<>();
        inchSizeAdapter = new Filter_sizeAdapter(getContext(), inchSizeList);
        gvSizeInches.setAdapter(inchSizeAdapter);
        Filter_sizeAdapter.getInchSizeAndNotify(inchSizeList, inchSizeAdapter);

        centimeterSizeList = new ArrayList<>();
        centimeterSizeAdapter = new Filter_sizeAdapter(getContext(), centimeterSizeList);
        gvSizeCentimeters.setAdapter(centimeterSizeAdapter);
        Filter_sizeAdapter.getCentimeterSizeAndNotify(centimeterSizeList, centimeterSizeAdapter);
    }

    private void getColors() {
        colorList = new ArrayList<>();
        colorAdapter = new Filter_colorAdapter(getContext(), colorList);
        gvColor.setAdapter(colorAdapter);
        Filter_colorAdapter.getColorAndNotify(colorList, colorAdapter);
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