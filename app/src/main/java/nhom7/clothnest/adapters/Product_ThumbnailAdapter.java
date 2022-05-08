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

import androidx.annotation.NonNull;

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
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;

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

    public static void getProductAndPushToGridView(ArrayList<Product_Thumbnail> listProduct, Product_ThumbnailAdapter thumbnailAdapter){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //get data
        db.collection(Product_Thumbnail.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            //Duyệt từng product
                            for(QueryDocumentSnapshot document: task.getResult()){
                                //product để thêm vào arrayList
                                Product_Thumbnail thumbnail = new Product_Thumbnail();
                                //Thêm thumbnail vào arraylist và notifyDataSetChanged
                                listProduct.add(thumbnail);

                                //tempOject chứa product
                                Map<String, Object> tempObject = document.getData();

                                // Lặp qua từng field của một document
                                Iterator myVeryOwnIterator = tempObject.keySet().iterator();
                                while(myVeryOwnIterator.hasNext()){
                                    String key = (String) myVeryOwnIterator.next();

                                    //Set id
                                    thumbnail.setId(document.getId());

                                    //set category
                                    if(key.equals("category")){
                                        DocumentReference docRef = (DocumentReference) tempObject.get(key);
                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                thumbnail.setCategory(documentSnapshot.getString("name"));
                                                thumbnailAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    //Set name
                                    if(key.equals("name")){
                                        thumbnail.setName((String) tempObject.get(key));
                                        thumbnailAdapter.notifyDataSetChanged();
                                    }

                                    //Set price
                                    if(key.equals("price")){
                                        Double price = document.getDouble(key);
                                        thumbnail.setPrice(price);
                                        thumbnailAdapter.notifyDataSetChanged();
                                    }
                                    //set discount
                                    if(key.equals("discount")){
                                        int discount = (int)Math.round(document.getDouble(key));
                                        thumbnail.setDiscount(discount);
                                        thumbnailAdapter.notifyDataSetChanged();
                                    }
                                    //set mainImage
                                    if(key.equals("main_img")){
                                        thumbnail.setMainImage((String) tempObject.get(key));
                                        thumbnailAdapter.notifyDataSetChanged();
                                    }
                                    //set isFavorite
                                    FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
                                    DocumentReference doRef = db.document(Product_Thumbnail.COLLECTION_NAME + '/' + document.getId());

                                    CollectionReference coRef_wishList = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);
                                    Query query = coRef_wishList.whereEqualTo("product_id", doRef);
                                    query.limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                thumbnail.setFavorite(!task.getResult().isEmpty());
                                                thumbnailAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    }
                });
    }
}
