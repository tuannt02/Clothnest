package nhom7.clothnest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    ListView listView;
    ArrayList<CategoryItem> categoryItemArrayList;
    CategoryAdapter categoryAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_category, container, false);

        listView = mView.findViewById(R.id.list_Item);
        GetCategory();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ReplaceFragment(new SearchProductsFragment());
            }
        });

        return mView;
    }

    private void GetCategory() {
        categoryItemArrayList = new ArrayList<>();
        categoryItemArrayList.add(new CategoryItem("WINTER", R.drawable.ic_right_arrow));
        categoryItemArrayList.add(new CategoryItem("LINE", R.drawable.ic_right_arrow));
        categoryItemArrayList.add(new CategoryItem("UT", R.drawable.ic_right_arrow));
        categoryItemArrayList.add(new CategoryItem("UNISEX", R.drawable.ic_right_arrow));
        categoryItemArrayList.add(new CategoryItem("UV PROTECTION", R.drawable.ic_right_arrow));
        categoryAdapter = new CategoryAdapter(getContext(), R.layout.category_item, categoryItemArrayList);
        listView.setAdapter(categoryAdapter);
    }
    private void ReplaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.search_frame, fragment);
        transaction.commit();
    }
}