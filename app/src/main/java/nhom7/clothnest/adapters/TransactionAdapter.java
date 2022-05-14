package nhom7.clothnest.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.TransactionDetailActivity;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.Transaction;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;

public class TransactionAdapter extends BaseAdapter {
    private List<Transaction> transactionList;
    private View mview;
    private Context context;
    private TextView tvname, tvdate, tvprice, tvstate;


    public TransactionAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @Override
    public int getCount() {
        return transactionList.size();
    }

    @Override
    public Object getItem(int i) {
        return transactionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mview = view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_transaction, viewGroup, false);
        reference();
        getdata(i);
        return view;
    }


    private void getdata(int i) {
        Transaction transaction = transactionList.get(i);
        tvname.setText(transaction.getNameTransaction());

        String date = transaction.getDateTransaction();
        SimpleDateFormat input = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date answer = input.parse(date);
            tvdate.setText(output.format(answer));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Double price = Double.parseDouble(String.valueOf(transaction.getPriceTransaction()));
        tvprice.setText("$ " + price);

        tvstate.setText(transaction.getStateTransaction());
        String ChangeColor = tvstate.getText().toString();
        switch (ChangeColor) {
            case "Finished":
                tvstate.setTextColor(Color.parseColor("#20AF14"));
                break;
            case "Canceled":
                tvstate.setTextColor(Color.parseColor("#FF0000"));
                break;
            case "In Progress":
                tvstate.setTextColor(Color.parseColor("#FBC02D"));
                break;
        }
    }

    private void reference() {
        tvname = mview.findViewById(R.id.textname);
        tvdate = mview.findViewById(R.id.textdate);
        tvprice = mview.findViewById(R.id.textprice);
        tvstate = mview.findViewById(R.id.textstate);
    }


    public static void getDataFromTransaction(ArrayList<Transaction> listTransaction, TransactionAdapter transactionAdapter, TextView qty, TextView total) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Transaction.COLLECTION_TRANSACTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Transaction transaction = new Transaction();
                                listTransaction.add(transaction);

                                Map<String, Object> map = document.getData();
                                Iterator iterator = map.keySet().iterator();

                                while (iterator.hasNext()) {
                                    String key = (String) iterator.next();
                                    transaction.setIdTransaction(document.getId());

                                    if (key.equals("orderDate")) {
                                        transaction.setDateTransaction((String) map.get(key));
                                        transactionAdapter.notifyDataSetChanged();
                                    }

                                    if (key.equals("status")) {
                                        transaction.setStateTransaction((String) map.get(key));
                                        transactionAdapter.notifyDataSetChanged();
                                    }

                                    if (key.equals("total")) {
                                        Double price = document.getDouble(key);
                                        transaction.setPriceTransaction(price);
                                        transactionAdapter.notifyDataSetChanged();
                                    }

                                    if (key.equals("userRef")) {
                                        DocumentReference docRef = (DocumentReference) map.get(key);
                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                               transaction.setNameTransaction(documentSnapshot.getString("name"));
                                                transactionAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                }
                            }

                            qty.setText(listTransaction.size() + "");

                            double totalPrice = 0.0;
                            for (int i = 0; i < listTransaction.size(); i++) {
                                if (listTransaction.get(i).getStateTransaction().equals("Finished")) {
                                    totalPrice += listTransaction.get(i).getPriceTransaction();
                                }
                            }
                            total.setText(totalPrice + "");
                        }
                    }
                });
    }
}
