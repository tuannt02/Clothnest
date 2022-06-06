package nhom7.clothnest.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.Admin_ChatActivity;
import nhom7.clothnest.activities.CartActivity;
import nhom7.clothnest.activities.SeeAllItemActivity;
import nhom7.clothnest.adapters.ImageAdapter;
import nhom7.clothnest.adapters.Product_ThumbnailAdapter;
import nhom7.clothnest.models.Image;
import nhom7.clothnest.models.ProductSlider;
import nhom7.clothnest.models.Product_Thumbnail;


public class HomeFragment extends Fragment {
    View mView;
    GridView gridViewArrival, gridViewSales;
    ProductSlider productSlider;
    LinearLayout containersilder;
    View includeView;
    ImageView buttoncart, btnChat;
    Button btnSeeAllItem, btnSeeAllItemSales, btnWinter, btnLine, btnUT, btnUnisex, btnUvProtection;
    ViewPager2 viewPager2;
    ArrayList<Image> arrayList;
    ImageAdapter imageAdapter;
    Handler handler = new Handler();

    //Get product from FireStore
    ArrayList<Product_Thumbnail> arrivalsList, salesList, collectionsList;
    Product_ThumbnailAdapter arrivalsAdapter, salesAdapter;

    // Tuan be
    FirebaseFirestore tuanDb = FirebaseFirestore.getInstance();
    String currUserUid;
    DocumentReference currUser;
    DocumentSnapshot chatRoom;
    CollectionReference chatListRef;
    private ListenerRegistration chatRoomListener;
    private static final String TAG = "HomeFragment";
    private CardView cvUnreadMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        reference();
        AnimatonViewFliper();
        slider();
        getProductThumbnail();
        getEvent();
        createSlider("WINTER");
        initTuanBeVars();


        return mView;
    }

    private void slider() {
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.8f + r * 0.14f);
            }
        });

        viewPager2.setPageTransformer(transformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 1500);
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }


    private void AnimatonViewFliper() {
        arrayList = new ArrayList<>();
        imageAdapter = new ImageAdapter(arrayList, viewPager2, getContext());
        viewPager2.setAdapter(imageAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Image.COLLECTION_BANNERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Image image = new Image();
                                arrayList.add(image);

                                Map<String, Object> map = documentSnapshot.getData();
                                Iterator iterator = map.keySet().iterator();

                                while (iterator.hasNext()) {
                                    String key = (String) iterator.next();
                                    if (key.equals("img")) {
                                        image.setImage((String) map.get(key));
                                        imageAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        chatRoomListener = chatListRef.whereEqualTo("userRef", currUser)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, "Error: " + error.toString());
                        }

                        if (value.size() > 0) {
                            chatRoom = value.getDocuments().get(0);
                            if (!chatRoom.getBoolean("is_client_read")) {
                                cvUnreadMessage.setVisibility(View.VISIBLE);
                            } else {
                                cvUnreadMessage.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        chatRoomListener.remove();
    }

    private void initTuanBeVars() {
        currUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currUser = tuanDb.collection("users").document(currUserUid);
        chatListRef = tuanDb.collection("chat");
        cvUnreadMessage = mView.findViewById(R.id.cardview_unreadMessage);
    }

    @Override
    public void onResume() {
        super.onResume();
        getProductThumbnail();
        handler.postDelayed(runnable, 1500);

    }

    private void createSlider(String collectionName) {
        View view1 = containersilder.getChildAt(0);
        containersilder.removeView(view1);
        collectionsList = new ArrayList<>();
        productSlider = new ProductSlider(getContext(), containersilder, collectionsList);
        productSlider.createProductSlider();
        productSlider.getCollectionProduct(collectionsList, collectionName);
        btnWinter.setBackgroundColor(Color.parseColor("#DF7861"));
    }

    private void getEvent() {
        buttoncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        btnWinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = containersilder.getChildAt(0);
                containersilder.removeView(view1);
                createSlider("WINTER");

                btnWinter.setBackgroundColor(Color.parseColor("#DF7861"));
                btnUT.setBackgroundColor(Color.WHITE);
                btnUvProtection.setBackgroundColor(Color.WHITE);
                btnUnisex.setBackgroundColor(Color.WHITE);
                btnLine.setBackgroundColor(Color.WHITE);
            }
        });

        btnLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View view1 = containersilder.getChildAt(0);
                containersilder.removeView(view1);
                createSlider("LINE");
                btnUT.setBackgroundColor(Color.WHITE);
                btnWinter.setBackgroundColor(Color.WHITE);
                btnUvProtection.setBackgroundColor(Color.WHITE);
                btnUnisex.setBackgroundColor(Color.WHITE);
                btnLine.setBackgroundColor(Color.parseColor("#DF7861"));
            }
        });

        btnUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                containersilder.removeViewAt(0);
                createSlider("UT");
                btnLine.setBackgroundColor(Color.WHITE);
                btnWinter.setBackgroundColor(Color.WHITE);
                btnUvProtection.setBackgroundColor(Color.WHITE);
                btnUnisex.setBackgroundColor(Color.WHITE);
                btnUT.setBackgroundColor(Color.parseColor("#DF7861"));
            }
        });


        btnUnisex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containersilder.removeViewAt(0);
                createSlider("UNISEX");
                btnLine.setBackgroundColor(Color.WHITE);
                btnUT.setBackgroundColor(Color.WHITE);
                btnWinter.setBackgroundColor(Color.WHITE);

                btnUvProtection.setBackgroundColor(Color.WHITE);
                btnUnisex.setBackgroundColor(Color.parseColor("#DF7861"));
            }
        });
        btnUvProtection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containersilder.removeViewAt(0);

                createSlider("UV PROTECTION");
                btnLine.setBackgroundColor(Color.WHITE);
                btnUT.setBackgroundColor(Color.WHITE);
                btnWinter.setBackgroundColor(Color.WHITE);
                btnUnisex.setBackgroundColor(Color.WHITE);
                btnUvProtection.setBackgroundColor(Color.parseColor("#DF7861"));
            }
        });


        btnSeeAllItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = "NEW ARRIVALS";
                Intent intent = new Intent(getContext(), SeeAllItemActivity.class);
                intent.putExtra("name", a);
                startActivity(intent);
            }
        });

        btnSeeAllItemSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = "SALES";
                Intent intent = new Intent(getContext(), SeeAllItemActivity.class);
                intent.putExtra("name", a);
                startActivity(intent);
            }
        });


        // Open chat activity
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Admin_ChatActivity.class);
                intent.putExtra("isAdminAccessed", false);
                if (chatRoom != null)
                    intent.putExtra("chatRoomID", chatRoom.getId());
                startActivity(intent);
            }
        });
    }

    private void reference() {
        gridViewArrival = mView.findViewById(R.id.gridview_GroupThumbnail);
        includeView = mView.findViewById(R.id.group_sales);
        gridViewSales = includeView.findViewById(R.id.gridview_GroupThumbnail);
        containersilder = mView.findViewById(R.id.container_slider);
        buttoncart = mView.findViewById(R.id.btncart);
        btnSeeAllItem = mView.findViewById(R.id.btnarrival);
        btnSeeAllItemSales = mView.findViewById(R.id.btnsale);
        btnWinter = mView.findViewById(R.id.btnWinter);
        btnLine = mView.findViewById(R.id.btnLine);
        btnChat = mView.findViewById(R.id.btnchat);
        collectionsList = new ArrayList<>();
        viewPager2 = mView.findViewById(R.id.viewPager);
        btnUT = mView.findViewById(R.id.btnUt);
        btnUnisex = mView.findViewById(R.id.btnUnisex);
        btnUvProtection = mView.findViewById(R.id.btnUvprotection);

    }


    public void getProductThumbnail() {
        //Thêm sản phẩm vào arrivals
        arrivalsList = new ArrayList<>();
        arrivalsAdapter = new Product_ThumbnailAdapter(getContext(), R.layout.thumbnail, arrivalsList);
        gridViewArrival.setAdapter(arrivalsAdapter);
        Product_ThumbnailAdapter.getProductArrivalAndPushToGridView(arrivalsList, arrivalsAdapter);

        //Thêm sản phẩm vào sales
        salesList = new ArrayList<>();
        salesAdapter = new Product_ThumbnailAdapter(getContext(), R.layout.thumbnail, salesList);
        gridViewSales.setAdapter(salesAdapter);
        Product_ThumbnailAdapter.getProductSalesAndPushToGridView(salesList, salesAdapter);

    }
}