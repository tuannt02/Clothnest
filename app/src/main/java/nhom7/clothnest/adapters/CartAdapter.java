package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import nhom7.clothnest.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.models.CartItem;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.models.Purchase;
import nhom7.clothnest.models.PurchaseItem;
import nhom7.clothnest.models.SizeClass;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;

public class CartAdapter extends ArrayAdapter<CartItem> {
    public interface ICLickListenerOnItemListview   {
        void removeItem(int position);
    }

    private Context context;
    private int resource;
    private ArrayList<CartItem> listCart;
    private ICLickListenerOnItemListview mIClickListenerOnItemListview;

    public CartAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CartItem> objects, ICLickListenerOnItemListview listener) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.listCart = objects;
        this.mIClickListenerOnItemListview = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CartItem cartItem = listCart.get(position);

        if (convertView == null)    {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item,parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.cart_pic);
        TextView name = (TextView) convertView.findViewById(R.id.cart_name);
        TextView price = (TextView) convertView.findViewById(R.id.cart_price);
        Spinner cbxColor = (Spinner) convertView.findViewById(R.id.spn_color);
        Spinner cbxSize = (Spinner) convertView.findViewById(R.id.spn_size);
        ImageButton btnOption = (ImageButton) convertView.findViewById(R.id.cart_item_option_btn);

        setupOnClickListener(convertView, cartItem);

        ColorAdapter colorAdapter = new ColorAdapter(context, R.layout.color_item_selected, getListColor());
        SizeAdapter sizeAdapter = new SizeAdapter(context, R.layout.size_item_selected, getListSize());
        cbxColor.setAdapter(colorAdapter);
        cbxSize.setAdapter(sizeAdapter);

        imageView.setImageResource(cartItem.getImage());
        name.setText(cartItem.getTitle());
        price.setText('$' + Double.toString(cartItem.getPrice()));
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = getListOptionMenu();
                CustomOptionMenu customOptionMenu = new CustomOptionMenu(getContext(), new CustomOptionMenu.IClickListenerOnItemListview() {
                    @Override
                    public void onClickItem(int pos) {
                        // 0: Add to wishlist, 1: Remove from cart
                        if(pos == 0)    { // Add to cart

                        }

                        if(pos == 1)    { // Remove from wishlist
                            RemoveFromCart(position);
                        }
                    }
                }, list, "OPTION", getListImg());
                customOptionMenu.show();

            }
        });


        return convertView;
    }

    private void setupOnClickListener(View convertView, CartItem cartItem) {
        TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantityCart);
        ImageButton btnAdd = (ImageButton) convertView.findViewById(R.id.btnAddCart);
        ImageButton btnSub = (ImageButton) convertView.findViewById(R.id.btnSubCart);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItem.incrementQuantity();
                txtQuantity.setText(Integer.toString(cartItem.getQty()));
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItem.decreaseQuantity();
                txtQuantity.setText(Integer.toString(cartItem.getQty()));
            }
        });
    }

    private ArrayList<String> getListOptionMenu()   {
        ArrayList<String> list = new ArrayList<>();
        list.add("Add to wishlist");
        list.add("Remove from cart");

        return list;
    }

    private int[] getListImg()  {
        int[] listImg = {R.drawable.cart4,
                R.drawable.trash};

        return listImg;
    }

    private List<ColorClass> getListColor() {
        List<ColorClass> listColor = new ArrayList<>();
        listColor.add(new ColorClass("white", "#FFFFFF"));
        listColor.add(new ColorClass("black", "#000000"));
        listColor.add(new ColorClass("blue", "#0000FF"));
        listColor.add(new ColorClass("pink", "#F335A6"));
        return  listColor;
    }

    private List<SizeClass> getListSize()   {
        List<SizeClass> listSize = new ArrayList<>();
        listSize.add(new SizeClass("NAM S", "S"));
        listSize.add(new SizeClass("NAM M", "M"));
        listSize.add(new SizeClass("NAM 29INCH", "29INCH"));

        return listSize;
    }

    private void RemoveFromCart(int position)   {
        mIClickListenerOnItemListview.removeItem(position);
    }
}
