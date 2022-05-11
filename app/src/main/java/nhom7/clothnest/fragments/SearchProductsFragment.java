package nhom7.clothnest.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.adapters.Product_ThumbnailAdapter;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.activities.ProductDetail_Activity;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.Product_Thumbnail;

public class SearchProductsFragment extends Fragment {
    GridView gridView;
    ImageView btnFilter;
    ArrayList<Product_Thumbnail> productArrayList;
    Product_ThumbnailAdapter thumbnailAdapter;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search_products, container, false);

        View includedView = (View) mView.findViewById(R.id.groupThumbnail);
        gridView = (GridView) includedView.findViewById(R.id.gridview_GroupThumbnail);

        btnFilter = mView.findViewById(R.id.btnFilter);

        getProduct();

        getEvents();

        return mView;
    }

    private void getEvents() {
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i  == KeyEvent.KEYCODE_BACK) {
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.search_frame, new CategoryFragment());
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });
    }

    //    Thêm sản phẩm vào GridView
    public void getProduct(){
        productArrayList = new ArrayList<>();
        thumbnailAdapter = new Product_ThumbnailAdapter(getContext(), productArrayList);
        gridView.setAdapter(thumbnailAdapter);

        Product_ThumbnailAdapter.getProductsWithSameCategory(productArrayList, thumbnailAdapter, CategoryFragment.selectedCategory);
    }

    @Override
    public void onResume() {
        super.onResume();
        getProduct();
    }
}