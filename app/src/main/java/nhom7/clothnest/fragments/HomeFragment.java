package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.Admin_ChatActivity;
import nhom7.clothnest.activities.CartActivity;
import nhom7.clothnest.activities.SeeAllItemActivity;
import nhom7.clothnest.adapters.Product_ThumbnailAdapter;
import nhom7.clothnest.models.ProductSlider;
import nhom7.clothnest.models.Product_Thumbnail;


public class HomeFragment extends Fragment {
    View mView;
    GridView gridViewArrival, gridViewSales;
    ProductSlider productSlider;
    LinearLayout containersilder;
    View includeView;
    ImageView buttoncart, btnChat;
    Button btnSeeAllItem, btnSeeAllItemSales, btnWinter;
    ViewFlipper viewFlipper;
    Animation in, out, alpha;

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

        getProductThumbnail();

        getEvent();
        createSlider();

        initTuanBeVars();

        return mView;
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
    }

    private void createSlider() {
        productSlider = new ProductSlider(getContext(), containersilder, collectionsList);
        productSlider.createProductSlider();
        productSlider.getSimilarProduct(collectionsList, "T-Shirts");
    }

    private void getEvent() {
        buttoncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                Toast toast = new Toast(getContext());
                LayoutInflater inflater1 = getLayoutInflater();
                View view1 = inflater1.inflate(R.layout.layout_custom_toast, (ViewGroup) mView.findViewById(R.id.layout_custom_toast));
                TextView textView = view1.findViewById(R.id.tv_message);
                textView.setText("Thanh Cong");
                toast.setView(view1);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                //buttonOpenDialogClicked();
                startActivity(intent);
            }
        });


        btnWinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        viewFlipper = mView.findViewById(R.id.viewFlipper);
        btnChat = mView.findViewById(R.id.btnchat);

        collectionsList = new ArrayList<>();
    }


    private void AnimatonViewFliper() {
        in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_anim);
        out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
        viewFlipper.setFlipInterval(1500);
        viewFlipper.setAutoStart(true);
    }

    public void getProductThumbnail() {
        //Thêm sản phẩm vào arrivals
        arrivalsList = new ArrayList<>();
        arrivalsAdapter = new Product_ThumbnailAdapter(getContext(), arrivalsList);
        gridViewArrival.setAdapter(arrivalsAdapter);
        Product_ThumbnailAdapter.getProductArrivalAndPushToGridView(arrivalsList, arrivalsAdapter);

        //Thêm sản phẩm vào sales
        salesList = new ArrayList<>();
        salesAdapter = new Product_ThumbnailAdapter(getContext(), arrivalsList);
        gridViewSales.setAdapter(salesAdapter);
        Product_ThumbnailAdapter.getProductSalesAndPushToGridView(salesList, salesAdapter);
    }
}