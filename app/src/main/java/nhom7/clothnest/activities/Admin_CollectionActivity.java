package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CategoryAdapter;
import nhom7.clothnest.models.CategoryItem;
import nhom7.clothnest.models.Product_Admin;
import nhom7.clothnest.notifications.NetworkChangeReceiver;

public class Admin_CollectionActivity extends AppCompatActivity {
    ListView lvCategoryCollecttion;
    ArrayList<CategoryItem> categoryItems;
    CategoryAdapter categoryAdapter;
    TextView collections_close;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_collection);

        references();
        loadCategoryCollection();

        setOnClicklistener();
        setOnClicklistenerClose();

        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void setOnClicklistenerClose() {
        collections_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setOnClicklistener() {
        lvCategoryCollecttion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    Intent intent = new Intent(Admin_CollectionActivity.this,Admin_Collections_ProductActivity.class);
                    intent.putExtra("name","LINE");
                    startActivity(intent);
                }

                if(i==1)
                {
                    Intent intent = new Intent(Admin_CollectionActivity.this,Admin_Collections_ProductActivity.class);
                    intent.putExtra("name","UNISEX");
                    startActivity(intent);
                }
                if(i==2)
                {
                    Intent intent = new Intent(Admin_CollectionActivity.this,Admin_Collections_ProductActivity.class);
                    intent.putExtra("name","UT");
                    startActivity(intent);
                }
                if(i==3)
                {
                    Intent intent = new Intent(Admin_CollectionActivity.this,Admin_Collections_ProductActivity.class);
                    intent.putExtra("name","UV PROTECTION");
                    startActivity(intent);
                }
                if(i==4)
                {
                    Intent intent = new Intent(Admin_CollectionActivity.this,Admin_Collections_ProductActivity.class);
                    intent.putExtra("name","WINTER");
                    startActivity(intent);
                }

            }
        });
    }

    private void loadCategoryCollection() {
        categoryItems = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(Admin_CollectionActivity.this,  R.layout.category_item, categoryItems);
        lvCategoryCollecttion.setAdapter(categoryAdapter);
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("collections")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                categoryItems.add(new CategoryItem(documentSnapshot.getId(), R.drawable.ic_right_arrow));
                                categoryAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }



    private void references() {
        lvCategoryCollecttion = findViewById(R.id.more_CollectionsList);
        collections_close= findViewById(R.id.collections_close);
    }

}