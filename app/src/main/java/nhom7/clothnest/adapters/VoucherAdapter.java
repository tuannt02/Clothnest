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
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Voucher;
import nhom7.clothnest.models.Wishlist;

public class VoucherAdapter extends ArrayAdapter<Voucher> {

    private Context context;
    private int resource;
    private ArrayList<Voucher> voucherArrayList;


    public VoucherAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Voucher> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.voucherArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Voucher voucherItem = voucherArrayList.get(position);

        if (convertView == null)    {
            convertView = LayoutInflater.from(context).inflate(R.layout.voucher_item,parent, false);
        }

        // Get view
        TextView txtCode = convertView.findViewById(R.id.voucher_item_txt_code);
        TextView txtDiscount = convertView.findViewById(R.id.voucher_item_txt_discount);

        // Set view
        txtCode.setText(voucherItem.getCode());
        txtDiscount.setText(String.valueOf(voucherItem.getDiscount()) + '%');


        return convertView;


    }
}
