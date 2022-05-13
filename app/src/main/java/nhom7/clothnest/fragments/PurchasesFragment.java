package nhom7.clothnest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.adapters.CustomPurchaseAdapter;
import nhom7.clothnest.models.Purchase;
import nhom7.clothnest.models.PurchaseItem;
import nhom7.clothnest.R;

// This is a child fragment of Profile Fragment
public class PurchasesFragment extends Fragment {
    private ArrayList<Purchase> purchases;
    private CustomPurchaseAdapter adapter;
    private ListenerRegistration transactionListener;

    // Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference transactionRef = db.collection("transactions");

    private static final String TAG = "PurchasesFragment";

    public PurchasesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchases, container, false);

        initializePurchaseList();
        Log.d("initializeList", "Init model list");
        setUpPurchasesList(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        transactionListener = transactionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, error.toString());
                    return;
                }

                for (DocumentSnapshot documentSnapshot: value.getDocuments()) {
                    Purchase purchase = documentSnapshot.toObject(Purchase.class);
                    purchase.setTransactionID(documentSnapshot.getId());
                    purchases.add(purchase);

                    
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        transactionListener.remove();
    }

    private void initializePurchaseList() {
        purchases = new ArrayList<>();

    }

    private void setUpPurchasesList(View view) {
        ListView lvPurchases = view.findViewById(R.id.listview_purchases);
        registerForContextMenu(lvPurchases);
        adapter = new CustomPurchaseAdapter(getContext(), purchases);
        lvPurchases.setAdapter(adapter);
        lvPurchases.addFooterView(getLayoutInflater().inflate(R.layout.purchases_margin_bottom, null));
        lvPurchases.addHeaderView(getLayoutInflater().inflate(R.layout.purchases_margin_bottom, null));
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listview_purchases) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Purchase currPurchase = (Purchase) lv.getItemAtPosition(acmi.position);

            if (currPurchase.getStatus().equals("In Progress")) {
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.purchases_context_menu, menu);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cancel:
                cancelOrder();
                break;
            default:
                break;
        }
        return true;
    }

    private void cancelOrder() {
        Toast.makeText(getContext(), "Order canceled!", Toast.LENGTH_SHORT).show();
    }
}