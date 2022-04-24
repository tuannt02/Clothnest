package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import nhom7.clothnest.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import nhom7.clothnest.models.SizeClass;

public class SizeAdapter extends ArrayAdapter<SizeClass> {

    public SizeAdapter(@NonNull Context context, int resource, @NonNull List<SizeClass> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_item_selected, parent, false);
        TextView SizeSelected = convertView.findViewById(R.id.sizeSeleted);

        SizeClass size = this.getItem(position);
        if(size != null)   {
            SizeSelected.setText(size.getSize());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_item, parent, false);
        TextView chooseSize = convertView.findViewById(R.id.txtSizeChoose);

        SizeClass size = this.getItem(position);
        if(size != null)   {
            chooseSize.setText(size.getName());
        }
        return convertView;
    }
}
