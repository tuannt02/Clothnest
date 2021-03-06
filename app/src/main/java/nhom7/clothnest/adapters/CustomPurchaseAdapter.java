package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nhom7.clothnest.activities.ProductDetailActivity;
import nhom7.clothnest.models.Address;
import nhom7.clothnest.models.Purchase;
import nhom7.clothnest.models.PurchaseItem;
import nhom7.clothnest.R;
import nhom7.clothnest.util.RemoveTrailingZero;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;

public class CustomPurchaseAdapter extends BaseAdapter {
    private static final String TAG = "CustomPurchaseAdapter";
    private Context context;
    private ArrayList<Purchase> purchases;
    private LayoutInflater layoutInflater;

    private CustomOptionMenu customOptionMenu;
    private String currTransactionId;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd");
    private DecimalFormat df = new DecimalFormat("#.##");

    public CustomPurchaseAdapter(Context context, ArrayList<Purchase> purchases) {
        this.context = context;
        this.purchases = purchases;
        layoutInflater = LayoutInflater.from(context);
        df.setRoundingMode(RoundingMode.CEILING);
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

        if (purchases.get(i).getItems() != null) {
            for (PurchaseItem purchaseItem : purchases.get(i).getItems()) {

                View item = layoutInflater.inflate(R.layout.purchases_item, null);

                Picasso.get().load(purchaseItem.getImage()).into(((ImageView) item.findViewById(R.id.imageview_purchase)));
                ((TextView) item.findViewById(R.id.purchases_item_title)).setText(purchaseItem.getName());
                ((TextView) item.findViewById(R.id.purchases_item_color)).setText(purchaseItem.getColor());
                ((TextView) item.findViewById(R.id.purchases_item_size)).setText(purchaseItem.getSize());
                ((TextView) item.findViewById(R.id.purchases_item_price)).setText("$ " + purchaseItem.getSalePrice().toString().replaceAll("\\.?0*$", ""));
                ((TextView) item.findViewById(R.id.purchases_item_qty)).setText("" + purchaseItem.getQuantity());
                TextView tvNormalPrice = item.findViewById(R.id.purchases_item_normal_price);
                tvNormalPrice.setText("$ " + RemoveTrailingZero.removeTrailing(purchaseItem.getPrice()));
                tvNormalPrice.setPaintFlags(tvNormalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                (item.findViewById(R.id.purchases_item_review)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProductDetailActivity.writeAReview(context, purchaseItem.getProductRef().getId());
                    }
                });

                list.addView(item);
            }
        }

        Purchase currPurchase = purchases.get(i);
        Address address = currPurchase.getAddress();
        if (address != null) {
            LinearLayout addressLayout = (LinearLayout) layoutInflater.inflate(R.layout.purchase_address_layout, null);
            ((TextView) addressLayout.findViewById(R.id.textview_name)).setText(address.fullName);
            ((TextView) addressLayout.findViewById(R.id.textview_detail)).setText(address.detail);
            String addressText = address.getProvince() + ", " + address.getDistrict() + ", " + address.getWard();
            ((TextView) addressLayout.findViewById(R.id.textview_address)).setText(addressText);
            ((TextView) addressLayout.findViewById(R.id.textview_phone)).setText(address.phoneNumber);
            list.addView(addressLayout);
        }

        // Total
        LinearLayout totalLayout = (LinearLayout) layoutInflater.inflate(R.layout.purchases_total, null);
        String totalStr = df.format(currPurchase.getTotal());
        ((TextView) totalLayout.findViewById(R.id.textview_subtotal)).setText("$ " + RemoveTrailingZero.removeTrailing(currPurchase.getSubTotal()));
        ((TextView) totalLayout.findViewById(R.id.textview_totalPurchases)).setText("$ " + totalStr);
        ((TextView) totalLayout.findViewById(R.id.textview_discount)).setText("$ " + RemoveTrailingZero.removeTrailing(currPurchase.getDiscount()));
        ((TextView) totalLayout.findViewById(R.id.textview_deliveryFee)).setText("$ " + RemoveTrailingZero.removeTrailing(currPurchase.getDeliveryFee()));
        TextView tvOrderDate = totalLayout.findViewById(R.id.textview_orderDate);
        tvOrderDate.setText(getDateInFormat(currPurchase.getOrderDate()));

        // Display status
        TextView status = totalLayout.findViewById(R.id.textview_status);
        String currStatus = currPurchase.getStatus();
        status.setText(currStatus);
        switch (currStatus) {
            case "Finished":
                status.setTextColor(ContextCompat.getColor(context, R.color.statusFinished));
                break;
            case "Canceled":
                status.setTextColor(ContextCompat.getColor(context, R.color.statusCanceled));
                break;
            case "In Progress":
                status.setTextColor(ContextCompat.getColor(context, R.color.statusInProgress));
                break;
            default:
                break;
        }

        // Show menu
        ImageView btnOption = totalLayout.findViewById(R.id.imageview_option);
        if (currStatus.equals("In Progress")) {
            btnOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (customOptionMenu == null) {
                        initDataForCustomOptionMenu();
                    }
                    currTransactionId = currPurchase.getTransactionID();
                    customOptionMenu.show();
                }
            });
            btnOption.setVisibility(View.VISIBLE);
        } else {
            btnOption.setVisibility(View.GONE);
        }

        list.addView(totalLayout);

        return list;
    }

    private void initDataForCustomOptionMenu() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Cancel");

        customOptionMenu = new CustomOptionMenu(context, new CustomOptionMenu.IClickListenerOnItemListview() {
            @Override
            public void onClickItem(int pos) {
                switch (pos) {
                    case 0:
                        cancelOrder(currTransactionId);
                        break;
                    default:
                        break;
                }
            }
        }, data, "MODIFY", null);
    }

    private void cancelOrder(String transactionId) {
        FirebaseFirestore.getInstance().collection("transactions")
                .document(transactionId).update("status", "Canceled");
    }

    private String getDateInFormat(String date){
        try {
            Date convertedDate = formatter2.parse(date);
            String strDate = formatter.format(convertedDate);
            return strDate;
        } catch (ParseException e) {
            Log.d(TAG, "getDateInFormat: " + e);
        }
        return null;
    }
}
