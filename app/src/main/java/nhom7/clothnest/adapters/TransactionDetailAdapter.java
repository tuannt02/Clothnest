package nhom7.clothnest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Transaction;
import nhom7.clothnest.models.Transaction_Detail;

public class TransactionDetailAdapter extends BaseAdapter {
    private ArrayList<Transaction_Detail> transaction_detailList;
    private View mview;
    private TextView tvnameDetail, tvpriceDetail, tvquantityDetail, tvsizeDetail, tvcolorDetail, tvsalepriceDetail;
    private ImageView imageViewDetail;
    private Context context;


    public TransactionDetailAdapter(ArrayList<Transaction_Detail> transaction_detailList, Context context) {
        this.transaction_detailList = transaction_detailList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return transaction_detailList.size();
    }

    @Override
    public Object getItem(int i) {
        return transaction_detailList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        mview = view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.transaction_detail_item, viewGroup, false);

        reference();
        getdata(i);
        return view;
    }


    private void getdata(int i) {
        Transaction_Detail transaction = transaction_detailList.get(i);

        tvnameDetail.setText(transaction.getNameDetail());

        Glide.with(context).load(transaction.getImageListDetail()).into(imageViewDetail);
        tvsizeDetail.setText(transaction.getSizeDetail());
        tvcolorDetail.setText(transaction.getColorDetail());


        Double price = Double.parseDouble(String.valueOf(transaction.getPriceDetail()));
        tvpriceDetail.setText("$ " + price);

        int quantity = transaction.getQuantilyDetail();
        tvquantityDetail.setText("" + quantity);


        Double saleprice = Double.parseDouble(String.valueOf(transaction.getSalePriceDetail()));
        tvsalepriceDetail.setText("$ " + (double) Math.round(saleprice * 100) / 100);

    }

    private void reference() {
        tvnameDetail = mview.findViewById(R.id.item_name);
        tvpriceDetail = mview.findViewById(R.id.item_pr);
        tvquantityDetail = mview.findViewById(R.id.item_quantity);
        imageViewDetail = mview.findViewById(R.id.item_image);
        tvsizeDetail = mview.findViewById(R.id.item_size);
        tvcolorDetail = mview.findViewById(R.id.item_color);
        tvsalepriceDetail = mview.findViewById(R.id.item_saleprice);
    }

}
