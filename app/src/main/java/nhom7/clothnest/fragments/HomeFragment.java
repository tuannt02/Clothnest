package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nhom7.clothnest.activities.CartActivity;
import nhom7.clothnest.activities.SeeAllitemActivity;
import nhom7.clothnest.adapters.GridViewApdater;
import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;
import nhom7.clothnest.models.ProductSlider;


public class HomeFragment extends Fragment {
    GridView gridViewArrival,gridViewSales;
    ArrayList<Product> productArrayList;
    GridViewApdater gridViewApdaterArrival,gridViewApdaterSales;
    ProductSlider productSlider;
    LinearLayout containersilder;
    View includeView;
    ImageView buttoncart;
    Button btnSeeAllItem,btnSeeAllItemSales;
    ViewFlipper viewFlipper;
    Animation in,out;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        gridViewArrival = v.findViewById(R.id.gridview);
        includeView = v.findViewById(R.id.group_sales);
        gridViewSales = includeView.findViewById(R.id.gridview);
        containersilder=v.findViewById(R.id.container_slider);
        buttoncart = v.findViewById(R.id.btncart);
        btnSeeAllItem= v.findViewById(R.id.btnarrival);
        btnSeeAllItemSales=v.findViewById(R.id.btnsale);
        viewFlipper = v.findViewById(R.id.viewFlipper);
        AnimatonViewFliper();
        GetProduct();
        productSlider = new ProductSlider(getContext(),containersilder,productArrayList);
        productSlider.createProductSlider();
        buttoncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });
        btnSeeAllItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SeeAllitemActivity.class);
                startActivity(intent);
            }
        });
//        btnSeeAllItemSales.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), SeeAllitemActivity.class);
//                startActivity(intent);
//            }
//        });
        return v;
    }
    private void AnimatonViewFliper() {
        in=AnimationUtils.loadAnimation(getContext(),R.anim.fade_in_anim);
        out=AnimationUtils.loadAnimation(getContext(),R.anim.fade_out_anim);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(true);
    }

    public void GetProduct(){
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        productArrayList.add(new Product("Oversize Hoodie", R.drawable.productimage, "307", "24"));
        gridViewApdaterArrival = new GridViewApdater(getContext(), R.layout.thumbnail, productArrayList);
        gridViewArrival.setAdapter(gridViewApdaterArrival);
        gridViewApdaterSales = new GridViewApdater(getContext(), R.layout.thumbnail,productArrayList);
        gridViewSales.setAdapter(gridViewApdaterSales);
    }
}