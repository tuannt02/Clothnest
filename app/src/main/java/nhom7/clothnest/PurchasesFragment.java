package nhom7.clothnest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

// This is a child fragment of Profile Fragment
public class PurchasesFragment extends Fragment {
    private ArrayList<Purchase> purchases;

    public PurchasesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchases, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializePurchaseList();
        Log.d("initializeList", "Init model list");
        setUpPurchasesList(view);
    }

    private void initializePurchaseList() {
        purchases = new ArrayList<>();

        // purchase 1
        ArrayList<PurchaseItem> purchaseItems = new ArrayList<>();
        purchaseItems.add(new PurchaseItem(R.drawable.sample, 1, "Áo Polo Vải Pique Co Giãn Ngắn Tay", "21/03/2022", "COL31WHITE", "WOMEN M", 345d));
        purchaseItems.add(new PurchaseItem(R.drawable.sample, 1, "Áo Polo Vải Pique Co Giãn Ngắn Tay", "21/03/2022", "COL31WHITE", "WOMEN M", 342d));
        purchases.add(new Purchase(purchaseItems));

        // purchase 2
        purchaseItems = new ArrayList<>();
        purchaseItems.add(new PurchaseItem(R.drawable.sample, 1, "Áo Polo Vải Pique Co Giãn Ngắn Tay", "21/03/2022", "COL31WHITE", "WOMEN M", 341d));//        purchases.add(new PurchaseItem(R.drawable.sample, 1, "Áo Polo Vải Pique Co Giãn Ngắn Tay", "21/03/2022", "COL31WHITE", "WOMEN M", 311d));
        purchases.add(new Purchase(purchaseItems));

        // purchase 3
        purchaseItems = new ArrayList<>();
        purchaseItems.add(new PurchaseItem(R.drawable.sample, 1, "Áo Polo Vải Pique Co Giãn Ngắn Tay", "21/03/2022", "COL31WHITE", "WOMEN M", 310d));
        purchaseItems.add(new PurchaseItem(R.drawable.sample, 1, "Áo Polo Vải Pique Co Giãn Ngắn Tay", "21/03/2022", "COL31WHITE", "WOMEN M", 309d));
        purchaseItems.add(new PurchaseItem(R.drawable.sample, 1, "Áo Polo Vải Pique Co Giãn Ngắn Tay", "21/03/2022", "COL31WHITE", "WOMEN M", 109d));
        purchases.add(new Purchase(purchaseItems));
    }

    private void setUpPurchasesList(View view) {
        ListView lvPurchases = view.findViewById(R.id.listview_purchases);
        CustomPurchaseAdapter adapter = new CustomPurchaseAdapter(getContext(), purchases);
        lvPurchases.setAdapter(adapter);
        lvPurchases.addFooterView(getLayoutInflater().inflate(R.layout.purchases_margin_bottom, null));
        lvPurchases.addHeaderView(getLayoutInflater().inflate(R.layout.purchases_margin_bottom, null));
    }
}