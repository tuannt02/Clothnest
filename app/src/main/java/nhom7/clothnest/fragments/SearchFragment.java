
package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.CartActivity;

public class SearchFragment extends Fragment {
    View mView;
    ImageView btnCart, btnFilter;
    FrameLayout filterFrame;
    Fragment filterFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.search_frame, new CategoryFragment());
        transaction.commit();
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
                String tag = "already";
                filterFragment = getChildFragmentManager().findFragmentByTag(tag);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                if (filterFragment == null) {
                    filterFragment = new FilterFragment();
                    transaction.replace(R.id.filter_frame, filterFragment, tag).commit();
                }else
                {
                    transaction.remove(filterFragment).commit();
                }
            }
        });

    }

    private void reference() {
        btnCart = mView.findViewById(R.id.btnCart);
        btnFilter = mView.findViewById(R.id.btnFilter);
        filterFrame = mView.findViewById(R.id.filter_frame);
    }
}