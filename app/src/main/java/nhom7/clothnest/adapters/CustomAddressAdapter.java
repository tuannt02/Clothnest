package nhom7.clothnest.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

import nhom7.clothnest.models.Address;
import nhom7.clothnest.R;

public class CustomAddressAdapter extends BaseAdapter {
    private ArrayList<Address> addresses;
    private Context context;
    private LayoutInflater layoutInflater;

    public CustomAddressAdapter(ArrayList<Address> addresses, Context context) {
        this.addresses = addresses;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        Log.d("customAddressAdapter", "Construct successfully");
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Object getItem(int i) {
        return addresses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.d("customAddressAdapter", "Get View successfully");
        ViewHolder viewHolder = null;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.address_layout, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.fullName = view.findViewById(R.id.textview_fullname_address);
            viewHolder.detail = view.findViewById(R.id.textview_detail_address);
            viewHolder.address = view.findViewById(R.id.textview_address_address);
            viewHolder.phoneNumber = view.findViewById(R.id.textview_phoneNumber_address);
            viewHolder.edit = view.findViewById(R.id.imagebutton_edit_address);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Address address = addresses.get(i);

        viewHolder.fullName.setText(address.fullName);
        viewHolder.detail.setText(address.detail);
        viewHolder.address.setText(address.getProvince() + ", " + address.getDistrict() + ", " + address.getWard());
        viewHolder.phoneNumber.setText(address.phoneNumber);

        // set btnEdit click listener
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("edit-address");
                intent.putExtra("address", address);
                intent.putExtra("index", i);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                if (address == null) {
                    Log.d("customArrayAdapter", "Address is null ");
                }
            }
        });

        return view;
    }

    static class ViewHolder {
        TextView fullName, detail, address, phoneNumber;
        ImageButton edit;
    }
}
