package nhom7.clothnest.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        TextView fullName, detail, address, phoneNumber;
        ImageView edit;

        view = LayoutInflater.from(context).inflate(R.layout.address_layout, viewGroup, false);

        Log.d("customAddressAdapter", "Get View successfully");
        fullName = view.findViewById(R.id.textview_fullname_address);
        detail = view.findViewById(R.id.textview_detail_address);
        address = view.findViewById(R.id.textview_address_address);
        phoneNumber = view.findViewById(R.id.textview_phoneNumber_address);
        edit = view.findViewById(R.id.imageview_edit_address);

        Address currAddress = addresses.get(i);

        fullName.setText(currAddress.fullName);
        detail.setText(currAddress.detail);
        address.setText(currAddress.getProvince() + ", " + currAddress.getDistrict() + ", " + currAddress.getWard());
        phoneNumber.setText(currAddress.phoneNumber);

        // set btnEdit click listener
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("edit-address");
                intent.putExtra("address", currAddress);
                intent.putExtra("index", i);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                if (address == null) {
                    Log.d("customArrayAdapter", "Address is null ");
                }
            }
        });

        return view;
    }

}
