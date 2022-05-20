package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.models.CategoryItem;

public class Admin_ProductDetailActivity extends AppCompatActivity {
    AutoCompleteTextView autoCpTv_category;
    ArrayList<String> categoryList;
    ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);

        reference();
        getCategory();
    }

    private void getCategory() {
        categoryList = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<String>(this, R.layout.item_admin_productdetail_category, categoryList);
        autoCpTv_category.setAdapter(categoryAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(CategoryItem.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                String category = document.getString("name");
                                categoryList.add(category);
                                categoryAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void reference() {
        autoCpTv_category = findViewById(R.id.admin_productDetail_ddCategory);

    }
}