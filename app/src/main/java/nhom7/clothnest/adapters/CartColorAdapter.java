package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.ColorClass;

public class CartColorAdapter extends ArrayAdapter<ColorClass> {
    private Context context;
    private int resource;
    private ArrayList<ColorClass> colorClassArrayList;

    private int pos = 0;


    public CartColorAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ColorClass> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.colorClassArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ColorClass colorItem = colorClassArrayList.get(position);

        if (convertView == null)    {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_color_item,parent, false);
        }

        // Getview
        TextView nameColor = convertView.findViewById(R.id.cart_color_item_name);
        ImageView hexColor = convertView.findViewById(R.id.cart_color_item_hex);
        RelativeLayout cartColorItem = convertView.findViewById(R.id.cart_color_item);

        // Setview
            // set view name
        nameColor.setText(colorItem.getName());
            // set view hex
        Bitmap bmp = Bitmap.createBitmap(24, 24, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(Color.parseColor(colorItem.getHex()));
        hexColor.setImageBitmap(bmp);

        if(colorItem.isSelected())  {
            cartColorItem.setBackgroundResource(R.drawable.border_cart_select);
            nameColor.setTextColor(ContextCompat.getColor(context, R.color.pri_col));
        }
        else {
            cartColorItem.setBackgroundResource(R.drawable.border_radius_linear9);
            nameColor.setTextColor(ContextCompat.getColor(context, R.color.black));
        }



        return convertView;
    }

    public void selectedItem(int position)
    {
        this.pos = position; //position must be a global variable
    }



}
