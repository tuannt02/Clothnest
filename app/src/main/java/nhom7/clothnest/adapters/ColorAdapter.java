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
import android.widget.TextView;
import nhom7.clothnest.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import nhom7.clothnest.models.ColorClass;

public class ColorAdapter extends ArrayAdapter<ColorClass> {

    public ColorAdapter(@NonNull Context context, int resource, @NonNull List<ColorClass> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item_selected, parent, false);
        ImageView ColorSeleted = convertView.findViewById(R.id.colorSeleted);

        ColorClass color = this.getItem(position);
        if(color != null)   {
            Bitmap bmp = Bitmap.createBitmap(24, 24, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            canvas.drawColor(Color.parseColor(color.getHex()));
            ColorSeleted.setImageBitmap(bmp);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item, parent, false);
        TextView tvColor = convertView.findViewById(R.id.txtColorChoose);
        ImageView chooseColor = convertView.findViewById(R.id.colorChoose);

        ColorClass color = this.getItem(position);
        if(color != null)   {
            tvColor.setText(color.getName());
            Bitmap bmp = Bitmap.createBitmap(24, 24, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            canvas.drawColor(Color.parseColor(color.getHex()));
            chooseColor.setImageBitmap(bmp);
        }
        return convertView;
    }
}
