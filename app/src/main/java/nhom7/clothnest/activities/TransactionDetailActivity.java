package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.TransactionDetailAdapter;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.Transaction_Detail;

public class TransactionDetailActivity extends AppCompatActivity {

    private ArrayList<Transaction_Detail> transaction_detailList;
    private ListView listView;
    private TransactionDetailAdapter transactionDetailAdapter;
    private TextView customerTransactionDetail, dateTransactionDetail, stateTransactionDetail;
    private String nameDetail, dateDetail, stateDetail;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        listView = findViewById(R.id.listview_item_transaction);
        getDetailProduct();
        saveData();
    }

    private void saveData() {
        Intent intent = getIntent();
        reference();

        nameDetail = intent.getStringExtra("customer");
        dateDetail = intent.getStringExtra("date");
        stateDetail = intent.getStringExtra("state");

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
                relativeLayout.setBackgroundColor(Color.GREEN);
                break;
            case "Canceled":
                relativeLayout.setBackgroundColor(Color.RED);
                break;
            case "In Progress":
                relativeLayout.setBackgroundColor(Color.YELLOW);
                break;
        }
    }

    private void reference() {
        relativeLayout = findViewById(R.id.rlayout);
        customerTransactionDetail = findViewById(R.id.transactionCustomer);
        dateTransactionDetail = findViewById(R.id.transactionpurchasedate);
        stateTransactionDetail = findViewById(R.id.transactionstate);
    }

    private void getDetailProduct() {
        transaction_detailList = new ArrayList<>();
        transactionDetailAdapter = new TransactionDetailAdapter(transaction_detailList);
        listView.setAdapter(transactionDetailAdapter);

    }
}