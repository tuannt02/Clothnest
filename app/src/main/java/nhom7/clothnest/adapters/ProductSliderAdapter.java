package nhom7.clothnest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.ProductDetail_Activity;
import nhom7.clothnest.models.CategoryItem;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;

public class ProductSliderAdapter extends RecyclerView.Adapter<ProductSliderAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Product_Thumbnail> products;

    public ProductSliderAdapter(Context mContext, ArrayList<Product_Thumbnail> products) {
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

        getEvent(holder);
    }

    private void getEvent(@NonNull ViewHolder holder) {
        holder.ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetail_Activity.class);
                mContext.startActivity(intent);
            }
        });
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
