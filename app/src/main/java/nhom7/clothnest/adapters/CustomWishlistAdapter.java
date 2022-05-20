package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.ArrayList;

import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.models.Wishlist;
import nhom7.clothnest.util.AddToCart;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;

public class CustomWishlistAdapter extends ArrayAdapter<Wishlist> {
    public interface ICLickListenerOnItemListview   {
        void removeItem(int position, String docID);
    }

    private Context context;
    private int resource;
    private ArrayList<Wishlist> wishlistArrayList;
    private ICLickListenerOnItemListview mIClickListenerOnItemListview;

    public CustomWishlistAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Wishlist> objects, ICLickListenerOnItemListview listener) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.wishlistArrayList = objects;
        this.mIClickListenerOnItemListview = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Wishlist wishlistItem = wishlistArrayList.get(position);

        if (convertView == null)    {
            convertView = LayoutInflater.from(context).inflate(R.layout.wishlist_item,parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.profile_pic);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView downPrice = (TextView) convertView.findViewById(R.id.down_price);
        ImageButton btnOption = (ImageButton) convertView.findViewById(R.id.wishlist_item_option_btn);
        Button btnAddToCart = (Button) convertView.findViewById(R.id.wishlist_item_btn_add_to_cart);

        Glide.with(getContext()).load(wishlistItem.getProductImage()).into(imageView);
        name.setText(wishlistItem.getProductName());
        price.setText(wishlistItem.getRegularCost());
        downPrice.setText(wishlistItem.getDiscount());
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = getListOptionMenu();
                CustomOptionMenu customOptionMenu = new CustomOptionMenu(getContext(), new CustomOptionMenu.IClickListenerOnItemListview() {
                    @Override
                    public void onClickItem(int pos) {
                        // 0: Add to cart, 1: Remove from wishlist
                        if(pos == 0)    { // Add to cart

                        }

                        if(pos == 1)    { // Remove from wishlist
                            RemoveFromWishlist(position,wishlistItem.getKey());
                        }
                    }
                }, list, "OPTION", getListImg());
                customOptionMenu.show();

            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(wishlistItem.getKeyProduct());
                AddToCart addToCartDialog = new AddToCart(getContext(), wishlistItem.getKeyProduct());
//                addToCartDialog.show();
            }
        });

        return convertView;
    }

    private ArrayList<String> getListOptionMenu()   {
        ArrayList<String> list = new ArrayList<>();
        list.add("Add to cart");
        list.add("Remove from wishlist");

        return list;
    }

    private int[] getListImg()  {
        int[] listImg = {R.drawable.cart4,
                        R.drawable.trash};

        return listImg;
    }

    private void RemoveFromWishlist(int position, String docID)   {
        mIClickListenerOnItemListview.removeItem(position, docID);
    }
}
