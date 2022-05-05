package nhom7.clothnest.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import nhom7.clothnest.adapters.CartAdapter;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.models.CartItem;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.ProductSlider;
import nhom7.clothnest.models.PurchaseItem;

public class CartActivity extends AppCompatActivity {

    public ListView lv;
    ImageView btnBack;
    LinearLayout emptyView;
//    GridView gridView;
//    GridViewApdater gridViewApdater;
    CartAdapter cartAdapter;
    ArrayList<Product1> productArrayList;
    ArrayList<CartItem> cartItemArrayList;
    ScrollView scrollView;
    //you might like
    LinearLayout containersilder;
    ProductSlider productSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initUi();
        setupClickOnListener();
        setupScrollViewListener();
        getProductPropose();

        cartItemArrayList = getProductToDisplayListview();

        cartAdapter = new CartAdapter(CartActivity.this, R.layout.cart_item, cartItemArrayList, new CartAdapter.ICLickListenerOnItemListview() {
            @Override
            public void removeItem(int position) {
                cartItemArrayList.remove(position);
                cartAdapter.notifyDataSetChanged();
                updateViewEmpty();
            }
        });
        lv.setAdapter(cartAdapter);

        getLikeProducts();
    }

    private void getLikeProducts() {
        productSlider = new ProductSlider(this, containersilder, productArrayList);
        productSlider.createProductSlider();
    }

    private void initUi()   {
        lv = (ListView) findViewById(R.id.listview_cart);
        btnBack = findViewById(R.id.btnBackCart);
//        gridView = findViewById(R.id.gridview_GroupThumbnail);
        emptyView = findViewById(R.id.cart_view_empty);
        scrollView = findViewById(R.id.cart_main_scrview);
        containersilder = findViewById(R.id.cart_container_slider);
    }

    private void setupClickOnListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupScrollViewListener()  {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if(scrollY > 270) {
                    if(emptyView.getVisibility() == View.VISIBLE)
                        emptyView.setVisibility(View.GONE);
                }
                else    {
                    updateViewEmpty();
                }
            }
        });
    }

    private ArrayList<CartItem> getProductToDisplayListview()   {
        ArrayList<CartItem> cartItemArrayList = new ArrayList<>();
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
        for(int i=0; i<imageId.length; i++) {
            CartItem cartItem = new CartItem(imageId[i], 1, name[i], "", "", price[i]);
            cartItemArrayList.add(cartItem);
        }

        return cartItemArrayList;

    }

//    private void getProductPropose()    {
//        productArrayList = new ArrayList<>();
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "$307", "-24%"));
//        gridViewApdater = new GridViewApdater(CartActivity.this, R.layout.thumbnail, productArrayList);
//        gridView.setAdapter(gridViewApdater);
//    }
private void getProductPropose()    {
    productArrayList = new ArrayList<>();
    productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
    productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
    productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
    productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
    productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
    productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
    productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
    productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
    productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));

}

    private void updateViewEmpty()  {
        if(cartItemArrayList.size() == 0)    {
            emptyView.setVisibility(View.VISIBLE);
        }
        else    {
            emptyView.setVisibility(View.GONE);
        }
    }
}