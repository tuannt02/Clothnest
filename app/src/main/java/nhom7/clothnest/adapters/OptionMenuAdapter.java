package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Product;

public class OptionMenuAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resource;
    private ArrayList<String> arrayList;
    int[] listImage;

    public OptionMenuAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects, int[] listImg) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;
        this.listImage = listImg;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String item = arrayList.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.option_menu_item,parent, false);
        }

        TextView txtItem = (TextView) convertView.findViewById(R.id.option_menu_item_in_list);
        if(listImage != null)   {
            txtItem.setCompoundDrawablesWithIntrinsicBounds(listImage[position], 0, 0, 0);
        }

        txtItem.setText(item);

        return convertView;
    }
}
