package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.TransactionDetailAdapter;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.Transaction;
import nhom7.clothnest.models.Transaction_Detail;

public class TransactionDetailActivity extends AppCompatActivity {

    private ArrayList<Transaction_Detail> transaction_detailList;
    private ListView listView;
    private TransactionDetailAdapter transactionDetailAdapter;
    private TextView customerTransactionDetail, dateTransactionDetail, stateTransactionDetail;
    private String nameDetail, dateDetail, stateDetail, idDetail;
    private RelativeLayout relativeLayout;
    private TextView address, name, phone, qty, discount, deliveryfee, subtotal, total;
    private int discountDetail, deliveryDetail;
    private double stotal = 0.0;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        reference();
        saveData();
        getDetailData();
        getProductTransactionDetail();
    }


    private void getProductTransactionDetail() {
        String temp = Transaction.COLLECTION_TRANSACTION + '/' + idDetail + '/' + Transaction_Detail.COLLECTION_TRANSACTIONDETAIL;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(temp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            transaction_detailList = new ArrayList<>();
                            transactionDetailAdapter = new TransactionDetailAdapter(transaction_detailList, getApplicationContext());
                            listView.setAdapter(transactionDetailAdapter);

                            for (QueryDocumentSnapshot doc_transactionitemlist : task.getResult()) {

                                Map<String, Object> map = doc_transactionitemlist.getData();
                                Iterator iterator = map.keySet().iterator();

                                Transaction_Detail transaction_detail = new Transaction_Detail();

                                while (iterator.hasNext()) {

                                    String key = (String) iterator.next();
                                    transaction_detail.setIdDetail(doc_transactionitemlist.getId());

                                    if (key.equals("productRef")) {
                                        DocumentReference documentReference = (DocumentReference) map.get(key);
                                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                transaction_detail.setNameDetail(documentSnapshot.getString("name"));
                                                transaction_detail.setImageListDetail(documentSnapshot.getString("main_img"));
//                                                int discount = (int) Math.round(documentSnapshot.getDouble("discount"));
//                                                transaction_detail.setDiscountDetail(discount);
                                                transactionDetailAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }


                                    if (key.equals("colorRef")) {
                                        DocumentReference documentReference = (DocumentReference) map.get(key);
                                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                transaction_detail.setColorDetail(documentSnapshot.getString("name"));
                                                transactionDetailAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    if (key.equals("sizeRef")) {
                                        DocumentReference documentReference = (DocumentReference) map.get(key);
                                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                transaction_detail.setSizeDetail(documentSnapshot.getString("short_name"));
                                                transactionDetailAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    if (key.equals("price")) {
                                        Double price = doc_transactionitemlist.getDouble(key);
                                        transaction_detail.setPriceDetail((price));
                                        transactionDetailAdapter.notifyDataSetChanged();
                                    }

                                    if (key.equals("sale_price")) {
                                        Double price = doc_transactionitemlist.getDouble(key);
                                        transaction_detail.setSalePriceDetail((price));
                                        transactionDetailAdapter.notifyDataSetChanged();
                                    }


                                    if (key.equals("quantity")) {
                                        int quantity = (int) Math.round(doc_transactionitemlist.getDouble(key));
                                        transaction_detail.setQuantilyDetail(quantity);
                                        transactionDetailAdapter.notifyDataSetChanged();
                                    }
                                }
                                transaction_detailList.add(transaction_detail);
                                transactionDetailAdapter.notifyDataSetChanged();
                            }

                            for (int i = 0; i < transaction_detailList.size(); i++) {
                                count += transaction_detailList.get(i).getQuantilyDetail();
                            }
                            qty.setText(count + "");


                            for (int i = 0; i < transaction_detailList.size(); i++) {
                                stotal += transaction_detailList.get(i).getQuantilyDetail() * transaction_detailList.get(i).getSalePriceDetail();
                            }
                            subtotal.setText("$ " + (double) Math.round(stotal * 100) / 100);

                            double a = discountDetail + deliveryDetail + stotal;
                            total.setText("$ " + (double) Math.round(a * 100) / 100);
                        }
                    }
                });
    }

    private void getDetailData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Transaction.COLLECTION_TRANSACTION + '/' + idDetail + '/' + Transaction_Detail.COLLECTION_TRANSACTIONDETAIL)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> map = document.getData();
                                Iterator iterator = map.keySet().iterator();
                                while (iterator.hasNext()) {
                                    String key = (String) iterator.next();

                                    if (key.equals("addrRef")) {
                                        DocumentReference documentReference = (DocumentReference) map.get(key);
                                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String addresstransaction = documentSnapshot.getString("province") + ", " + documentSnapshot.getString("district") + ", " + documentSnapshot.getString("ward") + ", " + documentSnapshot.getString("street_name");
                                                address.setText(addresstransaction);

                                                String nametransaction = documentSnapshot.getString("name");
                                                name.setText(nametransaction);

                                                String phonetransaction = documentSnapshot.getString("phone_num");
                                                phone.setText(phonetransaction);
                                            }
                                        });
                                    }
                                }
                                break;
                            }
                        }
                    }
                });
    }

    private void saveData() {
        Intent intent = getIntent();

        nameDetail = intent.getStringExtra("customer");
        dateDetail = intent.getStringExtra("date");
        stateDetail = intent.getStringExtra("state");
        idDetail = intent.getStringExtra("key");
        deliveryDetail = intent.getIntExtra("delivery", 0);
        discountDetail = intent.getIntExtra("discount", 0);

        customerTransactionDetail.setText(nameDetail + "");
        SimpleDateFormat input = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date answer = input.parse(dateDetail);
            dateTransactionDetail.setText(output.format(answer));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        stateTransactionDetail.setText(stateDetail + "");
        String ChangeColor = stateTransactionDetail.getText().toString();
        switch (ChangeColor) {
            case "Finished":
                relativeLayout.setBackgroundColor(Color.parseColor("#20AF14"));
                break;
            case "Canceled":
                relativeLayout.setBackgroundColor(Color.parseColor("#DF7861"));
                break;
            case "In Progress":
                relativeLayout.setBackgroundColor(Color.parseColor("#FBC02D"));
                break;
        }

        discount.setText(discountDetail + "");
        deliveryfee.setText(deliveryDetail + "");
    }

    private void reference() {
        relativeLayout = findViewById(R.id.rlayout);
        customerTransactionDetail = findViewById(R.id.transactionCustomer);
        dateTransactionDetail = findViewById(R.id.transactionpurchasedate);
        stateTransactionDetail = findViewById(R.id.transactionstate);
        listView = findViewById(R.id.listview_item_transaction);
        address = findViewById(R.id.addresss);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        qty = findViewById(R.id.transactionQuantity);
        discount = findViewById(R.id.transactionDiscountPrice);
        deliveryfee = findViewById(R.id.transactionDeliveryPrice);
        subtotal = findViewById(R.id.transactionSumPrice);
        total = findViewById(R.id.transactionTotal);
    }
}