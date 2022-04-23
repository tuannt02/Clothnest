package nhom7.clothnest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import nhom7.clothnest.adapters.CustomWishlistAdapter;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;


public class WishlistFragment extends Fragment {

    ListView listViewWishlist;

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


        ArrayList<Product> productArrayList = new ArrayList<>();

        for(int i=0; i<imageId.length; i++) {
            Product prod = new Product(name[i], imageId[i], price[i], downPrice[i]);
            productArrayList.add(prod);
        }


        CustomWishlistAdapter customWishlistAdapter = new CustomWishlistAdapter(getContext(), R.layout.wishlist_item, productArrayList);
        listViewWishlist.setAdapter(customWishlistAdapter);


        return root;
    }
}