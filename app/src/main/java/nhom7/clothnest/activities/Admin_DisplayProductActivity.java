package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.Product_AdminReadOnlyAdapter;
import nhom7.clothnest.models.Product_Admin;
import nhom7.clothnest.models.Stock;

public class Admin_DisplayProductActivity extends AppCompatActivity {

    ArrayList<Product_Admin> arrayList= new ArrayList<>();
    Product_AdminReadOnlyAdapter product_adminAdapter;
    ListView listView;
    View includeview;
    TextView tvCountProduct, tvCountStock;
    String key,iscollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_display_product);
        reference();
        saveIntent();
        GetProduct(tvCountProduct, tvCountStock);

    }

    private void saveIntent() {
        key = getIntent().getStringExtra("adminModifyProduct_key");
        if(key==null)
        key=getIntent().getStringExtra("name");
        iscollection= getIntent().getStringExtra("iscollection");
    }


    private void reference() {
        includeview = findViewById(R.id.admin_display_product);
        listView = includeview.findViewById(R.id.admin_productList_productList);
        tvCountProduct = includeview.findViewById(R.id.admin_productList_numOfProduct);
        tvCountStock = includeview.findViewById(R.id.admin_productList_numOfStocks);
    }


    private void GetProduct(TextView tvCountProduct, TextView tvCountStock) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();

        product_adminAdapter = new Product_AdminReadOnlyAdapter(Admin_DisplayProductActivity.this, arrayList,key,iscollection);
        listView.setAdapter(product_adminAdapter);

        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            tvCountProduct.setText(task.getResult().size() + "");
                            int CountStock = 0;
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Product_Admin productAdmin = new Product_Admin();
                                arrayList.add(productAdmin);

                                productAdmin.setId(documentSnapshot.getId());
                                productAdmin.setName(documentSnapshot.getString("name"));
                                productAdmin.setMainImage(documentSnapshot.getString("main_img"));
                                productAdmin.setPrice(documentSnapshot.getDouble("price"));
                                product_adminAdapter.notifyDataSetChanged();

                                documentSnapshot.getReference().collection("stocks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            int stock = 0;
                                            for (QueryDocumentSnapshot documentSnapshot1 : task.getResult()) {
                                                stock += (int) Math.round(documentSnapshot1.getDouble("quantity"));
                                                productAdmin.setStock(stock);
                                                product_adminAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                                Task<QuerySnapshot> a = db.collection(Product_Admin.COLLECTION_NAME).document(documentSnapshot.getId()).collection(Stock.COLLECTION_NAME)
                                        .get();
                                int stock = 0;
                                while (!a.isComplete()) ;
                                for (DocumentSnapshot documentSnapshot1 : a.getResult()) {
                                    stock += (int) Math.round(documentSnapshot1.getDouble("quantity"));
                                }
                                CountStock += stock;
                                tvCountStock.setText(CountStock + "");
                            }
                        }

                    }
                });
    }

}