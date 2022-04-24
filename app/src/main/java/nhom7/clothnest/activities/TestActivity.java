package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;

public class TestActivity extends AppCompatActivity {
    private ArrayList<Product> products;
    private LinearLayout container;
    EditText input_email, input_name;
    Button btnAddAppendUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // set data
//        setDataForList();

        // review this method to know how we can implement a slider
//        createProductSlider();

//        input_email = findViewById(R.id.test_input_email);
//        input_name = findViewById(R.id.test_input_name);
//        btnAddAppendUser = findViewById(R.id.test_btn_Add);
//
//        String email = input_email.getText().toString().trim();
//        String name = input_name.getText().toString().trim();
//        btnAddAppendUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                User.addAppendUser(email, name, "", "", "");
//            }
//        });

    }

//    private void createProductSlider() {
//        // init UI
//        container = findViewById(R.id.linearLayout_container_testActivity);
//
//        // create product slider
//        ProductSlider productSlider = new ProductSlider(this, container, products);
//        productSlider.createProductSlider();
//    }
//
//    private void setDataForList() {
//        products = new ArrayList<>();
//        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
//        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
//        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
//        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
//        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
//        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
//        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
//        products.add(new Product("Oversized Hoodie Oversized Hoodie Oversized Hoodie", R.drawable.product, "307", "24"));
//    }
}