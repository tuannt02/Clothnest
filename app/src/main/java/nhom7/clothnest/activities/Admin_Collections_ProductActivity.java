package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.Product_AdminAdapter;
import nhom7.clothnest.models.Product_Admin;
import nhom7.clothnest.notifications.NetworkChangeReceiver;

public class Admin_Collections_ProductActivity extends AppCompatActivity {
    TextView admin_collectionProduct_tvTitle, admin_collectionProduct_tvClose ,admin_collectionProduct_tvAdd;
    String name;
    ArrayList<Product_Admin> productList;
    Product_AdminAdapter adminAdapter;
    ListView lvProduct;
    View includeView;
    TextView tvNumOfProduct,tvNumOfStock;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_collections_product);
        reference();

        getData();
        onClickListenerClose();
        onClickListenerAdd();

        getProducts();


        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void onClickListenerAdd() {
        admin_collectionProduct_tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Admin_DisplayProductActivity.class);
                intent.putExtra("name",getIntent().getStringExtra("name"));
                intent.putExtra("iscollection","abc");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
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
        tvNumOfProduct = includeView.findViewById(R.id.admin_productList_numOfProduct);
        tvNumOfStock= includeView.findViewById(R.id.admin_productList_numOfStocks);
        admin_collectionProduct_tvAdd= findViewById(R.id.admin_collectionProduct_tvAdd);
    }

    private void getData() {
        name = getIntent().getStringExtra("name");
        admin_collectionProduct_tvTitle.setText(name);
    }

    private void getProducts() {
        productList = new ArrayList<>();
        adminAdapter = new Product_AdminAdapter(getApplicationContext(), productList);
        lvProduct.setAdapter(adminAdapter);

        if (name.compareTo("UT") == 0)
            Product_AdminAdapter.getProductUTFromCollection(productList, adminAdapter,tvNumOfProduct,tvNumOfStock);
        if (name.compareTo("LINE") == 0)
            Product_AdminAdapter.getProductLINEFromCollection(productList, adminAdapter,tvNumOfProduct,tvNumOfStock);
        if (name.compareTo("UNISEX") == 0)
            Product_AdminAdapter.getProductUNISEXFromCollection(productList, adminAdapter,tvNumOfProduct,tvNumOfStock);
        if (name.compareTo("WINTER") == 0)
            Product_AdminAdapter.getProducWINTERtFromCollection(productList, adminAdapter,tvNumOfProduct,tvNumOfStock);
        if (name.compareTo("UV PROTECTION") == 0)
            Product_AdminAdapter.getProducUVPROTECTIONtFromCollection(productList, adminAdapter,tvNumOfProduct,tvNumOfStock);
    }

}