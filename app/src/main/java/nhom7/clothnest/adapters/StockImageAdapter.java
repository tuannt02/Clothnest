package nhom7.clothnest.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import nhom7.clothnest.R;

public class StockImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Uri> uriList;
    View mView;

    public StockImageAdapter(Context mContext, ArrayList<Uri> uriList) {
        this.mContext = mContext;
        this.uriList = uriList;
    }

    @Override
    public int getCount() {
        return uriList.size();
    }

    @Override
    public Object getItem(int i) {
        return uriList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mView = view = inflater.inflate(R.layout.item_stock_image, null);

        ImageView ivStock = mView.findViewById(R.id.item_stock_ivImageItem);

        ivStock.setImageURI(uriList.get(i));

        return mView;
    }
}
