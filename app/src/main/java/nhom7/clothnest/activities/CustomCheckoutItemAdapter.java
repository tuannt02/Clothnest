package nhom7.clothnest.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.models.CartItem;

public class CustomCheckoutItemAdapter extends ArrayAdapter<CartItem> {
    private Context context;
    private int resource;
    private ArrayList<CartItem> cartItems;

    public CustomCheckoutItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CartItem> cartItems) {
        super(context, resource, cartItems);
        this.context = context;
        this.resource = resource;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);

        CartItem item = cartItems.get(position);

        // Declare UI Views
        ImageView ivProduct;
        TextView tvName, tvSize, tvColor, tvQty, tvPrice;

        // Initialize UI Views
        ivProduct = convertView.findViewById(R.id.imageview_product);
        tvName = convertView.findViewById(R.id.textview_name);
        tvSize = convertView.findViewById(R.id.textview_size);
        tvColor = convertView.findViewById(R.id.textview_color);
        tvQty = convertView.findViewById(R.id.textview_qty);
        tvPrice = convertView.findViewById(R.id.textview_price);

        // Set data for UI Views
        Picasso.get().load(item.getImg()).into(ivProduct);
        tvName.setText(item.getName());
        tvSize.setText(item.getSizeSelected().getShort_name());
        tvColor.setText(item.getColorSelected().getName());
        tvQty.setText(item.getQty() + "");
        tvPrice.setText(item.getDiscountPrice() + "");

        return convertView;
    }
}
