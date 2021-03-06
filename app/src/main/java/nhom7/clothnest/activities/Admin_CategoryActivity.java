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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CategoryAdapter;
import nhom7.clothnest.models.CategoryItem;
import nhom7.clothnest.models.Product_Admin;
import nhom7.clothnest.notifications.NetworkChangeReceiver;

public class Admin_CategoryActivity extends AppCompatActivity {

    TextView admin_Category_tvClose;
    ArrayList<CategoryItem> arrayList;
    ListView lv_category;
    CategoryAdapter categoryAdapter;
    BroadcastReceiver broadcastReceiver;
    ArrayList<CategoryItem> listOriginal;

    EditText inputSearch;
    TextView clearSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        reference();
        setOnClickListenerClose();


        getCategoryFromFireStore();

        setOnClickListenerItem();

        setOnClickClearSearch();

        setOnTextChange();




        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void setOnClickListenerItem() {
        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    Intent intent = new Intent(Admin_CategoryActivity.this,Admin_Category_ProductActivity.class);
                    intent.putExtra("nameCategory","T-Shirts");
                    startActivity(intent);
                }

                if(i==1)
                {
                    Intent intent = new Intent(Admin_CategoryActivity.this,Admin_Category_ProductActivity.class);
                    intent.putExtra("nameCategory","Accessories");
                    startActivity(intent);
                }
                if(i==2)
                {
                    Intent intent = new Intent(Admin_CategoryActivity.this,Admin_Category_ProductActivity.class);
                    intent.putExtra("nameCategory","Loungewear & Home");
                    startActivity(intent);
                }
                if(i==3)
                {
                    Intent intent = new Intent(Admin_CategoryActivity.this,Admin_Category_ProductActivity.class);
                    intent.putExtra("nameCategory","Pants");
                    startActivity(intent);
                }
                if(i==4)
                {
                    Intent intent = new Intent(Admin_CategoryActivity.this,Admin_Category_ProductActivity.class);
                    intent.putExtra("nameCategory","Sport Utility Wear");
                    startActivity(intent);
                }
                if(i==5)
                {
                    Intent intent = new Intent(Admin_CategoryActivity.this,Admin_Category_ProductActivity.class);
                    intent.putExtra("nameCategory","Innerwear");
                    startActivity(intent);
                }
                if(i==6)
                {
                    Intent intent = new Intent(Admin_CategoryActivity.this,Admin_Category_ProductActivity.class);
                    intent.putExtra("nameCategory","Outerwear");
                    startActivity(intent);
                }


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
                    arrayList.clear();
                    arrayList.addAll(listOriginal);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<CategoryItem> listFilter = getFilterByname(inputSearch);
                    arrayList.clear();
                    arrayList.addAll(listFilter);
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private ArrayList<CategoryItem> getFilterByname(String inputSearch) {
        ArrayList<CategoryItem> listAfterFiltered = new ArrayList<>();
        for (int k = 0; k < listOriginal.size(); k++) {
            CategoryItem categoryItem = listOriginal.get(k);
            if (categoryItem.getCategoryName().toLowerCase().contains(inputSearch)) {
                listAfterFiltered.add(categoryItem);
            }
        }
        return listAfterFiltered;
    }


    private void getCategoryFromFireStore() {
        arrayList = new ArrayList<>();
        listOriginal= new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getApplicationContext(), R.layout.category_item,arrayList);
        lv_category.setAdapter(categoryAdapter);

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                            {
                                CategoryItem categoryItem = new CategoryItem();
                                arrayList.add(categoryItem);
                                listOriginal.add(categoryItem);

                                Map<String, Object> map = documentSnapshot.getData();
                                Iterator iterator = map.keySet().iterator();

                                while (iterator.hasNext()) {
                                    String key = (String) iterator.next();
                                    if (key.equals("name")) {
                                        categoryItem.setCategoryName((String) map.get(key));
                                        categoryAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                });
    }



    private void setOnClickListenerClose() {
        admin_Category_tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void reference() {
        admin_Category_tvClose= findViewById(R.id.admin_Category_tvClose);
        lv_category= findViewById(R.id.lv_category);
        inputSearch = (EditText) findViewById(R.id.admin_category_input_search);
        clearSearch = (TextView) findViewById(R.id.admin_category_btn_clear);
    }
}