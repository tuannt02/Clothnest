package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.CartAdapter;
import nhom7.clothnest.models.CartItem;
import nhom7.clothnest.models.Wishlist;

public class CheckoutActivity extends AppCompatActivity {

    ListView lv;
    ImageView btnBack;
    ArrayList<CartItem> cartItemArrayList;
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //Init UI
        initUi();
        setupClickOnListener();

        cartItemArrayList = getProductToDisplayListview();
        cartAdapter = new CartAdapter(CheckoutActivity.this, R.layout.cart_item, cartItemArrayList, new CartAdapter.ICLickListenerOnItemListview() {
            @Override
            public void addItemToWishlist(String keyProduct) {
            }
            @Override
            public void removeItem(int position, String docID) {
            }
            @Override
            public void updateTotalPrice()  {

            }
        });
        lv.setAdapter(cartAdapter);
    }

    private void initUi()   {
        lv = findViewById(R.id.listview_checkout);
        btnBack = findViewById(R.id.btnBackCheckout);
        cartItemArrayList = new ArrayList<>();
    }

    private void setupClickOnListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private ArrayList<CartItem> getProductToDisplayListview()  {
        ArrayList<CartItem> cartItemArrayList = new ArrayList<>();

        String[] name = {"Áo Polo Thương Hiệu Chuẩn Thời Trang Việt",
                "Áo Polo Thương Hiệu Chuẩn Thời Trang Việt",
                "Áo Polo Thương Hiệu Chuẩn Thời Trang Việt",
                "Áo Polo Thương Hiệu Chuẩn Thời Trang Việt",
                "Áo Polo Thương Hiệu Chuẩn Thời Trang Việt",
                "Áo Polo Thương Hiệu Chuẩn Thời Trang Việt"};
        double[] price = {500, 600, 600, 600, 600, 600};
        for(int i=0; i<name.length; i++) {
            CartItem cartItem = new CartItem();
            cartItem.setImg("https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/sales%2Fusgoods_09_446223.webp?alt=media&token=3f27ce5b-c68b-472e-b1c6-6dc755e54619");
            cartItem.setQty(1);
            cartItem.setName("Áo Polo Thương Hiệu Chuẩn  Thời Trang Việt");
            cartItem.setPrice(499.0);
            cartItem.setDiscount(5);

            cartItemArrayList.add(cartItem);
        }

        return cartItemArrayList;
    }
}