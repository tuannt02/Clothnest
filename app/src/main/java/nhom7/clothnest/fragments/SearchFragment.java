
package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.CartActivity;
import nhom7.clothnest.adapters.CategoryAdapter;

public class SearchFragment extends Fragment {
    private View mView;
    private ImageView btnCart, btnFilter;
    private FrameLayout filterFrame;
    private Fragment filterFragment, categoryFragment;
    public static FrameLayout searchFrame;
    private FragmentTransaction transaction;
    public static EditText etSearch;

    //Filter
    //size chứa short name, color chứa name
    public static ArrayList<String> selectedSize, selectedColor;
    public static int selectedPrice = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        // use for filter
        selectedColor = new ArrayList<>();
        selectedSize = new ArrayList<>();
        FilterFragment.getMaxSelectedPrice();

        //add category fragment
        transaction = getChildFragmentManager().beginTransaction();
        searchFrame = mView.findViewById(R.id.search_frame);
        categoryFragment = new CategoryFragment();
        transaction.add(R.id.search_frame, categoryFragment).commit();

        reference();
        getEvent();

        return mView;
    }

    private void getEvent() {
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CartActivity.class));
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                filterFragment = getChildFragmentManager().findFragmentByTag(FilterFragment.TAG);

                if (filterFragment != null) {
                    if (filterFragment.isVisible()) {
                        transaction.replace(R.id.filter_frame, new Fragment()).addToBackStack(FilterFragment.TAG);
                        transaction.commit();
                        searchFrame.setClickable(true);
                    } else {
                        getChildFragmentManager().popBackStack();
                        searchFrame.setClickable(false);
                    }
                } else {
                    filterFragment = new FilterFragment();
                    transaction.replace(R.id.filter_frame, filterFragment, FilterFragment.TAG);
                    transaction.commit();
                    searchFrame.setClickable(false);
                }
            }
        });

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                filterFragment = getChildFragmentManager().findFragmentByTag(FilterFragment.TAG);

                if (filterFragment != null) {
                    if (filterFragment.isVisible()) {
                        transaction.replace(R.id.filter_frame, new Fragment()).addToBackStack(FilterFragment.TAG);
                        transaction.commit();
                        searchFrame.setClickable(true);
                    }
                }
            }
        });
    }

    private void reference() {
        btnCart = mView.findViewById(R.id.btnCart);
        btnFilter = mView.findViewById(R.id.btnFilter);
        filterFrame = mView.findViewById(R.id.filter_frame);
        etSearch = mView.findViewById(R.id.searchFragment_etSearch);
    }
}