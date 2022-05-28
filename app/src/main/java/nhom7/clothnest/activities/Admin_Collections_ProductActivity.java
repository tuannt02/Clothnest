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

public class Admin_Collections_ProductActivity extends AppCompatActivity {
    TextView admin_collectionProduct_tvTitle, admin_collectionProduct_tvClose;
    String name;
    ArrayList<Product_Admin> productList;
    Product_AdminAdapter adminAdapter;
    ListView lvProduct;
    View includeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_collections_product);
        reference();

        getData();
        onClickListenerClose();

        getProducts();
    }

    private void onClickListenerClose() {
        admin_collectionProduct_tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void reference() {
        admin_collectionProduct_tvTitle = findViewById(R.id.admin_collectionProduct_tvTitle);
        admin_collectionProduct_tvClose = findViewById(R.id.admin_collectionProduct_tvClose);
        includeView = findViewById(R.id.admin_modifyProduct_Include);
        lvProduct = includeView.findViewById(R.id.admin_productList_productList);
    }

    private void getData() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        admin_collectionProduct_tvTitle.setText(name);
    }

    private void getProducts() {
        productList = new ArrayList<>();
        adminAdapter = new Product_AdminAdapter(getApplicationContext(), productList);
        lvProduct.setAdapter(adminAdapter);

        if (name.compareTo("UT") == 0)
            Product_AdminAdapter.getProductUTFromCollection(productList, adminAdapter);
        if (name.compareTo("LINE") == 0)
            Product_AdminAdapter.getProductLINEFromCollection(productList, adminAdapter);
        if (name.compareTo("UNISEX") == 0)
            Product_AdminAdapter.getProductUNISEXFromCollection(productList, adminAdapter);
        if (name.compareTo("WINTER") == 0)
            Product_AdminAdapter.getProducWINTERtFromCollection(productList, adminAdapter);
        if (name.compareTo("UV PROTECTION") == 0)
            Product_AdminAdapter.getProducUVPROTECTIONtFromCollection(productList, adminAdapter);
    }

}