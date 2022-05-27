package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.Admin_ProductDetailActivity;
import nhom7.clothnest.adapters.Product_AdminAdapter;
import nhom7.clothnest.models.Product_Admin;

public class Admin_ProductsFragment extends Fragment {
    View mView;
    ArrayList<Product_Admin> productList;
    Product_AdminAdapter adminAdapter;
    ListView listView;
    TextView tvProducts, tvStock;
    ImageView ivAdd, ivSort;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_admin_products, container, false);

        reference();

        getProducts();

        getEvents();

        return mView;
    }

    private void getEvents() {
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_AdminProductDetail = new Intent(getContext(), Admin_ProductDetailActivity.class);
                intent_AdminProductDetail.putExtra("handle_adminProductDetail", "add");
                startActivity(intent_AdminProductDetail);
            }
        });
    }

    private void getProducts() {
        productList = new ArrayList<>();
        adminAdapter = new Product_AdminAdapter(getContext(), productList);

        listView.setAdapter(adminAdapter);

//        Product_AdminAdapter.getNumberTotal(tvProducts, tvStock);
        Product_AdminAdapter.getProductAndPushToGridView(productList, adminAdapter, tvProducts, tvStock);
    }

    private void reference() {
        listView = mView.findViewById(R.id.admin_productList_productList);
        tvProducts = mView.findViewById(R.id.admin_productList_numOfProduct);
        tvStock = mView.findViewById(R.id.admin_productList_numOfStocks);
        ivAdd = mView.findViewById(R.id.admin_products_ivAdd);
        ivSort = mView.findViewById(R.id.admin_products_ivSort);
    }

    @Override
    public void onResume() {
        super.onResume();
        getProducts();
    }
}