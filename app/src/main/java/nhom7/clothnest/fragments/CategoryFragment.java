package nhom7.clothnest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import nhom7.clothnest.adapters.CategoryAdapter;
import nhom7.clothnest.adapters.Product_ThumbnailAdapter;
import nhom7.clothnest.models.CategoryItem;
import nhom7.clothnest.R;
import nhom7.clothnest.models.Product_Thumbnail;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;

public class CategoryFragment extends Fragment {
    ListView listView;
    ArrayList<CategoryItem> categoryItemArrayList;
    CategoryAdapter categoryAdapter;

    public static String selectedCategory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_category, container, false);

        listView = mView.findViewById(R.id.list_Item);
        GetCategory();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedCategory = categoryItemArrayList.get(i).getCategoryName();

                ReplaceFragment(new SearchProductsFragment());
            }
        });

        return mView;
    }


    private void GetCategory() {
        categoryItemArrayList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), R.layout.category_item, categoryItemArrayList);
        listView.setAdapter(categoryAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(CategoryItem.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                String category = document.getString("name");
                                categoryItemArrayList.add(new CategoryItem(category, R.drawable.ic_right_arrow));
                                categoryAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
    private void ReplaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.search_frame, fragment);
        transaction.commit();
    }
}