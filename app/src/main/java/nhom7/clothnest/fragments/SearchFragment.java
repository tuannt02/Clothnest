
package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.CartActivity;

public class SearchFragment extends Fragment {
    View mView;
    ImageView btnCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
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

    }

    private void reference() {
        btnCart = mView.findViewById(R.id.btnCart);
    }
}