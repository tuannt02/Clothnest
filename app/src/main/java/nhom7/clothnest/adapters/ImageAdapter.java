package nhom7.clothnest.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Image;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ArrayList<Image> arrayList;
    private ViewPager2 viewPager2;
    private Context context;
    private  RoundedImageView imageView;

    public ImageAdapter(ArrayList<Image> arrayList, ViewPager2 viewPager2, Context context) {
        this.arrayList = arrayList;
        this.viewPager2 = viewPager2;
        this.context= context;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_container,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Image image = arrayList.get(position);
        Glide.with(context).load(image.getImage()).into(imageView);
        if (position==arrayList.size()-2)
        {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
        }
    }
    private Runnable runnable= new Runnable() {
        @Override
        public void run() {
            arrayList.addAll(arrayList);
            notifyDataSetChanged();

        }
    };
}
