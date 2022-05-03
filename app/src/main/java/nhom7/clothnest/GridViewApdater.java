package nhom7.clothnest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GridViewApdater extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Product> productList;

    public GridViewApdater(Context context, int layout, List<Product> productList) {
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
        view = layoutInflater.inflate(R.layout.thumbnail,null);

        TextView tvProductName = (TextView) view.findViewById(R.id.productName);
        ImageView tvProductImage = (ImageView) view.findViewById(R.id.productImage);
        TextView tvRegularCost = (TextView) view.findViewById(R.id.regularCost);
        TextView tvDiscount = (TextView) view.findViewById(R.id.discount);
        TextView tvDiscountCost = (TextView) view.findViewById(R.id.discountCost);


        tvProductName.setText(productList.get(i).getProductName());
        tvProductImage.setImageResource(productList.get(i).getProductImage());

        Double discount = Double.parseDouble(String.valueOf(productList.get(i).getDiscount()));
        tvDiscount.setText("-" + discount.toString().replaceAll("\\.?[0-9]*$", "") + "%");

        Double price = Double.parseDouble(String.valueOf(productList.get(i).getRegularCost()));
        tvRegularCost.setText("$" + price.toString().replaceAll("\\.?[0-9]*$", ""));

        Double discountPrice = price * (1 - discount/100.0);
        tvDiscountCost.setText("$" + discountPrice.toString().replaceAll("\\.?[0-9]*$", ""));

        return view;
    }
}
