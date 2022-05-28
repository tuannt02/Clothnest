package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CategoryAdapter;
import nhom7.clothnest.models.CategoryItem;

public class Admin_ModifyActivity extends AppCompatActivity {

    View mView;

    ListView lvModify;
    ArrayList<CategoryItem> modifyList;
    CategoryAdapter adapter;
    ImageView btnclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modify);

        //init Ui
        initUi();

        getModifyItems();

        // set On click
        setOnClickListener();
    }

    private void initUi() {

        lvModify = findViewById(R.id.more_ModifyList);
        btnclose = findViewById(R.id.modify_closeButton);
    }

    private void setOnClickListener() {
        lvModify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) { // Category

                }

                if (i == 1) { // New Arrivals
                    Intent intent = new Intent(Admin_ModifyActivity.this, Admin_Modify_ProductActivity.class);
                    intent.putExtra("adminModifyProduct_key", "new_arrivals");
                    startActivity(intent);
                }

                if (i == 2) { // Sale
                    Intent intent = new Intent(Admin_ModifyActivity.this, Admin_Modify_ProductActivity.class);
                    intent.putExtra("adminModifyProduct_key", "sales");
                    startActivity(intent);
                }

                if (i == 3) { // Collections
                    Intent intent = new Intent(Admin_ModifyActivity.this, Admin_CollectionActivity.class);
                    intent.putExtra("adminModifyProduct_key", "Collections");
                    startActivity(intent);

                }

                if (i == 4) { // Voucher
                    Intent intent = new Intent(Admin_ModifyActivity.this, Admin_VoucherActivity.class);
                    startActivity(intent);
                }

                if (i == 5) { // Banners
                    Intent intent = new Intent(Admin_ModifyActivity.this, Admin_BannersActivity.class);
                    startActivity(intent);
                }

            }
        });

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getModifyItems() {
        modifyList = new ArrayList<>();
        modifyList.add(new CategoryItem("Category", R.drawable.ic_right_arrow));
        modifyList.add(new CategoryItem("New Arrivals", R.drawable.ic_right_arrow));
        modifyList.add(new CategoryItem("Sale", R.drawable.ic_right_arrow));
        modifyList.add(new CategoryItem("Collections", R.drawable.ic_right_arrow));
        modifyList.add(new CategoryItem("Voucher", R.drawable.ic_right_arrow));
        modifyList.add(new CategoryItem("Banners", R.drawable.ic_right_arrow));
        adapter = new CategoryAdapter(Admin_ModifyActivity.this, R.layout.category_item, modifyList);
        lvModify.setAdapter(adapter);
    }


}