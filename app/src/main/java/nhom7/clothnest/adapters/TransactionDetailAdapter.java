package nhom7.clothnest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Transaction;
import nhom7.clothnest.models.Transaction_Detail;

public class TransactionDetailAdapter extends BaseAdapter {
    List<Transaction_Detail> transaction_detailList;
    View mview;
    private  TextView tvidDetail,tvnameDetail,tvpriceDetail, tvquantityDetail;
    private ImageView imageViewDetail;

    public TransactionDetailAdapter(List<Transaction_Detail>transaction_detailList)
    {
        this.transaction_detailList=transaction_detailList;

    }
    @Override
    public int getCount() {
        return transaction_detailList.size();
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
        mview= view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.transaction_detail_item,viewGroup,false);
        reference();
        getdata(i);
        return view;
    }

    private void getdata(int i) {
        Transaction_Detail transaction = transaction_detailList.get(i);
        tvidDetail.setText(transaction.getIdDetail());
        tvnameDetail.setText(transaction.getNameDetail());
        Double price = Double.parseDouble(String.valueOf(transaction.getPriceDetail()));
        tvpriceDetail.setText("$ " + price.toString().replaceAll("\\.?[0-9]*$", ""));

        int quantity = transaction.getQuantilyDetail();
        tvquantityDetail.setText(""+quantity);
        imageViewDetail.setImageResource(transaction.getImageListDetail());
    }

    private void reference() {
        tvidDetail = mview.findViewById(R.id.item_id);
        tvnameDetail= mview.findViewById(R.id.item_name);
        tvpriceDetail=mview.findViewById(R.id.item_price);
        tvquantityDetail=mview.findViewById(R.id.item_quantity);
        imageViewDetail = mview.findViewById(R.id.item_image);
    }
}
