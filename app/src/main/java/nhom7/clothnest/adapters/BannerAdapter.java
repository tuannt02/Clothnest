package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Banner;

public class BannerAdapter extends ArrayAdapter<Banner> {
    private Context context;
    private int resource;
    private ArrayList<Banner> bannerArrayList;

    public BannerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Banner> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.bannerArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Banner bannerItem = bannerArrayList.get(position);

        if (convertView == null)    {
            convertView = LayoutInflater.from(context).inflate(R.layout.banner_item,parent, false);
        }

        // Get view
        ImageView imgView = convertView.findViewById(R.id.banner_item_img);


        // Set view
        Glide.with(getContext()).load(bannerItem.getImg()).into(imgView);

        return convertView;
    }
}
