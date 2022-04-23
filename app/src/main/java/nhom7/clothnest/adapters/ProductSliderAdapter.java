package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;

public class ProductSliderAdapter extends RecyclerView.Adapter<ProductSliderAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Product> products;

    public ProductSliderAdapter(Context mContext, ArrayList<Product> products) {
        this.mContext = mContext;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.thumbnail, parent, false);
        view.setPadding(0, 0, 24, 0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.ivProduct.setImageResource(product.getProductImage());
        holder.tvProductName.setText(product.getProductName());
        holder.tvPrice.setText(product.getRegularCost());
        holder.tvDiscount.setText("-" + product.getDiscount() + "%");

        Double price = Double.parseDouble(product.getRegularCost());
        Integer discount = Integer.parseInt(product.getDiscount());
        Double discountPrice = price * (1 - discount/100.0);

        holder.tvDiscountPrice.setText(discountPrice.toString().replaceAll("\\.?[0-9]*$", ""));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProduct;
        TextView tvProductName, tvDiscountPrice, tvDiscount, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.productImage);
            tvProductName = itemView.findViewById(R.id.productName);
            tvDiscount = itemView.findViewById(R.id.discount);
            tvDiscountPrice = itemView.findViewById(R.id.discountCost);
            tvPrice = itemView.findViewById(R.id.regularCost);
        }


    }
}
