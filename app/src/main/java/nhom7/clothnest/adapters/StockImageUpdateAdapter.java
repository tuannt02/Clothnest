package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import nhom7.clothnest.R;

public class StockImageUpdateAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> downloadList;

    private ArrayList<Bitmap> bitmapList;

    private View mView;

    public StockImageUpdateAdapter(Context mContext, ArrayList<String> downloadList) {
        this.mContext = mContext;
        this.downloadList = downloadList;
        bitmapList = new ArrayList<>();
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
//        bitmapList.add(getBitmapFromURL(downloadList.get(i)));

        return mView;
    }
//
//    public Bitmap getBitmapFromURL(String src) {
//        try {
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            // Log exception
//            return null;
//        }
//    }
//
//    public ArrayList<Bitmap> getBitmapList() {
//        return bitmapList;
//    }
}
