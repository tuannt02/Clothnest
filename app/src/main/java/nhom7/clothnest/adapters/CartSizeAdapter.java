package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.SizeClass;

public class CartSizeAdapter extends ArrayAdapter<SizeClass> {
    private Context context;
    private int resource;
    private ArrayList<SizeClass> sizeClassArrayList;

    private int pos = 0;


    public CartSizeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SizeClass> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.sizeClassArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SizeClass sizeItem = sizeClassArrayList.get(position);

        if (convertView == null)    {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_size_item,parent, false);
        }

        // Getview
        TextView nameSize = convertView.findViewById(R.id.cart_size_item_name);
        RelativeLayout cartSizeItem = convertView.findViewById(R.id.cart_size_item);

        // Setview
            // set view name
        nameSize.setText(sizeItem.getName());

        if(sizeItem.isSelected())  {
            cartSizeItem.setBackgroundResource(R.drawable.border_cart_select);
            nameSize.setTextColor(ContextCompat.getColor(context, R.color.pri_col));
        }
        else {
            cartSizeItem.setBackgroundResource(R.drawable.border_radius_linear9);
            nameSize.setTextColor(ContextCompat.getColor(context, R.color.black));
        }



        return convertView;
    }

    public void selectedItem(int position)
    {
        this.pos = position; //position must be a global variable
    }



}
