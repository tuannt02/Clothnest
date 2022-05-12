package nhom7.clothnest.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Transaction;

public class TransactionAdapter extends BaseAdapter {
   private List<Transaction> transactionList;
   private  View mview;
   private Context context;
   private Layout layout;
   private  TextView tvid,tvname,tvdate,tvprice, tvstate;


   public TransactionAdapter(List<Transaction>transactionList)
   {
       this.transactionList=transactionList;
   }
    @Override
    public int getCount() {
        return this.transactionList.size();
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
               .inflate(R.layout.layout_transaction,viewGroup,false);
        reference();
        getdata(i);
        return view;
    }

    private void getdata(int i) {
        Transaction transaction = transactionList.get(i);
        tvid.setText(transaction.getIdTransaction());
        tvname.setText(transaction.getNameTransaction());
        tvdate.setText(transaction.getDateTransaction());
        Double price = Double.parseDouble(String.valueOf(transaction.getPriceTransaction()));
        tvprice.setText("$ " + price.toString().replaceAll("\\.?[0-9]*$", ""));
        tvstate.setText(transaction.getStateTransaction());
    }
    private void reference() {
         tvid= mview.findViewById(R.id.textid);
         tvname=mview.findViewById(R.id.textname);
         tvdate= mview.findViewById(R.id.textdate);
         tvprice = mview.findViewById(R.id.textprice);
         tvstate= mview.findViewById(R.id.textstate);
    }
}
