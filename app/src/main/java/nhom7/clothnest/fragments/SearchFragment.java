package nhom7.clothnest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nhom7.clothnest.R;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_search, container, false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.search_frame, new CategoryFragment());
        transaction.commit();


        return mView;
    }
}