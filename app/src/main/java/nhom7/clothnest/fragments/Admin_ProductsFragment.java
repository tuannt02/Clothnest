package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.Admin_ProductDetailActivity;
import nhom7.clothnest.adapters.Product_AdminAdapter;
import nhom7.clothnest.models.Product_Admin;
import nhom7.clothnest.models.User;

public class Admin_ProductsFragment extends Fragment {
    View mView;
    ArrayList<Product_Admin> productList;
    ArrayList<Product_Admin> listOriginal;

    Product_AdminAdapter adminAdapter;
    ListView listView;
    TextView tvProducts, tvStock;
    ImageView ivAdd, ivSort;
    EditText inputSearch;
    TextView clearSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_admin_products, container, false);

        reference();

        setOnTextChange();

        getProducts();

        getEvents();

        setOnClickClearSearch();

        return mView;
    }

    private void setOnClickClearSearch() {
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputSearch.setText("");
            }
        });
    }


    private void setOnTextChange() {
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String inputSearch = charSequence.toString().toLowerCase();
                if (inputSearch == null || inputSearch.length() == 0) {
                    productList.clear();
                    productList.addAll(listOriginal);
                    adminAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<Product_Admin> listFilter = getFilterByname(inputSearch);
                    productList.clear();
                    productList.addAll(listFilter);
                    adminAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private ArrayList<Product_Admin> getFilterByname(String inputSearch) {
        ArrayList<Product_Admin> listAfterFiltered = new ArrayList<>();
        for (int k = 0; k < listOriginal.size(); k++) {
            Product_Admin productAdmin = listOriginal.get(k);
            if (productAdmin.getName().toLowerCase().contains(inputSearch)) {
                listAfterFiltered.add(productAdmin);
            }
        }
        return listAfterFiltered;
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
        listOriginal = new ArrayList<>();
        productList = new ArrayList<>();
        adminAdapter = new Product_AdminAdapter(getContext(), productList);
        listView.setAdapter(adminAdapter);
        Product_AdminAdapter.getProductAndPushToGridView(productList, listOriginal, adminAdapter, tvProducts, tvStock);
    }

    private void reference() {
        listView = mView.findViewById(R.id.admin_productList_productList);
        tvProducts = mView.findViewById(R.id.admin_productList_numOfProduct);
        tvStock = mView.findViewById(R.id.admin_productList_numOfStocks);
        ivAdd = mView.findViewById(R.id.admin_products_ivAdd);
        ivSort = mView.findViewById(R.id.admin_products_ivSort);
        inputSearch = mView.findViewById(R.id.admin_input_search);
        clearSearch = mView.findViewById(R.id.admin_btn_clear);
    }

    @Override
    public void onResume() {
        super.onResume();
        getProducts();
    }
}