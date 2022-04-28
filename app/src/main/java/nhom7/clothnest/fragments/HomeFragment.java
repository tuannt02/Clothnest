package nhom7.clothnest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.models.ProductSlider;


public class HomeFragment extends Fragment {
    GridView gridView;
    ArrayList<Product> productArrayList;
    GridViewApdater gridViewApdater;
    ProductSlider productSlider;
    LinearLayout containersilder;
    GridViewApdater gridViewApdater1;
    GridView gridView1;
    View includeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        gridView = v.findViewById(R.id.gridview_GroupThumbnail);
        includeView = v.findViewById(R.id.group_sales);
        gridView1 = includeView.findViewById(R.id.gridview_GroupThumbnail);
        containersilder=v.findViewById(R.id.container_slider);
        GetProduct();
        productSlider = new ProductSlider(getContext(),containersilder,productArrayList);
        productSlider.createProductSlider();
        return v;
    }

    public void GetProduct(){
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        gridViewApdater = new GridViewApdater(getContext(), R.layout.thumbnail, productArrayList);
        gridView.setAdapter(gridViewApdater);
        gridViewApdater1 = new GridViewApdater(getContext(), R.layout.thumbnail, (ArrayList<Product>) productArrayList.clone());
        gridView1.setAdapter(gridViewApdater1);
    }
}