package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import nhom7.clothnest.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nhom7.clothnest.models.CartItem;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.models.Purchase;
import nhom7.clothnest.models.PurchaseItem;
import nhom7.clothnest.models.SizeClass;
import nhom7.clothnest.models.User;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;

public class CartAdapter extends ArrayAdapter<CartItem> {
    public interface ICLickListenerOnItemListview   {
        void addItemToWishlist(String keyProduct);
        void removeItem(int position, String docID);
        void updateTotalPrice();
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

        // Get view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.cart_pic);
        TextView name = (TextView) convertView.findViewById(R.id.cart_name);
        TextView price = (TextView) convertView.findViewById(R.id.cart_price);
        Spinner cbxColor = (Spinner) convertView.findViewById(R.id.spn_color);
        TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantityCart);
        Spinner cbxSize = (Spinner) convertView.findViewById(R.id.spn_size);
        ImageButton btnOption = (ImageButton) convertView.findViewById(R.id.cart_item_option_btn);

        setupOnClickListener(convertView, cartItem, position);

        // set view
        ColorAdapter colorAdapter = new ColorAdapter(context, R.layout.color_item_selected, cartItem.getListColor());
        SizeAdapter sizeAdapter = new SizeAdapter(context, R.layout.size_item_selected, cartItem.getListSize());
        cbxColor.setAdapter(colorAdapter);
        cbxColor.setSelection(cartItem.getPosSelectedColor());
        cbxSize.setAdapter(sizeAdapter);
        cbxSize.setSelection(cartItem.getPosSelectedSize());

        cbxColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateColorSelected(cartItem.getKey(), position, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cbxSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateSizeSelected(cartItem.getKey(), position, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        Glide.with(getContext()).load(cartItem.getImg()).into(imageView);
        name.setText(cartItem.getName());
        txtQuantity.setText(Integer.toString(cartItem.getQty()));
        price.setText("$ " + Integer.toString(cartItem.getDiscountPrice()));
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = getListOptionMenu();
                CustomOptionMenu customOptionMenu = new CustomOptionMenu(getContext(), new CustomOptionMenu.IClickListenerOnItemListview() {
                    @Override
                    public void onClickItem(int pos) {
                        // 0: Add to wishlist, 1: Remove from cart
                        if(pos == 0)    { // Add to wishlist
                            addItemToWishlist(cartItem.getKeyProduct());
                        }

                        if(pos == 1)    { // Remove from cart
                            RemoveFromCart(position, cartItem.getKey());
                            UpdateTotalPrice();
                        }
                    }
                }, list, "OPTION", getListImg());
                customOptionMenu.show();

            }
        });


        return convertView;
    }

    private void setupOnClickListener(View convertView, CartItem cartItem, int pos) {
        TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantityCart);
        ImageButton btnAdd = (ImageButton) convertView.findViewById(R.id.btnAddCart);
        ImageButton btnSub = (ImageButton) convertView.findViewById(R.id.btnSubCart);
        TextView txtPrice = (TextView) convertView.findViewById(R.id.cart_price);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItem.incrementQuantity();
                UpdatePrice(txtPrice, cartItem);
                UpdateTotalPrice();
                txtQuantity.setText(Integer.toString(cartItem.getQty()));
                updateQty(cartItem.getKey(),pos);
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItem.decreaseQuantity();
                UpdatePrice(txtPrice, cartItem);
                UpdateTotalPrice();
                txtQuantity.setText(Integer.toString(cartItem.getQty()));
                updateQty(cartItem.getKey(),pos);
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
        listColor.add(new ColorClass("white", "#FFFFFF", false));
        listColor.add(new ColorClass("black", "#000000", false));
        listColor.add(new ColorClass("blue", "#0000FF", true));
        listColor.add(new ColorClass("pink", "#F335A6", false));
        return  listColor;
    }

    private List<SizeClass> getListSize()   {
        List<SizeClass> listSize = new ArrayList<>();
        listSize.add(new SizeClass("NAM S", "S"));
        listSize.add(new SizeClass("NAM M", "M"));
        listSize.add(new SizeClass("NAM 29INCH", "29INCH"));

        return listSize;
    }

    private void addItemToWishlist(String keyProduct)    {
        mIClickListenerOnItemListview.addItemToWishlist(keyProduct);
    }

    private void RemoveFromCart(int position, String docID)   {
        mIClickListenerOnItemListview.removeItem(position, docID);
    }

    private void UpdateTotalPrice() {
        mIClickListenerOnItemListview.updateTotalPrice();
    }

    private void UpdatePrice(TextView txtPrice, CartItem cartItem)  {
        int priceUpdate = cartItem.getDiscountPrice();
        txtPrice.setText("$ " + Integer.toString(cartItem.getDiscountPrice()));
    }

    private void updateColorSelected(String docID, int pos, int i) {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        DocumentReference colorRef = listCart.get(pos).getListColor().get(i).getColorRef();
        data.put("color_selected", colorRef);
        db.collection(User.COLLECTION_NAME)
            .document(userInfo.getUid())
            .collection(CartItem.COLLECTION_NAME)
            .document(docID).set(data, SetOptions.merge());
    }

    private void updateSizeSelected(String docID, int pos, int i) {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        DocumentReference sizeRef = listCart.get(pos).getListSize().get(i).getSizeRef();
        data.put("size_selected", sizeRef);
        db.collection(User.COLLECTION_NAME)
                .document(userInfo.getUid())
                .collection(CartItem.COLLECTION_NAME)
                .document(docID).set(data, SetOptions.merge());
    }

    private void updateQty(String docID, int pos)   {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        int qty = listCart.get(pos).getQty();
        data.put("quantity", qty);
        db.collection(User.COLLECTION_NAME)
                .document(userInfo.getUid())
                .collection(CartItem.COLLECTION_NAME)
                .document(docID).set(data, SetOptions.merge());
    }

}
