package nhom7.clothnest.models;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import nhom7.clothnest.adapters.ProductSliderAdapter;
import nhom7.clothnest.models.Product;

public class ProductSlider {
    private RecyclerView recyclerView;
    private ArrayList<Product_Thumbnail> products;
    private int dotsNum;
    private final String htmlDot = "&#8226";
    private LinearLayout container;
    private LinearLayout linearLayout;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private Context mContext;
    private ProductSliderAdapter adapter;

    public ProductSlider(Context context, LinearLayout container, ArrayList<Product_Thumbnail> products) {
        mContext = context;
        this.container = container;
        this.products = products;
        dotsNum = products.size() / 2;
    }

    public void createProductSlider() {
        implementRecyclerView();
        createDotsIndicator();
    }

    private void implementRecyclerView() {
        // create new recycler view
        recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // add recycler view to container
        container.addView(recyclerView);

        // Initialize ProductSliderAdapter
        adapter = new ProductSliderAdapter(mContext, products);
        recyclerView.setAdapter(adapter);
    }

    private void createDotsIndicator() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        // create layout container
        linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setDividerPadding(4);
        linearLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

        // add dots to container
        for (int i = 0; i < dotsNum; i++) {
            TextView dot = new TextView(mContext);
            dot.setText(HtmlCompat.fromHtml(htmlDot, HtmlCompat.FROM_HTML_MODE_LEGACY));
            dot.setTextSize(40);
            dot.setTextColor(Color.parseColor("#B6B6B6"));
            linearLayout.addView(dot);
        }

        // add dots indicator to
        container.addView(linearLayout);

        // Change the indicator when user scroll the slider
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisibleIndex = linearLayoutManager.findLastVisibleItemPosition();
                    // Since 1 slide contains two products, we need to divide it before passing
                    changeDotsIndicator(lastVisibleIndex / 2);
                }
            });
        }
    }

    private void changeDotsIndicator(int index) {
        TextView prevDot = null, currDot, nextDot = null;

        // init dot
        if (index > 0)
            prevDot = (TextView) linearLayout.getChildAt(index - 1);

        if (index < dotsNum - 1)
            nextDot = (TextView) linearLayout.getChildAt(index + 1);

        currDot = (TextView) linearLayout.getChildAt(index);

        // change dot background
        if (prevDot != null) {
            prevDot.setTextColor(Color.parseColor("#B6B6B6"));
        }

        if (nextDot != null) {
            nextDot.setTextColor(Color.parseColor("#B6B6B6"));
        }

        if (currDot != null)
            currDot.setTextColor(ContextCompat.getColor(mContext, R.color.black));
    }


    public void getCollectionProduct(ArrayList<Product_Thumbnail> listProduct, String collectionName){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listProduct.clear();
        db.collection("collections").document(collectionName).collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                DocumentReference productRef = (DocumentReference) document.get("product_id");
                                productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Product_Thumbnail thumbnail = new Product_Thumbnail();
                                        listProduct.add(thumbnail);
                                        adapter.notifyDataSetChanged();

                                        //Set id
                                        thumbnail.setId(documentSnapshot.getId());
                                        adapter.notifyDataSetChanged();

                                        DocumentReference docRef = (DocumentReference) documentSnapshot.get("category");
                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                thumbnail.setCategory(documentSnapshot.getString("name"));
                                                adapter.notifyDataSetChanged();
                                            }
                                        });

                                        thumbnail.setName(documentSnapshot.getString("name"));
                                        adapter.notifyDataSetChanged();

                                        thumbnail.setPrice(documentSnapshot.getDouble("price"));
                                        adapter.notifyDataSetChanged();

                                        thumbnail.setDiscount((int)Math.round(documentSnapshot.getDouble("discount")));
                                        adapter.notifyDataSetChanged();

                                        thumbnail.setMainImage((documentSnapshot.getString("main_img")));
                                        adapter.notifyDataSetChanged();

                                        //set isFavorite
                                        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
                                        DocumentReference doRef = db.document(Product_Thumbnail.COLLECTION_NAME + '/' + documentSnapshot.getId());

                                        CollectionReference coRef_wishList = db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME);
                                        Query query = coRef_wishList.whereEqualTo("product_id", doRef);
                                        query.limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    thumbnail.setFavorite(!task.getResult().isEmpty());
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                });
    }

    //Lấy 10 sản phẩm cùng danh mục
    public void getSimilarProduct(ArrayList<Product_Thumbnail> listProduct, String category){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CategoryItem.COLLECTION_NAME)
                .whereEqualTo("name", category)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                DocumentReference doRef = db.document(CategoryItem.COLLECTION_NAME + '/' + document.getId());

                                //get data
                                db.collection(Product_Thumbnail.COLLECTION_NAME)
                                        .whereEqualTo("category", doRef)
                                        .limit(10)
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
                                                        adapter.notifyDataSetChanged();

                                                        //tempOject chứa product
                                                        Map<String, Object> tempObject = document.getData();

                                                        // Lặp qua từng field của một document
                                                        Iterator myVeryOwnIterator = tempObject.keySet().iterator();
                                                        while(myVeryOwnIterator.hasNext()){
                                                            String key = (String) myVeryOwnIterator.next();

                                                            //Set id
                                                            thumbnail.setId(document.getId());
                                                            adapter.notifyDataSetChanged();

                                                            //set category
                                                            if(key.equals("category")){
                                                                DocumentReference docRef = (DocumentReference) tempObject.get(key);
                                                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        thumbnail.setCategory(documentSnapshot.getString("name"));
                                                                        adapter.notifyDataSetChanged();
                                                                    }
                                                                });
                                                            }

                                                            //Set name
                                                            if(key.equals("name")){
                                                                thumbnail.setName((String) tempObject.get(key));
                                                                adapter.notifyDataSetChanged();
                                                            }

                                                            //Set price
                                                            if(key.equals("price")){
                                                                Double price = document.getDouble(key);
                                                                thumbnail.setPrice(price);
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                            //set discount
                                                            if(key.equals("discount")){
                                                                int discount = (int)Math.round(document.getDouble(key));
                                                                thumbnail.setDiscount(discount);
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                            //set mainImage
                                                            if(key.equals("main_img")){
                                                                thumbnail.setMainImage((String) tempObject.get(key));
                                                                adapter.notifyDataSetChanged();
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
                                                                        adapter.notifyDataSetChanged();
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
                    }
                });
    }
}
