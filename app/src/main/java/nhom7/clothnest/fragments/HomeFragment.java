package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import nhom7.clothnest.activities.CartActivity;
import nhom7.clothnest.activities.ProductDetail_Activity;
import nhom7.clothnest.activities.SeeAllItemActivity;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.ProductSlider;
import nhom7.clothnest.util.customizeComponent.CustomDialog;


public class HomeFragment extends Fragment {
    GridView gridViewArrival,gridViewSales;
    ArrayList<Product1> productArrayList;
    GridViewApdater gridViewApdaterArrival,gridViewApdaterSales;
    ProductSlider productSlider;
    LinearLayout containersilder;
    View includeView;
    ImageView buttoncart;
    Button btnSeeAllItem,btnSeeAllItemSales,btnWinter;
    ViewFlipper viewFlipper;
    Animation in,out,alpha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        gridViewArrival = v.findViewById(R.id.gridview_GroupThumbnail);
        includeView = v.findViewById(R.id.group_sales);
        gridViewSales = includeView.findViewById(R.id.gridview_GroupThumbnail);
        containersilder=v.findViewById(R.id.container_slider);
        buttoncart = v.findViewById(R.id.btncart);
        btnSeeAllItem= v.findViewById(R.id.btnarrival);
        btnSeeAllItemSales=v.findViewById(R.id.btnsale);
        btnWinter=v.findViewById(R.id.btnWinter);
        viewFlipper = v.findViewById(R.id.viewFlipper);

        AnimatonViewFliper();
        GetProduct();


        productSlider = new ProductSlider(getContext(),containersilder,productArrayList);
        productSlider.createProductSlider();

        buttoncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                Toast toast = new Toast(getContext());
                LayoutInflater inflater1= getLayoutInflater();
                View view1 = inflater1.inflate(R.layout.layout_custom_toast,(ViewGroup) v.findViewById(R.id.layout_custom_toast));
                TextView textView= view1.findViewById(R.id.tv_message);
                textView.setText("Thanh Cong");
                toast.setView(view1);
                toast.setGravity(Gravity.BOTTOM,0,0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                //buttonOpenDialogClicked();
                startActivity(intent);
            }
        });

//        gridViewArrival.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                alpha= AnimationUtils.loadAnimation(getContext(),R.anim.alpha_anim);
//                view.startAnimation(alpha);
//               // startActivity(new Intent(getContext(), ProductDetail_Activity.class));
//
//
//
//            }
//        });

        btnWinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProductDetail_Activity.class));

            }
        });

        btnSeeAllItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a= "NEW ARRIVALS";
                Intent intent = new Intent(getContext(), SeeAllItemActivity.class);
                intent.putExtra("name",a);
                startActivity(intent);
            }
        });

        btnSeeAllItemSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a= "SALES";
                Intent intent = new Intent(getContext(), SeeAllItemActivity.class);
                intent.putExtra("name",a);
                startActivity(intent);
            }
        });
        return v;
    }

    private void buttonOpenDialogClicked() {
        CustomDialog.Listener listener = new CustomDialog.Listener() {
            @Override
            public void ListenerEntered() {
            }
        };
        final CustomDialog dialog = new CustomDialog(getContext(),listener);
        dialog.show();
    }
    private void AnimatonViewFliper() {
        in=AnimationUtils.loadAnimation(getContext(),R.anim.fade_in_anim);
        out=AnimationUtils.loadAnimation(getContext(),R.anim.fade_out_anim);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
        viewFlipper.setFlipInterval(1500);
        viewFlipper.setAutoStart(true);
    }
    public void GetProduct(){
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("2", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("3", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("4", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("5", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("6", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));
        productArrayList.add(new Product1("1", "Oversize Hoodie", R.drawable.productimage, 307, 24));

        gridViewApdaterArrival = new GridViewApdater(getContext(), R.layout.thumbnail, productArrayList);
        gridViewArrival.setAdapter(gridViewApdaterArrival);
        gridViewApdaterSales = new GridViewApdater(getContext(), R.layout.thumbnail,productArrayList);
        gridViewSales.setAdapter(gridViewApdaterSales);
    }
}