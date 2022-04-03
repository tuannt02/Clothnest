package nhom7.clothnest;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomPurchaseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Purchase> purchases;
    private LayoutInflater layoutInflater;

    public CustomPurchaseAdapter(Context context, ArrayList<Purchase> purchases) {
        this.context = context;
        this.purchases = purchases;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return purchases.size();
    }

    @Override
    public Object getItem(int i) {
        return purchases.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout list = (LinearLayout) layoutInflater.inflate(R.layout.purchases_list, viewGroup, false);
        purchases.get(i).calcTotal();

        for (PurchaseItem purchaseItem : purchases.get(i).getItems()) {

            View item = layoutInflater.inflate(R.layout.purchases_item, null);

            ((ImageView) item.findViewById(R.id.imageview_purchase)).setImageResource(R.drawable.sample);
            ((TextView) item.findViewById(R.id.purchases_item_title)).setText(purchaseItem.getTitle());
            ((TextView) item.findViewById(R.id.purchases_item_date)).setText("Purchase date: " + purchaseItem.getPurchaseDate());
            ((TextView) item.findViewById(R.id.purchases_item_color)).setText("Color: " + purchaseItem.getColor());
            ((TextView) item.findViewById(R.id.purchases_item_size)).setText("Size: " + purchaseItem.getSize());
            ((TextView) item.findViewById(R.id.purchases_item_price)).setText("Price: $" + purchaseItem.getPrice().toString().replaceAll("\\.?0*$", ""));
            ((TextView) item.findViewById(R.id.purchases_item_qty)).setText("Qty: " + purchaseItem.getQty());

            list.addView(item);
        }

        RelativeLayout totalLayout = (RelativeLayout) layoutInflater.inflate(R.layout.purchases_total, null);
        ((TextView) totalLayout.findViewById(R.id.textview_totalPurchases)).setText("$" + purchases.get(i).getTotal().toString().replaceAll("\\.?0*$", ""));

        list.addView(totalLayout);

        return list;
    }
}
