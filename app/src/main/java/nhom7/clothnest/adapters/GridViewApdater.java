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

import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.ProductDetail_Activity;
import nhom7.clothnest.models.Product1;

public class GridViewApdater extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Product1> productList;

    //Change screen
    private View mView;
    private Animation alpha;

    //reference
    private TextView tvProductName;
    private ImageView ivProductImage;
    private TextView tvRegularCost;
    private TextView tvDiscount;
    private TextView tvDiscountCost;

    public GridViewApdater(Context context, int layout, List<Product1> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = view = layoutInflater.inflate(R.layout.thumbnail, null);

        reference();
        getData(i);
        setEventsClick(i);

        return view;
    }

    private void getData(int i) {
        tvProductName.setText(productList.get(i).getProductName());
        ivProductImage.setImageResource(productList.get(i).getProductImage());

        Double discount = Double.parseDouble(String.valueOf(productList.get(i).getDiscount()));
        tvDiscount.setText("-" + discount.toString().replaceAll("\\.?[0-9]*$", "") + "%");

        Double price = Double.parseDouble(String.valueOf(productList.get(i).getRegularCost()));
        tvRegularCost.setText("$" + price.toString().replaceAll("\\.?[0-9]*$", ""));

        Double discountPrice = price * (1 - discount / 100.0);
        tvDiscountCost.setText("$" + discountPrice.toString().replaceAll("\\.?[0-9]*$", ""));
    }

    private void reference() {
        tvProductName = (TextView) mView.findViewById(R.id.productName);
        ivProductImage = (ImageView) mView.findViewById(R.id.productImage);
        tvRegularCost = (TextView) mView.findViewById(R.id.regularCost);
        tvDiscount = (TextView) mView.findViewById(R.id.discount);
        tvDiscountCost = (TextView) mView.findViewById(R.id.discountCost);
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
        Intent intent_productDetail = new Intent(context, ProductDetail_Activity.class);
        intent_productDetail.putExtra("key", productList.get(i).getId());
        alpha = AnimationUtils.loadAnimation(context, R.anim.alpha_anim);
        mView.startAnimation(alpha);
        context.startActivity(intent_productDetail);
    }

}
