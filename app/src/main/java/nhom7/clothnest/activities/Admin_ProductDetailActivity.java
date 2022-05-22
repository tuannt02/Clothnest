package nhom7.clothnest.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.StockAdapter;
import nhom7.clothnest.adapters.StockImageAdapter;
import nhom7.clothnest.interfaces.ActivityConstants;
import nhom7.clothnest.models.CategoryItem;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.SizeClass;
import nhom7.clothnest.models.Stock;

public class Admin_ProductDetailActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SIZE = 0x2902;
    private static final int REQUEST_CODE_COLOR = 0x2903;
    private static final int REQUEST_CODE_IMAGE = 0x2904;

    String colorID;
    String sizeID;

    ArrayList<String> categoryList;
    ArrayList<Stock> stockList;

    ArrayAdapter<String> categoryAdapter;
    StockAdapter stockAdapter;

    Button btnAdd, btnClear;
    EditText etSize, etColor, etQuantity;
    ListView lvStock;
    GridView gvImages;
    AutoCompleteTextView autoCpTv_category;

    //Select image
    ArrayList<Uri> uriList;
    StockImageAdapter stockImageAdapter;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);

        reference();
        createStock();
        getCategory();
        getEvent();
    }

    private void getEvent() {
        etSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sizeIntent = new Intent(getApplicationContext(), Admin_SizeActivity.class);
                sizeIntent.putExtra("activity_type", ActivityConstants.CHOOSE_SIZE);
                startActivityForResult(sizeIntent, REQUEST_CODE_SIZE);
            }
        });

        etColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sizeIntent = new Intent(getApplicationContext(), Admin_ColorActivity.class);
                sizeIntent.putExtra("activity_type", ActivityConstants.CHOOSE_COLOR);
                startActivityForResult(sizeIntent, REQUEST_CODE_COLOR);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSize.getText().toString().isEmpty() || etColor.getText().toString().isEmpty() || etQuantity.getText().toString().isEmpty() || uriList.size() == 1) {
                    Toast.makeText(Admin_ProductDetailActivity.this, "Lack of information", Toast.LENGTH_SHORT).show();
                } else {
                    int quantity = 0;
                    try {
                        quantity = Integer.parseInt(etQuantity.getText().toString());

                        uriList.remove(0);

                        Stock currentStoct = new Stock(sizeID, colorID, quantity, uriList);

                        currentStoct.setSizeName(etSize.getText().toString());
                        currentStoct.setColorName(etColor.getText().toString());

                        stockList.add(currentStoct);
                        stockAdapter.notifyDataSetChanged();

                        getStockImageList();
                    } catch (NumberFormatException e) {
                        Toast.makeText(Admin_ProductDetailActivity.this, "Quantity is an integer", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSize.setText("");
                etColor.setText("");
                etQuantity.setText("");
            }
        });

        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    selectImage();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIZE) {
            if (resultCode == Activity.RESULT_OK) {
                sizeID = data.getStringExtra("selected_size");
                getSize(sizeID);
            }
        } else if (requestCode == REQUEST_CODE_COLOR) {
            if (resultCode == Activity.RESULT_OK) {
                colorID = data.getStringExtra("selected_color");
                getColor(colorID);
            }
        } else if (requestCode == REQUEST_CODE_IMAGE) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                uriList.add(imageUri);
                stockImageAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getColor(String colorID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(ColorClass.COLLECTION_NAME + '/' + colorID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        etColor.setText(documentSnapshot.getString("name"));
                    }
                });
    }

    private void getSize(String sizeID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(SizeClass.COLLECTION_NAME + '/' + sizeID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        etSize.setText(documentSnapshot.getString("short_name"));
                    }
                });
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
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
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
        btnAdd = findViewById(R.id.admin_productDetail_btnAdd);
        btnClear = findViewById(R.id.admin_productDetail_btnClear);
        etSize = findViewById(R.id.admin_productDetail_etSize);
        etColor = findViewById(R.id.admin_productDetail_etColor);
        etQuantity = findViewById(R.id.admin_productDetail_etQuantity);
        lvStock = findViewById(R.id.admin_productDetail_lvStock);
        gvImages = findViewById(R.id.admin_productDetail_gvImage);
    }

    private void createStock() {
        stockList = new ArrayList<>();
        stockAdapter = new StockAdapter(this, stockList, new StockAdapter.ClickListener() {
            @Override
            public void removeItem(int position) {
                stockList.remove(position);
                stockAdapter.notifyDataSetChanged();
            }
        });
        lvStock.setAdapter(stockAdapter);

        getStockImageList();
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    private void getStockImageList() {
        uriList = new ArrayList<>();
        stockImageAdapter = new StockImageAdapter(Admin_ProductDetailActivity.this, uriList);
        gvImages.setAdapter(stockImageAdapter);

        String uriPath = "android.resource://" + getPackageName() + "/" + R.drawable.add_image;
        Uri uri = Uri.parse(uriPath);
        uriList.add(uri);
        stockImageAdapter.notifyDataSetChanged();
    }
}