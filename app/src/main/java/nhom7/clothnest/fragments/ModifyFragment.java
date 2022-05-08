package nhom7.clothnest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CategoryAdapter;
import nhom7.clothnest.models.CategoryItem;

public class ModifyFragment extends Fragment {
    View mView;

    ListView lvModify;
    ArrayList<CategoryItem> modifyList;
    CategoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_modify, container, false);

        reference();

        getModifyItems();

        return mView;
    }

    private void reference() {
        lvModify = mView.findViewById(R.id.more_ModifyList);
    }

    private void getModifyItems() {
        modifyList = new ArrayList<>();
        modifyList.add(new CategoryItem("Category", R.drawable.ic_right_arrow));
        modifyList.add(new CategoryItem("New Arrivals", R.drawable.ic_right_arrow));
        modifyList.add(new CategoryItem("Sale", R.drawable.ic_right_arrow));
        modifyList.add(new CategoryItem("Collections", R.drawable.ic_right_arrow));
        modifyList.add(new CategoryItem("Voucher", R.drawable.ic_right_arrow));
        modifyList.add(new CategoryItem("Banners", R.drawable.ic_right_arrow));
        adapter = new CategoryAdapter(getContext(),  R.layout.category_item, modifyList);
        lvModify.setAdapter(adapter);
    }

}