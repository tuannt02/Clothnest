package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import nhom7.clothnest.activities.CartActivity;
import nhom7.clothnest.activities.ProductDetail_Activity;
import nhom7.clothnest.activities.SeeAllItemActivity;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.adapters.ProductSliderAdapter;
import nhom7.clothnest.adapters.Product_ThumbnailAdapter;
import nhom7.clothnest.models.CategoryItem;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.ProductSlider;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;
import nhom7.clothnest.util.customizeComponent.CustomDialog;


public class HomeFragment extends Fragment {
    View mView;
    GridView gridViewArrival, gridViewSales;
    ProductSlider productSlider;
    LinearLayout containersilder;
    View includeView;
    ImageView buttoncart;
    Button btnSeeAllItem, btnSeeAllItemSales, btnWinter;
    ViewFlipper viewFlipper;
    Animation in, out, alpha;

    //Get product from FireStore
    ArrayList<Product_Thumbnail> arrivalsList, collectionsList;
    Product_ThumbnailAdapter arrivalsAdapter;

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

        return mView;
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
                startActivity(new Intent(getContext(), ProductDetail_Activity.class));

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

    private void getProductThumbnail(){
        //Thêm sản phẩm vào arrivals
        arrivalsList = new ArrayList<>();
        arrivalsAdapter = new Product_ThumbnailAdapter(getContext(), arrivalsList);
        gridViewArrival.setAdapter(arrivalsAdapter);

        Product_ThumbnailAdapter.getProductAndPushToGridView(arrivalsList, arrivalsAdapter);
    }
}