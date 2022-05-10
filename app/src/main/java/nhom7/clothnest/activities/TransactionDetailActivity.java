package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.TransactionDetailAdapter;
import nhom7.clothnest.models.Product1;
import nhom7.clothnest.models.Transaction_Detail;

public class TransactionDetailActivity extends AppCompatActivity {

    private  List <Transaction_Detail> transaction_detailList;
    private  ListView listView;
    private TransactionDetailAdapter transactionDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        listView = findViewById(R.id.listview_item_transaction);
        getDetailProduct();
    }

    private void getDetailProduct() {
        transaction_detailList = new ArrayList<>();
        transaction_detailList.add(new Transaction_Detail("1", "Oversize Hoodie",307, R.drawable.sample, 3));
        transaction_detailList.add(new Transaction_Detail("1", "Oversize Hoodie",307, R.drawable.sample, 3));
        transaction_detailList.add(new Transaction_Detail("1", "Oversize Hoodie",307, R.drawable.sample, 3));
        transaction_detailList.add(new Transaction_Detail("1", "Oversize Hoodie",307, R.drawable.sample, 3));
        transaction_detailList.add(new Transaction_Detail("1", "Oversize Hoodie",307, R.drawable.sample, 3));
        transaction_detailList.add(new Transaction_Detail("1", "Oversize Hoodie",307, R.drawable.sample, 3));
        transaction_detailList.add(new Transaction_Detail("1", "Oversize Hoodie",307, R.drawable.sample, 3));
        transaction_detailList.add(new Transaction_Detail("1", "Oversize Hoodie",307, R.drawable.sample, 3));
        transactionDetailAdapter = new TransactionDetailAdapter(transaction_detailList);
        listView.setAdapter(transactionDetailAdapter);

    }
}