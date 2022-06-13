package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.Product_AdminAdapter;
import nhom7.clothnest.models.Product_Admin;

public class Admin_Category_ProductActivity extends AppCompatActivity {

    private ArrayList<Product_Admin> productList;
    private ArrayList<Product_Admin> listOriginal;
    private EditText inputSearch;
    private TextView clearSearch;

    private Product_AdminAdapter adminAdapter;

    private String key;
    private TextView admin_categoryProduct_tvTitle,admin_categoryProduct_tvClose;
    private ListView lvCategoryProduct;
    private  View includeView;
    private TextView tvNumOfProduct, tvNumOfStock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_product);

        reference();

        getData();

        setOnClickListennerClose();

        setOnClickClearSearch();
        setOnTextChange();
    }

    private void setOnClickListennerClose() {
        admin_categoryProduct_tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void reference() {
        admin_categoryProduct_tvTitle = findViewById(R.id.admin_categoryProduct_tvTitle);
        admin_categoryProduct_tvClose= findViewById(R.id.admin_categoryProduct_tvClose);
        includeView = findViewById(R.id.admin_categoryProduct_Include);
        lvCategoryProduct =includeView.findViewById(R.id.admin_productList_productList);
        tvNumOfProduct = includeView.findViewById(R.id.admin_productList_numOfProduct);
        tvNumOfStock = includeView.findViewById(R.id.admin_productList_numOfStocks);
        inputSearch = findViewById(R.id.admin_input_search);
        clearSearch = findViewById(R.id.admin_btn_clear);
    }

    private void getData() {
        key = getIntent().getStringExtra("nameCategory");
        admin_categoryProduct_tvTitle.setText(key);
        getProduct(key);
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


    private void getProduct(String collection) {
        productList= new ArrayList<>();
        listOriginal = new ArrayList<>();
        adminAdapter= new Product_AdminAdapter(getApplicationContext(),productList);
        lvCategoryProduct.setAdapter(adminAdapter);

        Product_AdminAdapter.getProductFromCategory(productList,adminAdapter,collection,tvNumOfProduct,tvNumOfStock,listOriginal);

    }
}