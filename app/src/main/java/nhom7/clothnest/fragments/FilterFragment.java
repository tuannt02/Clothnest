package nhom7.clothnest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.Filter_colorAdapter;
import nhom7.clothnest.adapters.Filter_sizeAdapter;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.SizeClass;

public class FilterFragment extends Fragment {
    public static final String TAG = FilterFragment.class.getName();
    private View mView;
    private TextView tvSeekBarMax, tvPriceRange;
    //view
    private GridView gvSize, gvSizeInches, gvSizeCentimeters, gvColor;
    public static SeekBar seekBar;
    private Button btnApply;
    //Color
    private ArrayList<ColorClass> colorList;
    private Filter_colorAdapter colorAdapter;
    //Size
    private ArrayList<SizeClass> sizeList, inchSizeList, centimeterSizeList;
    private Filter_sizeAdapter sizeAdapter, inchSizeAdapter, centimeterSizeAdapter;
    //price
    private int max = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_filter, container, false);

        reference();

        getData();

        getEvents();

        SearchFragment.selectedPrice = seekBar.getMax();

        return mView;
    }

    private void getEvents() {
        setOnColorClickListener();

        setOnPriceSeekBar();

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle data

                //change fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.filter_frame, new Fragment());
                transaction.replace(R.id.search_frame, new SearchProductsFragment());
                transaction.commit();
            }
        });
    }

    private void getData() {
        getColors();
        getSizes();
        getMaxPrice();
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

    private void getMaxPrice() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getDouble("price") > max) {
                                    max = (int) Math.round(document.getDouble("price"));
                                    seekBar.setMax(max);
                                    tvSeekBarMax.setText(max + "");
                                    tvPriceRange.setText("Price range: 0 - " + max);
                                }
                            }
                        }
                    }
                });
    }

    public static void getMaxSelectedPrice(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getDouble("price") > SearchFragment.selectedPrice) {
                                    SearchFragment.selectedPrice = (int) Math.round(document.getDouble("price"));
                                }
                            }
                        }
                    }
                });
    }

    private void setOnColorClickListener() {
        gvColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean check = colorList.get(i).isSelected();

                if (check) {
                    colorList.get(i).setSelected(false);

                    if (SearchFragment.selectedColor.contains(colorList.get(i).getName()))
                        SearchFragment.selectedColor.remove(colorList.get(i).getName());
                } else {
                    colorList.get(i).setSelected(true);

                    if (!SearchFragment.selectedColor.contains(colorList.get(i).getName()))
                        SearchFragment.selectedColor.add(colorList.get(i).getName());
                }
                colorAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setOnPriceSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPriceRange.setText("Price range: 0 - " + i);
                SearchFragment.selectedPrice = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void reference() {
        tvSeekBarMax = mView.findViewById(R.id.filter_seekbar_max);
        tvPriceRange = mView.findViewById(R.id.filter_tvPriceRange);
        gvSize = mView.findViewById(R.id.filter_sizeGridView);
        gvSizeInches = mView.findViewById(R.id.filter_sizeInchesGridView);
        gvSizeCentimeters = mView.findViewById(R.id.filter_sizeCentimeterGridView);
        gvColor = mView.findViewById(R.id.filter_ColorGridView);
        seekBar = mView.findViewById(R.id.filter_seekbar);
        btnApply = mView.findViewById(R.id.filter_ApplyButton);
    }

    public static boolean checkFilter(Product_Thumbnail thumbnail) {
        if (thumbnail.getPrice() <= SearchFragment.selectedPrice) {
            if (SearchFragment.selectedColor.size() == 0) {
                if (SearchFragment.selectedColor.size() == 0) {
                    return true;
                }
                else {
                    for (String size : thumbnail.getSizeList())
                        if (SearchFragment.selectedSize.contains(size)) {
                            return true;
                        }
                    return false;
                }
            }
            else {
                for (String color : thumbnail.getColorList())
                    if (SearchFragment.selectedColor.contains(color)) {
                        if(SearchFragment.selectedSize.size() == 0)
                            return true;
                        else{
                            for (String size : thumbnail.getSizeList())
                                if (SearchFragment.selectedSize.contains(size)) {
                                    return true;
                                }
                            return false;
                        }
                    }
                return false;
            }
        }
        return false;
    }
}