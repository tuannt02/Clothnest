package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.activities.TransactionDetailActivity;
import nhom7.clothnest.adapters.Product_ThumbnailAdapter;
import nhom7.clothnest.adapters.TransactionAdapter;
import nhom7.clothnest.models.Address;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.Transaction;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;


public class TransactionFragment extends Fragment {

    private ListView listView;
    private ArrayList<Transaction> arrayList;
    private View mview;
    private TransactionAdapter transactionAdapter;
    private TextView quantity, sumprice;
    private Transaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_transaction, container, false);
        reference();
        getDetailTransaction();
        onClickDetailTransaction();
        return mview;
    }


    private void onClickDetailTransaction() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                transaction = arrayList.get(i);
                transaction.getAddressTransaction().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Address address = documentSnapshot.toObject(Address.class);
                        Intent intent = new Intent(getContext(), TransactionDetailActivity.class);
                        intent.putExtra("customer", transaction.getNameTransaction());
                        intent.putExtra("date", transaction.getDateTransaction());
                        intent.putExtra("state", transaction.getStateTransaction());
                        intent.putExtra("key", transaction.getIdTransaction());
                        intent.putExtra("discount", transaction.getDiscountTransaction());
                        intent.putExtra("delivery", transaction.getDeliveryTransaction());
                        intent.putExtra("address", address);

                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void reference() {
        listView = mview.findViewById(R.id.lvTransaction);
        quantity = mview.findViewById(R.id.textquantity);
        sumprice = mview.findViewById(R.id.textsumprice);
    }

    private void getDetailTransaction() {
        arrayList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(getContext(), arrayList);
        listView.setAdapter(transactionAdapter);
        TransactionAdapter.getDataFromTransaction(arrayList, transactionAdapter, quantity, sumprice);
    }

}