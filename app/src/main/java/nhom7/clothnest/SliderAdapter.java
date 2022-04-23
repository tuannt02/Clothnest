package nhom7.clothnest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private Context mContext;
    private List<SliderItem> itemList;

    public SliderAdapter(Context mContext, List<SliderItem> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_item, container, false);

        ImageView imageView = view.findViewById(R.id.sliderImage);
        SliderItem photo = itemList.get(position);
        if(photo != null){
            Glide.with(mContext).load(photo.getResourceID()).into(imageView);
        }
        //add view to viewgroup
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
