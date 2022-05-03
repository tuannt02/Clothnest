<<<<<<< Updated upstream:app/src/main/java/nhom7/clothnest/CustomWishlistAdapter.java
package nhom7.clothnest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomWishlistAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;
    private ArrayList<Product> arrProd;


    public CustomWishlistAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.arrProd = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product prod = arrProd.get(position);

        if (convertView == null)    {
            convertView = LayoutInflater.from(context).inflate(R.layout.wishlist_item,parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.profile_pic);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView downPrice = (TextView) convertView.findViewById(R.id.down_price);

        imageView.setImageResource(prod.productImage);
        name.setText(prod.productName);
        price.setText(prod.regularCost);
        downPrice.setText(prod.discount);


        return convertView;
    }
}
=======
package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Product;

public class CustomWishlistAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;
    private ArrayList<Product> arrProd;


    public CustomWishlistAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.arrProd = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product prod = arrProd.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.wishlist_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.profile_pic);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView downPrice = (TextView) convertView.findViewById(R.id.down_price);

        imageView.setImageResource(prod.getProductImage());
        name.setText(prod.getProductName());
        price.setText(String.valueOf(prod.getRegularCost()));
        downPrice.setText(String.valueOf(
                prod.getDiscount()
        ));


        return convertView;
    }
}
>>>>>>> Stashed changes:app/src/main/java/nhom7/clothnest/adapters/CustomWishlistAdapter.java
