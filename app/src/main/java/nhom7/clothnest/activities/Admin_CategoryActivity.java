package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class Admin_CategoryActivity extends AppCompatActivity {

    TextView admin_Category_tvClose;
    ArrayList<CategoryItem> arrayList;
    ListView lv_category;
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        reference();
        setOnClickListenerClose();
        getCategoryFromFireStore();
        setOnClickListenerItem();
    }

    private void setOnClickListenerItem() {
        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void getCategoryFromFireStore() {
        arrayList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getApplicationContext(),arrayList);
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
    }
}