package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.TransactionDetailActivity;
import nhom7.clothnest.adapters.TransactionAdapter;
import nhom7.clothnest.models.Transaction;


public class TransactionFragment extends Fragment {

    private ListView listView;
    private List<Transaction> transactionListView;
    private  View mview;
    private TransactionAdapter transactionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview =inflater.inflate(R.layout.fragment_transaction, container, false);
        reference();

        getDetailTransaction();
        onClickDetailTransaction();
        return mview;
    }

    private void onClickDetailTransaction() {
     listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             Intent intent = new Intent(getContext(), TransactionDetailActivity.class);
             startActivity(intent);
         }
     });
    }

    private void reference() {
        listView = mview.findViewById(R.id.lvTransaction);
    }

    private void getDetailTransaction() {
        transactionListView = new ArrayList<>();
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionListView.add(new Transaction("1","vo dinh van",203,"22/04/2002","Finish"));
        transactionAdapter = new TransactionAdapter(transactionListView);
        listView.setAdapter(transactionAdapter);
    }
}