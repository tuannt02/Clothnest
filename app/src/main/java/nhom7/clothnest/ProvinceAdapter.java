package nhom7.clothnest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ProvinceAdapter extends ArrayAdapter<Province> {
    private Context mContext;
    private ArrayList<Province> provinces;
    private int mResource;

    public ProvinceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Province> provinces) {
        super(context, resource, provinces);
        mContext = context;
        mResource = resource;
        this.provinces = provinces;
    }

    @Override
    public int getCount() {
        return provinces.size();
    }


    @Override
    public Province getItem(int i) {
        return provinces.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(mResource, viewGroup, false);
        Province province = provinces.get(i);
        TextView tvName = view.findViewById(R.id.textview_address_item);
        tvName.setText(province.getName());
        return view;
    }
}
