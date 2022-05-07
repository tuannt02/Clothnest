package nhom7.clothnest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.ProductDetail_Activity;
import nhom7.clothnest.models.Product_Thumbnail;

public class Product_ThumbnailAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Product_Thumbnail> listThumbnail;

    //Change screen
    private View mView;
    private Animation alpha;

    //reference
    private TextView tvProductName;
    private ImageView ivProductImage;
    private TextView tvRegularCost;
    private TextView tvDiscount;
    private TextView tvDiscountCost;
    private ImageView ivFavorite;

    public Product_ThumbnailAdapter(Context mContext, ArrayList<Product_Thumbnail> listThumbnail) {
        this.mContext = mContext;
        this.listThumbnail = listThumbnail;
    }

    @Override
    public int getCount() {
        return listThumbnail.size();
    }

    @Override
    public Object getItem(int i) {
        return listThumbnail.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = view = layoutInflater.inflate(R.layout.thumbnail, null);

        reference();
        getData(i);
        setEventsClick(i);

        return view;
    }

    private void getData(int i) {
        Product_Thumbnail product = listThumbnail.get(i);

        tvProductName.setText(product.getName());

        Glide.with(mContext).load(product.getMainImage()).into(ivProductImage);

        int discount = product.getDiscount();
        tvDiscount.setText("-" + discount + "%");

        Double price = Double.parseDouble(String.valueOf(product.getPrice()));
        tvRegularCost.setText("$" + price.toString().replaceAll("\\.?[0-9]*$", ""));

        Double discountPrice = price * (1 - discount / 100.0);
        tvDiscountCost.setText("$" + discountPrice.toString().replaceAll("\\.?[0-9]*$", ""));

        if(product.isFavorite())
            ivFavorite.setImageResource(R.drawable.is_favorite);
    }

    private void reference() {
        tvProductName = (TextView) mView.findViewById(R.id.productName);
        ivProductImage = (ImageView) mView.findViewById(R.id.productImage);
        tvRegularCost = (TextView) mView.findViewById(R.id.regularCost);
        tvDiscount = (TextView) mView.findViewById(R.id.discount);
        tvDiscountCost = (TextView) mView.findViewById(R.id.discountCost);
        ivFavorite = (ImageView) mView.findViewById(R.id.favoriteButton);
    }

    private void setEventsClick(int i) {
        //Click on product
        LinearLayout product = mView.findViewById(R.id.view_product);
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoDetail(i);
            }
        });

    }

    private void gotoDetail(int i) {
        Intent intent_productDetail = new Intent(mContext, ProductDetail_Activity.class);
        intent_productDetail.putExtra("selected_Thumbnail", listThumbnail.get(i).getId());
        alpha = AnimationUtils.loadAnimation(mContext, R.anim.alpha_anim);
        mView.startAnimation(alpha);
        mContext.startActivity(intent_productDetail);
    }
}
