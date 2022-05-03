package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import nhom7.clothnest.activities.CartActivity;
import nhom7.clothnest.adapters.CustomWishlistAdapter;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;


public class WishlistFragment extends Fragment {

    ListView listViewWishlist;
    ImageButton btnNavCart, orderOptionMenu;
    ArrayList<Product> productArrayList;
    TextView txtQuantity;
    CustomWishlistAdapter customWishlistAdapter;
    LinearLayout emptyView;

    public WishlistFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_wishlist, container, false);

        listViewWishlist = (ListView)root.findViewById(R.id.listview_wishlist);


        productArrayList = getListProduct();

        customWishlistAdapter = new CustomWishlistAdapter(getContext(), R.layout.wishlist_item, productArrayList, new CustomWishlistAdapter.ICLickListenerOnItemListview() {
            @Override
            public void removeItem(int position) {
                productArrayList.remove(position);
                customWishlistAdapter.notifyDataSetChanged();
                updateQuantityItemInListView(root);
                updateViewEmpty(root);
            }
        });

        updateViewEmpty(root);
        listViewWishlist.setAdapter(customWishlistAdapter);

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupClickListener(view);
        setupAutoEventListener(view);
    }

    private void setupClickListener(View view) {
        // Set up click event cho cac danh muc
        setupBtnNavCart(view);
        setupOrderOptionMenu(view);
    }

    private void setupAutoEventListener(View view)   {
        updateQuantityItemInListView(view);
    }

    private void setupBtnNavCart(View view)  {
        btnNavCart = view.findViewById(R.id.imagebutton_naviCart);
        btnNavCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupOrderOptionMenu(View view)    {
        orderOptionMenu = view.findViewById(R.id.imagebutton_order_option_menu);
        orderOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = getListOptionMenu();
                CustomOptionMenu orderOptionMenu = new CustomOptionMenu(getContext(), new CustomOptionMenu.IClickListenerOnItemListview() {
                    @Override
                    public void onClickItem(int pos) {
                        // Order by (0.Date add, 1.Price (Low - High) 2.Price (High - Low))
                        if(pos == 0)    { // Date add

                        }

                        if(pos == 1)    { // Price Low -> High

                        }

                        if(pos == 2)    { // Price High -> Low

                        }

                    }
                },list, "ORDER BY", null);
                orderOptionMenu.show();

            }
        });
    }

    private void updateQuantityItemInListView(View view) {
        // Get quantity product from Listview
        int quantity = productArrayList.size();

        txtQuantity = view.findViewById(R.id.wishlist_quantity_item);
        txtQuantity.setText(quantity + "  ITEMS");

    }

    private void updateViewEmpty(View view) {
        emptyView = view.findViewById(R.id.wishlist_view_empty);
        if(productArrayList.size() == 0)    {
            emptyView.setVisibility(View.VISIBLE);
        }
        else    {
            emptyView.setVisibility(View.GONE);
        }
    }

    ArrayList<String> getListOptionMenu()   {
        ArrayList<String> list = new ArrayList<>();
        list.add("Date add");
        list.add("Price (Low - High)");
        list.add("Price (High - Low)");

        return list;
    }

    ArrayList<Product> getListProduct() {
        //Call API here
        ArrayList<Product> productArrayList = new ArrayList<>();

        int[] imageId = {
                R.drawable.sample,
                R.drawable.sample,
                R.drawable.sample,
                R.drawable.sample,
                R.drawable.sample,
                R.drawable.sample};

        String[] name = {
                "Áo Polo Vải Pique Co Giãn Ngắn Tay",
                "Áo Polo Vải Pique Co Giãn Ngắn Tay",
                "Áo Polo Vải Pique Co Giãn Ngắn Tay",
                "Áo Polo Vải Pique Co Giãn Ngắn Tay",
                "Áo Polo Vải Pique Co Giãn Ngắn Tay",
                "Áo Polo Vải Pique Co Giãn Ngắn Tay"};
        String[] price = {"$500", "$600", "$600", "$600", "$600", "$600"};
        String[] downPrice = {"$300", "$400", "$400", "$400", "$400", "$400"};

        for(int i=0; i<imageId.length; i++) {
            Product prod = new Product(name[i], imageId[i], price[i], downPrice[i]);
            productArrayList.add(prod);
        }


        return productArrayList;
    }
}