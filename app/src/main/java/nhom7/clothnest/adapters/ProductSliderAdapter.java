package nhom7.clothnest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.ProductDetailActivity;
import nhom7.clothnest.models.Product_Thumbnail;

public class ProductSliderAdapter extends RecyclerView.Adapter<ProductSliderAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Product_Thumbnail> products;
    private View mView;

    public ProductSliderAdapter(Context mContext, ArrayList<Product_Thumbnail> products) {
        this.mContext = mContext;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = inflater.inflate(R.layout.thumbnail, parent, false);
        mView.setPadding(0, 0, 24, 0);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product_Thumbnail product = products.get(position);

        holder.tvProductName.setText(product.getName());

        Glide.with(mContext).load(product.getMainImage()).into(holder.ivProduct);

        Double price = Double.parseDouble(String.valueOf(product.getPrice()));
        holder.tvPrice.setText("$" + price.toString().replaceAll("\\.?[0-9]*$", ""));

        Double discount = Double.parseDouble(String.valueOf(product.getDiscount()));
        holder.tvDiscount.setText("-" + discount.toString().replaceAll("\\.?[0-9]*$", "") + "%");

        Double discountPrice = price * (1 - discount/100.0);
        holder.tvDiscountPrice.setText("$" + discountPrice.toString().replaceAll("\\.?[0-9]*$", ""));

        if(product.isFavorite())
            holder.ivFavorite.setImageResource(R.drawable.is_favorite);

        setEventsClick(position);
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
        Intent intent_productDetail = new Intent(mContext, ProductDetailActivity.class);
        intent_productDetail.putExtra("selected_Thumbnail", products.get(i).getId());
        mContext.startActivity(intent_productDetail);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProduct, ivFavorite;
        TextView tvProductName, tvDiscountPrice, tvDiscount, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.productImage);
            tvProductName = itemView.findViewById(R.id.productName);
            tvDiscount = itemView.findViewById(R.id.discount);
            tvDiscountPrice = itemView.findViewById(R.id.discountCost);
            tvPrice = itemView.findViewById(R.id.regularCost);
            ivFavorite = itemView.findViewById(R.id.favoriteButton);
        }
    }
}
