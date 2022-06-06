package nhom7.clothnest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.Product_ThumbnailAdapter;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class SearchProductsFragment extends Fragment {
    private GridView gridView;
    private ImageView btnFilter;
    private View mView;
    private ArrayList<Button> btnCollectionList;

    public static ArrayList<Product_Thumbnail> productArrayList, originProductList;
    public static Product_ThumbnailAdapter thumbnailAdapter;
    public static CustomProgressBar dialog;
    public static String collectionName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search_products, container, false);

        reference();

        getProduct();

        getEvents();

        return mView;
    }

    private void reference() {
        dialog = new CustomProgressBar(getContext());
        collectionName = null;

        View includedView = (View) mView.findViewById(R.id.groupThumbnail);
        gridView = (GridView) includedView.findViewById(R.id.gridview_GroupThumbnail);
        btnFilter = mView.findViewById(R.id.btnFilter);
        getCollectionList();

    }

    private void getCollectionList(){
        btnCollectionList = new ArrayList<>();
        btnCollectionList.add(mView.findViewById(R.id.searchProduct_btn_WINTER));
        btnCollectionList.add(mView.findViewById(R.id.searchProduct_btn_LINE));
        btnCollectionList.add(mView.findViewById(R.id.searchProduct_btn_UT));
        btnCollectionList.add(mView.findViewById(R.id.searchProduct_btn_UNISEX));
        btnCollectionList.add(mView.findViewById(R.id.searchProduct_btn_UV_PROTECTION));
    }

    private void getEvents() {
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.search_frame, new CategoryFragment());
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });

        SearchFragment.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                thumbnailAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        for(Button button : btnCollectionList){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterCollection(button.getText().toString());
                    setColorButtonCollection(button);
                }
            });
        }
    }

    private void setColorButtonCollection(Button selectedBtn){
        for(Button button : btnCollectionList)
            if(button.equals(selectedBtn))
                button.setBackgroundColor(Color.parseColor("#DF7861"));
            else
                button.setBackgroundColor(Color.TRANSPARENT);
    }

    //    Thêm sản phẩm vào GridView
    public void getProduct() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.filter_frame, new Fragment());

        dialog.show();
        productArrayList = new ArrayList<>();
        originProductList = new ArrayList<>();
        thumbnailAdapter = new Product_ThumbnailAdapter(getContext(), R.layout.thumbnail, productArrayList);
        gridView.setAdapter(thumbnailAdapter);

        if (CategoryFragment.selectedCategory != null)
            Product_ThumbnailAdapter.getProductsWithSameCategory(productArrayList, thumbnailAdapter, CategoryFragment.selectedCategory);
        else
            Product_ThumbnailAdapter.getProductAndPushToGridView(productArrayList, thumbnailAdapter);
    }

    private void filterCollection(String collectionName){
        dialog.show();

        productArrayList.clear();
        FirebaseFirestore.getInstance().collection("collections").document(collectionName).collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<DocumentReference> collection_listRef = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                DocumentReference product_Ref = document.getDocumentReference("product_id");
                                collection_listRef.add(product_Ref);

                                if(task.getResult().getDocuments().indexOf(document) == task.getResult().getDocuments().size() - 1){
                                    for(Product_Thumbnail thumbnail : originProductList){
                                        DocumentReference thumbnail_Ref = FirebaseFirestore.getInstance().collection("products").document(thumbnail.getId());
                                        if(collection_listRef.contains(thumbnail_Ref))
                                            productArrayList.add(thumbnail);
                                    }
                                    thumbnailAdapter.notifyDataSetChanged();
                                    if(dialog.isShowing())
                                        dialog.dismiss();
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getProduct();
    }
}