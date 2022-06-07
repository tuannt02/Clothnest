package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class Admin_Collections_ProductActivity extends AppCompatActivity {
    TextView admin_collectionProduct_tvTitle, admin_collectionProduct_tvClose, admin_collectionProduct_tvAdd;
    String name;
    ArrayList<Product_Admin> productList;
    Product_AdminAdapter adminAdapter;
    ListView lvProduct;
    View includeView;
    TextView tvNumOfProduct, tvNumOfStock;
    BroadcastReceiver broadcastReceiver;

    EditText inputSearch;
    TextView clearSearch;
    ArrayList<Product_Admin> listOriginal;
    public static CustomProgressBar dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_collections_product);
        dialog = new CustomProgressBar(Admin_Collections_ProductActivity.this);
        reference();

        getData();
        onClickListenerClose();
        onClickListenerAdd();

        setOnClickClearSearch();
        setOnTextChange();


        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void onClickListenerAdd() {
        admin_collectionProduct_tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Admin_DisplayProductActivity.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("iscollection", "abc");
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


    private void reference() {
        admin_collectionProduct_tvTitle = findViewById(R.id.admin_collectionProduct_tvTitle);
        admin_collectionProduct_tvClose = findViewById(R.id.admin_collectionProduct_tvClose);
        includeView = findViewById(R.id.admin_modifyProduct_Include);
        lvProduct = includeView.findViewById(R.id.admin_productList_productList);
        tvNumOfProduct = includeView.findViewById(R.id.admin_productList_numOfProduct);
        tvNumOfStock = includeView.findViewById(R.id.admin_productList_numOfStocks);
        admin_collectionProduct_tvAdd = findViewById(R.id.admin_collectionProduct_tvAdd);
        inputSearch = findViewById(R.id.admin_input_search);
        clearSearch = findViewById(R.id.admin_btn_clear);
    }

    private void getData() {
        dialog.show();
        name = getIntent().getStringExtra("name");
        admin_collectionProduct_tvTitle.setText(name);
        getProducts(name);
    }

    private void getProducts(String collectionName) {
        listOriginal = new ArrayList<>();
        productList = new ArrayList<>();
        adminAdapter = new Product_AdminAdapter(getApplicationContext(), productList);
        lvProduct.setAdapter(adminAdapter);
        Product_AdminAdapter.getProductFromCollection(productList, listOriginal, adminAdapter, tvNumOfProduct, tvNumOfStock, collectionName);
    }

}