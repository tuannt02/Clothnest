package nhom7.clothnest.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.activities.ProductDetail_Activity;
import nhom7.clothnest.models.Product1;

public class SearchProductsFragment extends Fragment {
    GridView gridView;
    ImageView btnFilter;
    ArrayList<Product1> productArrayList;
    GridViewApdater gridViewApdater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_search_products, container, false);

        View includedView = (View) mView.findViewById(R.id.groupThumbnail);
        gridView = (GridView) includedView.findViewById(R.id.gridview_GroupThumbnail);

        btnFilter = mView.findViewById(R.id.btnFilter);

        GetProduct();
        Button button = mView.findViewById(R.id.searchProduct_WinterBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProductDetail_Activity.class));
            }
        });

        return mView;
    }

    private void getEvents() {

    }

    //    Thêm sản phẩm vào GridView
    public void GetProduct(){
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("2", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("3", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("4", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("5", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("6", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        gridViewApdater = new GridViewApdater(getContext(), R.layout.thumbnail, productArrayList);
        gridView.setAdapter(gridViewApdater);
    }

}