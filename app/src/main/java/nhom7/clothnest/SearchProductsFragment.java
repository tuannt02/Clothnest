package nhom7.clothnest;

import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class SearchProductsFragment extends Fragment {
    GridView gridView;
    ImageView btnFilter;
    ArrayList<Product> productArrayList;
    GridViewApdater gridViewApdater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_search_products, container, false);

        gridView = mView.findViewById(R.id.gridview);
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
    //    Thêm sản phẩm vào GridView
    public void GetProduct(){
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        gridViewApdater = new GridViewApdater(getContext(), R.layout.thumbnail, productArrayList);
        gridView.setAdapter(gridViewApdater);
    }
}