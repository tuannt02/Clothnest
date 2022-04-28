package nhom7.clothnest.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import nhom7.clothnest.adapters.CartAdapter;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.models.CartItem;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.models.PurchaseItem;

public class CartActivity extends AppCompatActivity {

    public ListView lv;
    ImageView btnBack;
    GridView gridView;
    GridViewApdater gridViewApdater;
    ArrayList<Product> productArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initUi();
        setupClickOnListener();
        getProductPropose();

        int[] imageId = {R.drawable.sample,
                R.drawable.sample,
                R.drawable.sample,
                R.drawable.sample,
                R.drawable.sample,
                R.drawable.sample,};

        String[] name = {"Áo Polo Thương Hiệu Chuẩn Thời Trang Việt",
                "Áo Polo Thương Hiệu Chuẩn Thời Trang Việt",
                "Áo Polo Thương Hiệu Chuẩn Thời Trang Việt",
                "Áo Polo Thương Hiệu Chuẩn Thời Trang Việt",
                "Áo Polo Thương Hiệu Chuẩn Thời Trang Việt",
                "Áo Polo Thương Hiệu Chuẩn Thời Trang Việt"};
        double[] price = {500, 600, 600, 600, 600, 600};
//        String[] downPrice = {"$300", "$400", "$400", "$400", "$400", "$400",};

        ArrayList<CartItem> cartItemArrayList = new ArrayList<>();

        for(int i=0; i<imageId.length; i++) {
            CartItem cartItem = new CartItem(imageId[i], 1, name[i], "", "", price[i]);
            cartItemArrayList.add(cartItem);
        }


        CartAdapter cartAdapter = new CartAdapter(CartActivity.this, R.layout.cart_item, cartItemArrayList);
        lv.setAdapter(cartAdapter);



    }

    private void initUi()   {
        lv = (ListView) findViewById(R.id.listview_cart);
        btnBack = findViewById(R.id.btnBackCart);
        gridView = findViewById(R.id.gridview_GroupThumbnail);

    }

    private void setupClickOnListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getProductPropose()    {
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
        gridViewApdater = new GridViewApdater(CartActivity.this, R.layout.thumbnail, productArrayList);
        gridView.setAdapter(gridViewApdater);
    }
}