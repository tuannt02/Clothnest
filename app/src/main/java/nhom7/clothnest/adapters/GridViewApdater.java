package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nhom7.clothnest.models.Product;
import nhom7.clothnest.R;

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

        TextView productName = (TextView) view.findViewById(R.id.productName);
        ImageView productImage = (ImageView) view.findViewById(R.id.productImage);
        TextView regularCost = (TextView) view.findViewById(R.id.regularCost);
        TextView discount = (TextView) view.findViewById(R.id.discount);


        productName.setText(productList.get(i).productName);
        productImage.setImageResource(productList.get(i).productImage);
        regularCost.setText(productList.get(i).regularCost);
        discount.setText(productList.get(i).discount);

        return view;
    }
}
