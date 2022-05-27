package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import nhom7.clothnest.R;
import nhom7.clothnest.models.ColorClass;
import nhom7.clothnest.models.SizeClass;
import nhom7.clothnest.models.Stock;

public class StockAdapter extends BaseAdapter {
    public interface ClickListener   {
        void removeItem(int position);
    }

    private Context mContext;
    private ArrayList<Stock> stocks;
    private View mView;
    private ClickListener mClickListener;

    //View
    TextView tvSize, tvColor, tvQuantity;
    ImageView ivDelete;
    CircleImageView civColor;
    GridView gvImage;
    StockImageAdapter stockImageAdapter;


    public StockAdapter(Context mContext, ArrayList<Stock> stocks, ClickListener mClickListener) {
        this.mContext = mContext;
        this.stocks = stocks;
        this.mClickListener = mClickListener;
    }

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public Object getItem(int i) {
        return stocks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mView = view = inflater.inflate(R.layout.item_stock, null);

        reference();

        Stock stock = stocks.get(i);

        tvSize.setText(stock.getSizeName());
        tvColor.setText(stock.getColorName());
        tvQuantity.setText(stock.getQuantity() + "");

        //civColor.setImageResource(getColorFromName(stock.getColorName()));
        FirebaseFirestore.getInstance().collection(ColorClass.COLLECTION_NAME).document(stock.getColorID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        civColor.setBackgroundColor(Color.parseColor(documentSnapshot.getString("hex")));
                    }
                });

        //Đổ dữ liệu vào gridView
        stockImageAdapter = new StockImageAdapter(mContext, stock.getImageList());
        gvImage.setAdapter(stockImageAdapter);

        getEvent(i);
        return view;
    }

    private void getEvent(int i) {
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItemFromStocks(i);
            }
        });
    }

    private void reference() {
        tvSize = mView.findViewById(R.id.item_stock_tvSize);
        tvColor = mView.findViewById(R.id.item_stock_tvColor);
        tvQuantity = mView.findViewById(R.id.item_stock_tvQuantity);
        ivDelete = mView.findViewById(R.id.item_stock_ivDelete);
        civColor = mView.findViewById(R.id.item_stock_civColor);
        gvImage = mView.findViewById(R.id.item_stock_gvImage);
    }

    private void removeItemFromStocks(int position){
        mClickListener.removeItem(position);
    }
}
