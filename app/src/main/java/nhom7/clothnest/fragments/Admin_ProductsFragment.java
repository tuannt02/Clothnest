package nhom7.clothnest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.Product_AdminAdapter;
import nhom7.clothnest.models.Product_Admin;

public class Admin_ProductsFragment extends Fragment {
    View mView;
    ArrayList<Product_Admin> productList;
    Product_AdminAdapter adminAdapter;
    ListView listView;
    TextView tvProducts, tvStock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_admin_products, container, false);

        reference();

        getProducts();

        return mView;
    }

    private void getProducts() {
        productList = new ArrayList<>();
        adminAdapter = new Product_AdminAdapter(getContext(), productList);

        listView.setAdapter(adminAdapter);

        Product_AdminAdapter.getNumberTotal(tvProducts, tvStock);
        Product_AdminAdapter.getProductAndPushToGridView(productList, adminAdapter);
    }

    private void reference() {
        listView = mView.findViewById(R.id.admin_productList_productList);
        tvProducts = mView.findViewById(R.id.admin_productList_numOfProduct);
        tvStock = mView.findViewById(R.id.admin_productList_numOfStocks);
    }
}