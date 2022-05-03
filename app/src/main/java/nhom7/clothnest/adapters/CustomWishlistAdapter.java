package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;

public class CustomWishlistAdapter extends ArrayAdapter<Product> {
    public interface ICLickListenerOnItemListview   {
        void removeItem(int position);
    }

    private Context context;
    private int resource;
    private ArrayList<Product> arrProd;
    private ICLickListenerOnItemListview mIClickListenerOnItemListview;

    public CustomWishlistAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects, ICLickListenerOnItemListview listener) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.arrProd = objects;
        this.mIClickListenerOnItemListview = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product prod = arrProd.get(position);

        if (convertView == null)    {
            convertView = LayoutInflater.from(context).inflate(R.layout.wishlist_item,parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.profile_pic);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView downPrice = (TextView) convertView.findViewById(R.id.down_price);
        ImageButton btnOption = (ImageButton) convertView.findViewById(R.id.wishlist_item_option_btn);


        imageView.setImageResource(prod.productImage);
        name.setText(prod.productName);
        price.setText(prod.regularCost);
        downPrice.setText(prod.discount);
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
                            RemoveFromWishlist(position);
                        }
                    }
                }, list, "OPTION", getListImg());
                customOptionMenu.show();

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

    private void RemoveFromWishlist(int position)   {
        mIClickListenerOnItemListview.removeItem(position);
    }
}
