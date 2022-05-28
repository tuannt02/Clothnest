package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.Product_AdminAdapter;
import nhom7.clothnest.models.Product_Admin;

public class Admin_Modify_ProductActivity extends AppCompatActivity {
    ArrayList<Product_Admin> productList;
    Product_AdminAdapter adminAdapter;

    TextView tvTitle, tvAdd, tvClose;
    View includeView;
    TextView tvNumOfProduct, tvNumOfStock;
    ListView lvProduct;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modify_product);

        reference();
        handleData();
        getEvent();
    }

    private void getEvent() {
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void handleData() {
        key = getIntent().getStringExtra("adminModifyProduct_key");
        getProducts(key);
    }

    private void reference() {
        includeView = findViewById(R.id.admin_modifyProduct_Include);
        tvTitle = findViewById(R.id.admin_modifyProduct_tvTitle);
        tvAdd = findViewById(R.id.admin_modifyProduct_tvAdd);
        tvClose = findViewById(R.id.admin_modifyProduct_tvClose);
        tvNumOfProduct = includeView.findViewById(R.id.admin_productList_numOfProduct);
        tvNumOfStock = includeView.findViewById(R.id.admin_productList_numOfStocks);
        lvProduct = includeView.findViewById(R.id.admin_productList_productList);
    }

    private void getProducts(String collectionName) {
        productList = new ArrayList<>();
        adminAdapter = new Product_AdminAdapter(getApplicationContext(), productList);

        lvProduct.setAdapter(adminAdapter);

        //Product_AdminAdapter.getNumberTotal(tvNumOfProduct, tvNumOfStock);
        Product_AdminAdapter.getModifyProducts(productList, adminAdapter, collectionName);
    }
}