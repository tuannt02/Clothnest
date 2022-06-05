package nhom7.clothnest.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.Admin_ProductDetailActivity;
import nhom7.clothnest.activities.CartActivity;
import nhom7.clothnest.models.Product_Admin;
import nhom7.clothnest.util.customizeComponent.CustomToast;

public class Product_AdminReadOnlyAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Product_Admin> productAdminList;

    //Change screen
    private View mView;

    //reference
    private TextView tvProductName;
    private ImageView ivProductImage;
    private TextView tvID;
    private TextView tvCost;
    private TextView tvStock;
    private LinearLayout product;
    String key,iscollection;


    public Product_AdminReadOnlyAdapter(Context mContext, ArrayList<Product_Admin> productAdminList,String key,String iscollection) {
        this.mContext = mContext;
        this.productAdminList = productAdminList;
        this.key = key;
        this.iscollection= iscollection;
    }

    @Override
    public int getCount() {
        return productAdminList.size();
    }

    @Override
    public Object getItem(int i) {
        return productAdminList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = view = layoutInflater.inflate(R.layout.item_admin_productlist, null);

        reference();
        getData(i);
        if(iscollection==null)
        setEventsClickArrivals(i);
        else
        setEventsClickCollectionsWinter(i);
        return view;
    }


    private void setEventsClickArrivals(int i) {
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(key)
                        .whereEqualTo("product_id", db.document("/products/" + productAdminList.get(i).getId()))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Boolean check = false;
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        check = true;
                                        break;
                                    }
                                    if (check) {
                                        CustomToast.DisplayToast(mContext,
                                                2,
                                                "Product have already existed");
                                    } else {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("product_id", db.document("/products/" + productAdminList.get(i).getId()));
                                        map.put("date_add", Calendar.getInstance().getTime());
                                        db.collection(key).add(map);

                                        CustomToast.DisplayToast(mContext,
                                                1,
                                                "Add product is sucessfully");
                                    }

                                }
                            }
                        });
            }
        });

    }

    private void setEventsClickCollectionsWinter(int i) {
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("collections")
                        .document(key)
                        .collection("products")
                        .whereEqualTo("product_id", db.document("/products/" + productAdminList.get(i).getId()))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Boolean check = false;
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        check = true;
                                        break;
                                    }
                                    if (check) {
                                        CustomToast.DisplayToast(mContext,
                                                2,
                                                "Product have already existed");
                                    } else {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("product_id", db.document("/products/" + productAdminList.get(i).getId()));
                                        map.put("date_add", Calendar.getInstance().getTime());
                                        db.collection("collections")
                                                .document(key)
                                                .collection("products")
                                                .add(map);
                                        CustomToast.DisplayToast(mContext,
                                                1,
                                                "Add product is sucessfully");
                                    }
                                }
                            }
                        });
            }
        });
    }

    private void getData(int i) {
        Product_Admin product = productAdminList.get(i);

        tvProductName.setText(product.getName());

        Glide.with(mContext).load(product.getMainImage()).into(ivProductImage);

        tvID.setText("ID: " + product.getId());

        Double price = Double.parseDouble(String.valueOf(product.getPrice()));
        tvCost.setText("$" + price.toString().replaceAll("\\.?[0-9]*$", ""));

        tvStock.setText("" + product.getStock());
    }

    private void reference()
    {
        tvProductName = (TextView) mView.findViewById(R.id.item_admin_productList_productName);
        ivProductImage = (ImageView) mView.findViewById(R.id.item_admin_productList_productImage);
        tvID = (TextView) mView.findViewById(R.id.item_admin_productList_productID);
        tvCost = (TextView) mView.findViewById(R.id.item_admin_productList_productPrice);
        tvStock = (TextView) mView.findViewById(R.id.item_admin_productList_productStock);
        product = (LinearLayout) mView.findViewById(R.id.item_admin_productList_View);
    }
}
