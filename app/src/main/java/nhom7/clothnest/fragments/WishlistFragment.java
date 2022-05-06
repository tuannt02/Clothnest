package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import nhom7.clothnest.activities.CartActivity;
import nhom7.clothnest.adapters.CustomWishlistAdapter;
import nhom7.clothnest.R;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;


public class WishlistFragment extends Fragment {

    ListView listViewWishlist;
    ImageButton btnNavCart, orderOptionMenu;
    TextView txtQuantity;
    CustomWishlistAdapter customWishlistAdapter;
    LinearLayout emptyView;
    CustomProgressBar customProgressBar;
    ArrayList<Wishlist> wishlistArray;

    public WishlistFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_wishlist, container, false);

        // Init view
        listViewWishlist = (ListView)root.findViewById(R.id.listview_wishlist);
        customProgressBar = new CustomProgressBar(getContext());
        wishlistArray = new ArrayList<>();
        customWishlistAdapter = new CustomWishlistAdapter(getContext(), R.layout.wishlist_item, wishlistArray, new CustomWishlistAdapter.ICLickListenerOnItemListview() {
            @Override
            public void removeItem(int position, String docID) {
                wishlistArray.remove(position);
                customWishlistAdapter.notifyDataSetChanged();
                removeWishlistItemFromFirestore(docID);
                updateQuantityItemInListView(root, wishlistArray.size());
                updateViewEmpty(root, wishlistArray.size());
            }
        });

        updateViewEmpty(root, wishlistArray.size());
        listViewWishlist.setAdapter(customWishlistAdapter);

        getWishListAndShowOnListview(root);

        return root;
    }

    private void getWishListAndShowOnListview(View root)   {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        customProgressBar.show();
        // Lấy tất cả wishlist của một user
        db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Lặp qua từng document
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Wishlist wishlistItem = new Wishlist();
                                Map<String, Object> tempObject;
                                tempObject = document.getData();

                                String docID = document.getId();
                                wishlistItem.setKey(docID);

                                // Lặp qua từng field của một document
                                Iterator myVeryOwnIterator = tempObject.keySet().iterator();
                                while(myVeryOwnIterator.hasNext()) {
                                    String key=(String)myVeryOwnIterator.next();

                                    if(key.equals("date_add"))   {
                                        Timestamp timestamp = (Timestamp)tempObject.get(key);
                                        wishlistItem.setDate_add(timestamp);
                                    }

                                    if (key.equals("product_id"))    {
                                        DocumentReference docRef = (DocumentReference) tempObject.get(key);
                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String name = documentSnapshot.getString("name");
                                                String main_img = documentSnapshot.getString("main_img");
                                                Double price = documentSnapshot.getDouble("price");
                                                int discount = (int)Math.round(documentSnapshot.getDouble("discount"));

                                                wishlistItem.setProductName(name);
                                                wishlistItem.setProductImage(main_img);
                                                wishlistItem.setRegularCost(price);
                                                wishlistItem.setDiscount(discount);
                                                wishlistArray.add(wishlistItem);
                                                customWishlistAdapter.notifyDataSetChanged();
                                                updateQuantityItemInListView(root, wishlistArray.size());
                                                updateViewEmpty(root, wishlistArray.size());
                                            }
                                        });
                                    }


                                }

                            }
                            customProgressBar.dismiss();
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupClickListener(view);
    }

    private void setupClickListener(View view) {
        // Set up click event cho cac danh muc
        setupBtnNavCart(view);
        setupOrderOptionMenu(view);
    }

    private void setupBtnNavCart(View view)  {
        btnNavCart = view.findViewById(R.id.imagebutton_naviCart);
        btnNavCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupOrderOptionMenu(View view)    {
        orderOptionMenu = view.findViewById(R.id.imagebutton_order_option_menu);
        orderOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = getListOptionMenu();
                CustomOptionMenu orderOptionMenu = new CustomOptionMenu(getContext(), new CustomOptionMenu.IClickListenerOnItemListview() {
                    @Override
                    public void onClickItem(int pos) {
                        // Order by (0.Date add, 1.Price (Low - High) 2.Price (High - Low))
                        if(pos == 0)    { // Date add
                            OrderByDateAdd(wishlistArray);
                        }

                        if(pos == 1)    { // Price Low -> High
                            OrderByPriceLowToHigh(wishlistArray);
                        }

                        if(pos == 2)    { // Price High -> Low
                            OrderByPriceHighToLow(wishlistArray);
                        }

                    }
                },list, "ORDER BY", null);
                orderOptionMenu.show();

            }
        });
    }

    private void updateQuantityItemInListView(View view, int quantity) {
        txtQuantity = view.findViewById(R.id.wishlist_quantity_item);
        txtQuantity.setText(quantity + "  ITEMS");

    }

    private void updateViewEmpty(View view, int quantity) {
        emptyView = view.findViewById(R.id.wishlist_view_empty);
        if(quantity == 0)    {
            emptyView.setVisibility(View.VISIBLE);
        }
        else    {
            emptyView.setVisibility(View.GONE);
        }
    }

    private ArrayList<String> getListOptionMenu()   {
        ArrayList<String> list = new ArrayList<>();
        list.add("Date add");
        list.add("Price (Low - High)");
        list.add("Price (High - Low)");

        return list;
    }

    private void OrderByDateAdd(ArrayList<Wishlist> wishlistArrayList)   {
        Collections.sort(wishlistArrayList, new Comparator<Wishlist>() {
            @Override
            public int compare(Wishlist wishlist, Wishlist t1) {
                return wishlist.getDate_add().compareTo(t1.getDate_add());
            }
        });
        customWishlistAdapter.notifyDataSetChanged();
    }
    private void OrderByPriceLowToHigh(ArrayList<Wishlist> wishlistArrayList)   {
        Collections.sort(wishlistArrayList, new Comparator<Wishlist>() {
            @Override
            public int compare(Wishlist wishlist, Wishlist t1) {
                return Double.compare(wishlist.getDiscountCost(), t1.getDiscountCost());
            }
        });
        customWishlistAdapter.notifyDataSetChanged();
    }
    private void OrderByPriceHighToLow(ArrayList<Wishlist> wishlistArrayList)   {
        Collections.sort(wishlistArrayList, new Comparator<Wishlist>() {
            @Override
            public int compare(Wishlist wishlist, Wishlist t1) {
                return Double.compare(t1.getDiscountCost(), wishlist.getDiscountCost());
            }
        });
        customWishlistAdapter.notifyDataSetChanged();
    }

    private void removeWishlistItemFromFirestore(String docID)    {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(User.COLLECTION_NAME + '/' + userInfo.getUid() + '/' + Wishlist.COLLECTION_NAME)
                .document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Delete success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Delete fails", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}