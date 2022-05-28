package nhom7.clothnest.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nhom7.clothnest.R;

public class StockImageUpdateAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> downloadList;

    View mView;

    public StockImageUpdateAdapter(Context mContext, ArrayList<String> downloadList) {
        this.mContext = mContext;
        this.downloadList = downloadList;
    }

    @Override
    public int getCount() {
        return downloadList.size();
    }

    @Override
    public Object getItem(int i) {
        return downloadList.get(i);
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

        Glide.with(mContext).load(downloadList.get(i)).into(ivStock);

        return mView;
    }
}
