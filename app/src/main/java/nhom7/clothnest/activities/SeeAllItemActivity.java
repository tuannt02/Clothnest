package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.adapters.Product_ThumbnailAdapter;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;

public class SeeAllItemActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<Product_Thumbnail> productArrayList;
    Product_ThumbnailAdapter adapter;
    Animation scale;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_item);
        reference();
        intentData();
        GetProduct();
        AnimationScale();

    }


    private void reference() {
        gridView = findViewById(R.id.gridviewSeeAllItem);
    }

    private void AnimationScale() {
        scale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_list_anim);
        gridView.startAnimation(scale);
    }

    private void intentData() {
        Intent intent = getIntent();
        TextView textView = findViewById(R.id.textview2);
        name = intent.getStringExtra("name");
        textView.setText("" + name);
    }

    private void GetProduct() {
        productArrayList = new ArrayList<>();
        adapter = new Product_ThumbnailAdapter(getApplicationContext(), productArrayList);
        gridView.setAdapter(adapter);

        if (name.compareTo("NEW ARRIVALS") == 0)
            Product_ThumbnailAdapter.getProductArrivalAndPushToGridView(productArrayList, adapter);
        else if (name.compareTo("SALES") == 0)
            Product_ThumbnailAdapter.getProductSalesAndPushToGridView(productArrayList, adapter);
        else if (name.compareTo("WINTER") == 0)
            Product_ThumbnailAdapter.getProductFromCollection(productArrayList, adapter);
        else if (name.compareTo("LINE") == 0)
            Product_ThumbnailAdapter.getProductLineFromCollection(productArrayList, adapter);
        else if (name.compareTo("UT") == 0)
            Product_ThumbnailAdapter.getProductUTFromCollection(productArrayList, adapter);
        else if (name.compareTo("UNISEX") == 0)
            Product_ThumbnailAdapter.getProductUnisexFromCollection(productArrayList, adapter);
        else if (name.compareTo("UV PROTECTION") == 0)
            Product_ThumbnailAdapter.getProductUvProtectionFromCollection(productArrayList, adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductThumbnail();
    }

    public void getProductThumbnail() {
        //Thêm sản phẩm vào sales
        productArrayList = new ArrayList<>();
        adapter = new Product_ThumbnailAdapter(getApplicationContext(), productArrayList);
        gridView.setAdapter(adapter);
        if (name.compareTo("NEW ARRIVALS") == 0)
            Product_ThumbnailAdapter.getProductArrivalAndPushToGridView(productArrayList, adapter);
        else if (name.compareTo("SALES") == 0)
            Product_ThumbnailAdapter.getProductSalesAndPushToGridView(productArrayList, adapter);
        else if (name.compareTo("WINTER") == 0)
            Product_ThumbnailAdapter.getProductFromCollection(productArrayList, adapter);
        else if (name.compareTo("LINE") == 0)
            Product_ThumbnailAdapter.getProductLineFromCollection(productArrayList, adapter);
        else if (name.compareTo("UT") == 0)
            Product_ThumbnailAdapter.getProductUTFromCollection(productArrayList, adapter);
        else if (name.compareTo("UNISEX") == 0)
            Product_ThumbnailAdapter.getProductUnisexFromCollection(productArrayList, adapter);
        else if (name.compareTo("UV PROTECTION") == 0)
            Product_ThumbnailAdapter.getProductUvProtectionFromCollection(productArrayList, adapter);
    }
}