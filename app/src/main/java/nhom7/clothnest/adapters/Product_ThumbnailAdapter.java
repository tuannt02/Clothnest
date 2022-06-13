package nhom7.clothnest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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
import java.util.List;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.ProductDetailActivity;
import nhom7.clothnest.fragments.FilterFragment;
import nhom7.clothnest.fragments.SearchProductsFragment;
import nhom7.clothnest.models.CategoryItem;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.Stock;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;

public class Product_ThumbnailAdapter extends ArrayAdapter<Product_Thumbnail> {
    private Context mContext;
    private List<Product_Thumbnail> listThumbnail, originListThumbnail;

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
    public boolean isAvailable = false;

    public Product_ThumbnailAdapter(@NonNull Context context, int resource, @NonNull List<Product_Thumbnail> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listThumbnail = objects;
        this.originListThumbnail = objects;
    }

    @Override
    public int getCount() {
        return listThumbnail.size();
    }

    @Override
    public Product_Thumbnail getItem(int i) {
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

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                if (charSequence.equals("")) {
                    results.count = originListThumbnail.size();
                    results.values = originListThumbnail;
                    return results;
                }

                ArrayList<Product_Thumbnail> filteredThumbnail = new ArrayList<>();

                charSequence = charSequence.toString().toLowerCase();
                for (int i = 0; i < originListThumbnail.size(); i++) {
                    String product_name = originListThumbnail.get(i).getName();
                    if (product_name.toLowerCase().contains(charSequence.toString())) {
                        filteredThumbnail.add(originListThumbnail.get(i));
                    }
                }

                results.count = filteredThumbnail.size();
                results.values = filteredThumbnail;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listThumbnail = (List<Product_Thumbnail>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
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

        if (product.isFavorite())
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
        Intent intent_productDetail = new Intent(mContext, ProductDetailActivity.class);
        intent_productDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent_productDetail.putExtra("selected_Thumbnail", listThumbnail.get(i).getId());
        intent_productDetail.putExtra("write_review", "not_write_review");
        alpha = AnimationUtils.loadAnimation(mContext, R.anim.alpha_anim);
        mView.startAnimation(alpha);
        mContext.startActivity(intent_productDetail);
    }

    public static void getProductFromCollection(ArrayList<Product_Thumbnail> listThumbnail, Product_ThumbnailAdapter thumbnailAdapter) {
        String temp = Product_Thumbnail.COLECTION_NAME_COLLECTIONS + '/' + "WINTER" + '/' + Product_Thumbnail.COLECTION_NAME_PRODUCTS;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(temp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //check available
                                document.getReference().collection(Stock.COLLECTION_NAME)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int stock = 0;
                                                    for (QueryDocumentSnapshot stock_Ref : task.getResult()) {
                                                        stock += (int) Math.round(stock_Ref.getDouble("quantity"));
                                                        if (task.getResult().getDocuments().indexOf(stock_Ref) == task.getResult().size() - 1) {
                                                            if (stock != 0) {
                                                                //get data
                                                                Map<String, Object> map = document.getData();
                                                                Iterator iterator = map.keySet().iterator();
                                                                while (iterator.hasNext()) {
                                                                    String key = (String) iterator.next();

                                                                    if (key.equals("product_id")) {
                                                                        DocumentReference documentReference = (DocumentReference) map.get(key);
                                                                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                Product_Thumbnail thumbnail = new Product_Thumbnail();
                                                                                listThumbnail.add(thumbnail);

                                                                                thumbnail.setId(documentSnapshot.getId());

                                                                                thumbnail.setName(documentSnapshot.getString("name"));
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                thumbnail.setPrice(documentSnapshot.getDouble("price"));
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                int discount = (int) Math.round(documentSnapshot.getDouble("discount"));
                                                                                thumbnail.setDiscount(discount);
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                thumbnail.setMainImage(documentSnapshot.getString("main_img"));
                                                                                thumbnailAdapter.notifyDataSetChanged();


                                                                                DocumentReference docRef = (DocumentReference) documentSnapshot.get("category");
                                                                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                        thumbnail.setCategory(documentSnapshot.getString("name"));
                                                                                        thumbnailAdapter.notifyDataSetChanged();
                                                                                    }
                                                                                });

                                                                                FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
                                                                                DocumentReference doRef = db.document(Product_Thumbnail.COLLECTION_NAME + '/' + documentSnapshot.getId());

                                                                                CollectionReference coRef_wishList = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);
                                                                                Query query = coRef_wishList.whereEqualTo("product_id", doRef);
                                                                                query.limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            thumbnail.setFavorite(!task.getResult().isEmpty());
                                                                                            thumbnailAdapter.notifyDataSetChanged();
                                                                                        }
                                                                                    }
                                                                                });

                                                                            }
                                                                        });
                                                                    }


                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    public static void getProductLineFromCollection(ArrayList<Product_Thumbnail> listThumbnail, Product_ThumbnailAdapter thumbnailAdapter) {
        String temp = Product_Thumbnail.COLECTION_NAME_COLLECTIONS + '/' + "LINE" + '/' + Product_Thumbnail.COLECTION_NAME_PRODUCTS;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(temp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //check available
                                document.getReference().collection(Stock.COLLECTION_NAME)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int stock = 0;
                                                    for (QueryDocumentSnapshot stock_Ref : task.getResult()) {
                                                        stock += (int) Math.round(stock_Ref.getDouble("quantity"));
                                                        if (task.getResult().getDocuments().indexOf(stock_Ref) == task.getResult().size() - 1) {
                                                            if (stock != 0) {
                                                                //get data
                                                                Map<String, Object> map = document.getData();
                                                                Iterator iterator = map.keySet().iterator();
                                                                while (iterator.hasNext()) {
                                                                    String key = (String) iterator.next();

                                                                    if (key.equals("product_id")) {
                                                                        DocumentReference documentReference = (DocumentReference) map.get(key);
                                                                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                Product_Thumbnail thumbnail = new Product_Thumbnail();
                                                                                listThumbnail.add(thumbnail);

                                                                                thumbnail.setId(documentSnapshot.getId());

                                                                                thumbnail.setName(documentSnapshot.getString("name"));
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                thumbnail.setPrice(documentSnapshot.getDouble("price"));
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                int discount = (int) Math.round(documentSnapshot.getDouble("discount"));
                                                                                thumbnail.setDiscount(discount);
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                thumbnail.setMainImage(documentSnapshot.getString("main_img"));
                                                                                thumbnailAdapter.notifyDataSetChanged();


                                                                                DocumentReference docRef = (DocumentReference) documentSnapshot.get("category");
                                                                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                        thumbnail.setCategory(documentSnapshot.getString("name"));
                                                                                        thumbnailAdapter.notifyDataSetChanged();
                                                                                    }
                                                                                });

                                                                                FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
                                                                                DocumentReference doRef = db.document(Product_Thumbnail.COLLECTION_NAME + '/' + documentSnapshot.getId());

                                                                                CollectionReference coRef_wishList = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);
                                                                                Query query = coRef_wishList.whereEqualTo("product_id", doRef);
                                                                                query.limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            thumbnail.setFavorite(!task.getResult().isEmpty());
                                                                                            thumbnailAdapter.notifyDataSetChanged();
                                                                                        }
                                                                                    }
                                                                                });

                                                                            }
                                                                        });
                                                                    }


                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });

                            }
                        }
                    }
                });
    }

    public static void getProductUTFromCollection(ArrayList<Product_Thumbnail> listThumbnail, Product_ThumbnailAdapter thumbnailAdapter) {
        String temp = Product_Thumbnail.COLECTION_NAME_COLLECTIONS + '/' + "UT" + '/' + Product_Thumbnail.COLECTION_NAME_PRODUCTS;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(temp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //check available
                                document.getReference().collection(Stock.COLLECTION_NAME)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int stock = 0;
                                                    for (QueryDocumentSnapshot stock_Ref : task.getResult())
                                                        stock += (int) Math.round(stock_Ref.getDouble("quantity"));
                                                    if (stock != 0) {
                                                        //get data
                                                        Map<String, Object> map = document.getData();
                                                        Iterator iterator = map.keySet().iterator();
                                                        while (iterator.hasNext()) {
                                                            String key = (String) iterator.next();

                                                            if (key.equals("product_id")) {
                                                                DocumentReference documentReference = (DocumentReference) map.get(key);
                                                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        Product_Thumbnail thumbnail = new Product_Thumbnail();
                                                                        listThumbnail.add(thumbnail);

                                                                        thumbnail.setId(documentSnapshot.getId());

                                                                        thumbnail.setName(documentSnapshot.getString("name"));
                                                                        thumbnailAdapter.notifyDataSetChanged();

                                                                        thumbnail.setPrice(documentSnapshot.getDouble("price"));
                                                                        thumbnailAdapter.notifyDataSetChanged();

                                                                        int discount = (int) Math.round(documentSnapshot.getDouble("discount"));
                                                                        thumbnail.setDiscount(discount);
                                                                        thumbnailAdapter.notifyDataSetChanged();

                                                                        thumbnail.setMainImage(documentSnapshot.getString("main_img"));
                                                                        thumbnailAdapter.notifyDataSetChanged();


                                                                        DocumentReference docRef = (DocumentReference) documentSnapshot.get("category");
                                                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                thumbnail.setCategory(documentSnapshot.getString("name"));
                                                                                thumbnailAdapter.notifyDataSetChanged();
                                                                            }
                                                                        });

                                                                        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
                                                                        DocumentReference doRef = db.document(Product_Thumbnail.COLLECTION_NAME + '/' + documentSnapshot.getId());

                                                                        CollectionReference coRef_wishList = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);
                                                                        Query query = coRef_wishList.whereEqualTo("product_id", doRef);
                                                                        query.limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    thumbnail.setFavorite(!task.getResult().isEmpty());
                                                                                    thumbnailAdapter.notifyDataSetChanged();
                                                                                }
                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                            }


                                                        }
                                                    }
                                                }
                                            }
                                        });

                            }
                        }
                    }
                });
    }

    public static void getProductUnisexFromCollection(ArrayList<Product_Thumbnail> listThumbnail, Product_ThumbnailAdapter thumbnailAdapter) {
        String temp = Product_Thumbnail.COLECTION_NAME_COLLECTIONS + '/' + "UNISEX" + '/' + Product_Thumbnail.COLECTION_NAME_PRODUCTS;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(temp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //check available
                                document.getReference().collection(Stock.COLLECTION_NAME)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int stock = 0;
                                                    for (QueryDocumentSnapshot stock_Ref : task.getResult()) {
                                                        stock += (int) Math.round(stock_Ref.getDouble("quantity"));
                                                        if (task.getResult().getDocuments().indexOf(stock_Ref) == task.getResult().size() - 1) {
                                                            if (stock != 0) {
                                                                //get data
                                                                Map<String, Object> map = document.getData();
                                                                Iterator iterator = map.keySet().iterator();
                                                                while (iterator.hasNext()) {
                                                                    String key = (String) iterator.next();

                                                                    if (key.equals("product_id")) {
                                                                        DocumentReference documentReference = (DocumentReference) map.get(key);
                                                                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                Product_Thumbnail thumbnail = new Product_Thumbnail();
                                                                                listThumbnail.add(thumbnail);

                                                                                thumbnail.setId(documentSnapshot.getId());

                                                                                thumbnail.setName(documentSnapshot.getString("name"));
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                thumbnail.setPrice(documentSnapshot.getDouble("price"));
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                int discount = (int) Math.round(documentSnapshot.getDouble("discount"));
                                                                                thumbnail.setDiscount(discount);
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                thumbnail.setMainImage(documentSnapshot.getString("main_img"));
                                                                                thumbnailAdapter.notifyDataSetChanged();


                                                                                DocumentReference docRef = (DocumentReference) documentSnapshot.get("category");
                                                                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                        thumbnail.setCategory(documentSnapshot.getString("name"));
                                                                                        thumbnailAdapter.notifyDataSetChanged();
                                                                                    }
                                                                                });

                                                                                FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
                                                                                DocumentReference doRef = db.document(Product_Thumbnail.COLLECTION_NAME + '/' + documentSnapshot.getId());

                                                                                CollectionReference coRef_wishList = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);
                                                                                Query query = coRef_wishList.whereEqualTo("product_id", doRef);
                                                                                query.limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            thumbnail.setFavorite(!task.getResult().isEmpty());
                                                                                            thumbnailAdapter.notifyDataSetChanged();
                                                                                        }
                                                                                    }
                                                                                });

                                                                            }
                                                                        });
                                                                    }


                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });

                            }
                        }
                    }
                });
    }

    public static void getProductUvProtectionFromCollection(ArrayList<Product_Thumbnail> listThumbnail, Product_ThumbnailAdapter thumbnailAdapter) {
        String temp = Product_Thumbnail.COLECTION_NAME_COLLECTIONS + '/' + "UV PROTECTION" + '/' + Product_Thumbnail.COLECTION_NAME_PRODUCTS;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(temp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //check available
                                document.getReference().collection(Stock.COLLECTION_NAME)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int stock = 0;
                                                    for (QueryDocumentSnapshot stock_Ref : task.getResult()) {
                                                        stock += (int) Math.round(stock_Ref.getDouble("quantity"));
                                                        if (task.getResult().getDocuments().indexOf(stock_Ref) == task.getResult().size() - 1) {
                                                            if (stock != 0) {
                                                                //get data
                                                                Map<String, Object> map = document.getData();
                                                                Iterator iterator = map.keySet().iterator();
                                                                while (iterator.hasNext()) {
                                                                    String key = (String) iterator.next();

                                                                    if (key.equals("product_id")) {
                                                                        DocumentReference documentReference = (DocumentReference) map.get(key);
                                                                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                Product_Thumbnail thumbnail = new Product_Thumbnail();
                                                                                listThumbnail.add(thumbnail);

                                                                                thumbnail.setId(documentSnapshot.getId());

                                                                                thumbnail.setName(documentSnapshot.getString("name"));
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                thumbnail.setPrice(documentSnapshot.getDouble("price"));
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                int discount = (int) Math.round(documentSnapshot.getDouble("discount"));
                                                                                thumbnail.setDiscount(discount);
                                                                                thumbnailAdapter.notifyDataSetChanged();

                                                                                thumbnail.setMainImage(documentSnapshot.getString("main_img"));
                                                                                thumbnailAdapter.notifyDataSetChanged();


                                                                                DocumentReference docRef = (DocumentReference) documentSnapshot.get("category");
                                                                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                        thumbnail.setCategory(documentSnapshot.getString("name"));
                                                                                        thumbnailAdapter.notifyDataSetChanged();
                                                                                    }
                                                                                });

                                                                                FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
                                                                                DocumentReference doRef = db.document(Product_Thumbnail.COLLECTION_NAME + '/' + documentSnapshot.getId());

                                                                                CollectionReference coRef_wishList = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);
                                                                                Query query = coRef_wishList.whereEqualTo("product_id", doRef);
                                                                                query.limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            thumbnail.setFavorite(!task.getResult().isEmpty());
                                                                                            thumbnailAdapter.notifyDataSetChanged();
                                                                                        }
                                                                                    }
                                                                                });

                                                                            }
                                                                        });
                                                                    }


                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });

                            }
                        }
                    }
                });
    }

    public static void getProductSalesAndPushToGridView(ArrayList<Product_Thumbnail> listProduct, Product_ThumbnailAdapter thumbnailAdapter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Product_Thumbnail.COLECTION_NAME_SALES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Map<String, Object> tempObject = documentSnapshot.getData();

                                DocumentReference docRef = (DocumentReference) tempObject.get("product_id");

                                //check available
                                docRef.collection(Stock.COLLECTION_NAME)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int stock = 0;
                                                    for (QueryDocumentSnapshot stock_Ref : task.getResult()) {
                                                        stock += (int) Math.round(stock_Ref.getDouble("quantity"));
                                                        if (task.getResult().getDocuments().indexOf(stock_Ref) == task.getResult().size() - 1) {
                                                            if (stock != 0) {
                                                                //get data
                                                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot document) {
                                                                        Product_Thumbnail thumbnail = new Product_Thumbnail();
                                                                        //Thêm thumbnail vào arraylist và notifyDataSetChanged
                                                                        listProduct.add(thumbnail);

                                                                        //tempOject chứa product
                                                                        Map<String, Object> tempObject = document.getData();

                                                                        // Lặp qua từng field của một document
                                                                        Iterator myVeryOwnIterator = tempObject.keySet().iterator();

                                                                        while (myVeryOwnIterator.hasNext()) {
                                                                            String key = (String) myVeryOwnIterator.next();

                                                                            //Set id
                                                                            thumbnail.setId(document.getId());

                                                                            //set category
                                                                            if (key.equals("category")) {
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
                                                                            if (key.equals("name")) {
                                                                                thumbnail.setName((String) tempObject.get(key));
                                                                                thumbnailAdapter.notifyDataSetChanged();
                                                                            }

                                                                            //Set price
                                                                            if (key.equals("price")) {
                                                                                Double price = document.getDouble(key);
                                                                                thumbnail.setPrice(price);
                                                                                thumbnailAdapter.notifyDataSetChanged();
                                                                            }
                                                                            //set discount
                                                                            if (key.equals("discount")) {
                                                                                int discount = (int) Math.round(document.getDouble(key));
                                                                                thumbnail.setDiscount(discount);
                                                                                thumbnailAdapter.notifyDataSetChanged();
                                                                            }
                                                                            //set mainImage
                                                                            if (key.equals("main_img")) {
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
                                                                                    if (task.isSuccessful()) {
                                                                                        thumbnail.setFavorite(!task.getResult().isEmpty());
                                                                                        thumbnailAdapter.notifyDataSetChanged();
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                        }

                    }
                });
    }

    public static void getProductArrivalAndPushToGridView(ArrayList<Product_Thumbnail> listProduct, Product_ThumbnailAdapter thumbnailAdapter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Product_Thumbnail.COLECTION_NAME_ARRIVAL)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Map<String, Object> tempObject = documentSnapshot.getData();

                                DocumentReference docRef = (DocumentReference) tempObject.get("product_id");

                                //check available
                                docRef.collection(Stock.COLLECTION_NAME)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int stock = 0;
                                                    for (QueryDocumentSnapshot stock_Ref : task.getResult()) {
                                                        stock += (int) Math.round(stock_Ref.getDouble("quantity"));
                                                        if (task.getResult().getDocuments().indexOf(stock_Ref) == task.getResult().size() - 1) {
                                                            if (stock != 0) {
                                                                //get data
                                                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot document) {
                                                                        Product_Thumbnail thumbnail = new Product_Thumbnail();
                                                                        //Thêm thumbnail vào arraylist và notifyDataSetChanged
                                                                        listProduct.add(thumbnail);

                                                                        //tempOject chứa product
                                                                        Map<String, Object> tempObject = document.getData();

                                                                        // Lặp qua từng field của một document
                                                                        Iterator myVeryOwnIterator = tempObject.keySet().iterator();
                                                                        while (myVeryOwnIterator.hasNext()) {
                                                                            String key = (String) myVeryOwnIterator.next();

                                                                            //Set id
                                                                            thumbnail.setId(document.getId());

                                                                            //set category
                                                                            if (key.equals("category")) {
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
                                                                            if (key.equals("name")) {
                                                                                thumbnail.setName((String) tempObject.get(key));
                                                                                thumbnailAdapter.notifyDataSetChanged();
                                                                            }

                                                                            //Set price
                                                                            if (key.equals("price")) {
                                                                                Double price = document.getDouble(key);
                                                                                thumbnail.setPrice(price);
                                                                                thumbnailAdapter.notifyDataSetChanged();
                                                                            }
                                                                            //set discount
                                                                            if (key.equals("discount")) {
                                                                                int discount = (int) Math.round(document.getDouble(key));
                                                                                thumbnail.setDiscount(discount);
                                                                                thumbnailAdapter.notifyDataSetChanged();
                                                                            }
                                                                            //set mainImage
                                                                            if (key.equals("main_img")) {
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
                                                                                    if (task.isSuccessful()) {
                                                                                        thumbnail.setFavorite(!task.getResult().isEmpty());
                                                                                        thumbnailAdapter.notifyDataSetChanged();
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                        }

                    }
                });
    }

    public static void getProductAndPushToGridView(ArrayList<Product_Thumbnail> listProduct, Product_ThumbnailAdapter thumbnailAdapter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //get data
        db.collection(Product_Thumbnail.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task_product) {
                        if (task_product.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task_product.getResult()) {
                                //check available
                                document.getReference().collection(Stock.COLLECTION_NAME)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int stock = 0;
                                                    for (QueryDocumentSnapshot stock_Ref : task.getResult()) {
                                                        stock += (int) Math.round(stock_Ref.getDouble("quantity"));
                                                        if (task.getResult().getDocuments().indexOf(stock_Ref) == task.getResult().size() - 1) {
                                                            if (stock == 0) {
                                                                if (task_product.getResult().getDocuments().indexOf(document) == task_product.getResult().getDocuments().size() - 1) {
                                                                    SearchProductsFragment.originProductList.addAll(listProduct);
                                                                    SearchProductsFragment.dialog.dismiss();
                                                                }
                                                            } else {
                                                                //get data
                                                                Product_Thumbnail thumbnail = new Product_Thumbnail();

                                                                FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();

                                                                DocumentReference category_Ref = document.getDocumentReference("category");
                                                                DocumentReference product_Ref = db.document(Product_Thumbnail.COLLECTION_NAME + '/' + document.getId());
                                                                CollectionReference wishList_Ref = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);
                                                                CollectionReference color_Ref = product_Ref.collection("colors");
                                                                CollectionReference size_Ref = product_Ref.collection("sizes");

                                                                Task getCategory = category_Ref.get();
                                                                Task<QuerySnapshot> getFavorite = wishList_Ref.whereEqualTo("product_id", product_Ref).limit(1).get();
                                                                Task<QuerySnapshot> getColor = color_Ref.get();
                                                                Task<QuerySnapshot> getSize = size_Ref.get();

                                                                Task<List<QuerySnapshot>> listTask = Tasks.whenAllSuccess(getFavorite, getColor, getSize);
                                                                listTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                                                                    @Override
                                                                    public void onSuccess(List<QuerySnapshot> querySnapshots) {
                                                                        ArrayList<String> colorTemps = new ArrayList<>();
                                                                        ArrayList<String> sizeTemps = new ArrayList<>();

                                                                        //set favorite
                                                                        thumbnail.setFavorite(!querySnapshots.get(0).getDocuments().isEmpty());

                                                                        //set colorList
                                                                        for (DocumentSnapshot color_Doc : querySnapshots.get(1).getDocuments()) {
                                                                            colorTemps.add(color_Doc.getString("name"));
                                                                        }
                                                                        thumbnail.setColorList(colorTemps);

                                                                        //set sizeList
                                                                        for (DocumentSnapshot size_Doc : querySnapshots.get(2).getDocuments()) {
                                                                            sizeTemps.add(size_Doc.getString("short_name"));
                                                                        }
                                                                        thumbnail.setSizeList(sizeTemps);

                                                                        //set category
                                                                        getCategory.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                thumbnail.setCategory(documentSnapshot.getString("name"));

                                                                                //set other fields
                                                                                thumbnail.setId(document.getId());
                                                                                thumbnail.setName(document.getString("name"));
                                                                                thumbnail.setPrice(document.getDouble("price"));
                                                                                thumbnail.setDiscount((int) Math.round(document.getDouble("discount")));
                                                                                thumbnail.setMainImage(document.getString("main_img"));

                                                                                if (FilterFragment.checkFilter(thumbnail)) {
                                                                                    listProduct.add(thumbnail);
                                                                                    thumbnailAdapter.notifyDataSetChanged();
                                                                                }

                                                                                if (task_product.getResult().getDocuments().indexOf(document) == task_product.getResult().getDocuments().size() - 1 && SearchProductsFragment.dialog.isShowing()) {
                                                                                    SearchProductsFragment.originProductList.addAll(listProduct);
                                                                                    SearchProductsFragment.dialog.dismiss();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }


                                                }
                                            }
                                        });


                            }
                        }
                    }
                });
    }

    public static void getProductsWithSameCategory(ArrayList<Product_Thumbnail> listProduct, Product_ThumbnailAdapter thumbnailAdapter, String category) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CategoryItem.COLLECTION_NAME)
                .whereEqualTo("name", category)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot category_Doc : task.getResult()) {
                                DocumentReference category_Ref = category_Doc.getReference();

                                //get data
                                db.collection(Product_Thumbnail.COLLECTION_NAME)
                                        .whereEqualTo("category", category_Ref)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task_product) {
                                                if (task_product.isSuccessful()) {

                                                    for (QueryDocumentSnapshot document : task_product.getResult()) {

                                                        document.getReference().collection(Stock.COLLECTION_NAME)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            int stock = 0;
                                                                            for (QueryDocumentSnapshot stock_Ref : task.getResult()) {
                                                                                stock += (int) Math.round(stock_Ref.getDouble("quantity"));
                                                                                if (task.getResult().getDocuments().indexOf(stock_Ref) == task.getResult().size() - 1) {
                                                                                    if (stock == 0) {
                                                                                        if (task_product.getResult().getDocuments().indexOf(document) == task_product.getResult().getDocuments().size() - 1) {
                                                                                            SearchProductsFragment.originProductList.addAll(listProduct);
                                                                                            SearchProductsFragment.dialog.dismiss();
                                                                                        }
                                                                                    } else {
                                                                                        Product_Thumbnail thumbnail = new Product_Thumbnail();

                                                                                        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();

                                                                                        DocumentReference product_Ref = document.getReference();
                                                                                        CollectionReference wishList_Ref = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);
                                                                                        CollectionReference color_Ref = product_Ref.collection("colors");
                                                                                        CollectionReference size_Ref = product_Ref.collection("sizes");

                                                                                        Task<QuerySnapshot> getFavorite = wishList_Ref.whereEqualTo("product_id", product_Ref).limit(1).get();
                                                                                        Task<QuerySnapshot> getColor = color_Ref.get();
                                                                                        Task<QuerySnapshot> getSize = size_Ref.get();

                                                                                        Task<List<QuerySnapshot>> listTask = Tasks.whenAllSuccess(getFavorite, getColor, getSize);
                                                                                        listTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                                                                                            @Override
                                                                                            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                                                                                                ArrayList<String> colorTemps = new ArrayList<>();
                                                                                                ArrayList<String> sizeTemps = new ArrayList<>();

                                                                                                //set favorite
                                                                                                thumbnail.setFavorite(!querySnapshots.get(0).getDocuments().isEmpty());

                                                                                                //set colorList
                                                                                                for (DocumentSnapshot color_Doc : querySnapshots.get(1).getDocuments()) {
                                                                                                    colorTemps.add(color_Doc.getString("name"));
                                                                                                }
                                                                                                thumbnail.setColorList(colorTemps);

                                                                                                //set sizeList
                                                                                                for (DocumentSnapshot size_Doc : querySnapshots.get(2).getDocuments()) {
                                                                                                    sizeTemps.add(size_Doc.getString("short_name"));
                                                                                                }
                                                                                                thumbnail.setSizeList(sizeTemps);

                                                                                                //set other fields
                                                                                                thumbnail.setCategory(category_Doc.getString("name"));
                                                                                                thumbnail.setId(document.getId());
                                                                                                thumbnail.setName(document.getString("name"));
                                                                                                thumbnail.setPrice(document.getDouble("price"));
                                                                                                thumbnail.setDiscount((int) Math.round(document.getDouble("discount")));
                                                                                                thumbnail.setMainImage(document.getString("main_img"));

                                                                                                if (FilterFragment.checkFilter(thumbnail)) {
                                                                                                    listProduct.add(thumbnail);
                                                                                                    thumbnailAdapter.notifyDataSetChanged();
                                                                                                }

                                                                                                if (task_product.getResult().getDocuments().indexOf(document) == task_product.getResult().getDocuments().size() - 1 && SearchProductsFragment.dialog.isShowing()) {
                                                                                                    SearchProductsFragment.originProductList.addAll(listProduct);
                                                                                                    SearchProductsFragment.dialog.dismiss();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                });


                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}
