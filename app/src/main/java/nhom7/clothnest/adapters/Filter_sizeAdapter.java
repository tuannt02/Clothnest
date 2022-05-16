package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.models.SizeClass;

public class Filter_sizeAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SizeClass> sizeList;
    private Button btnSize;

    public Filter_sizeAdapter(Context mContext, ArrayList<SizeClass> sizeList) {
        this.mContext = mContext;
        this.sizeList = sizeList;
    }

    private View mView;

    @Override
    public int getCount() {
        return sizeList.size();
    }

    @Override
    public Object getItem(int i) {
        return sizeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mView = view = inflater.inflate(R.layout.filter_size_item, null);

        btnSize = mView.findViewById(R.id.sizeItem_btn);

        btnSize.setText(sizeList.get(i).getShort_name());

        getEvent(i);

        return view;
    }

    private void getEvent(int i) {

        btnSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sizeList.get(i).isSelected()) {
                    view.findViewById(R.id.sizeItem_btn).setBackgroundColor(Color.parseColor("#DF7861"));
                    sizeList.get(i).setSelected(true);
                }else{
                    view.findViewById(R.id.sizeItem_btn).setBackgroundColor(Color.TRANSPARENT);
                    sizeList.get(i).setSelected(false);
                }
            }
        });
    }

    public static void getSizeAndNotify(ArrayList<SizeClass> list, Filter_sizeAdapter
            adapter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(SizeClass.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("type").equals("SIZE")) {
                                    SizeClass size = new SizeClass();
                                    list.add(size);

                                    size.setName(document.getString("name"));
                                    size.setShort_name(document.getString("short_name"));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }

    public static void getInchSizeAndNotify(ArrayList<SizeClass> list, Filter_sizeAdapter
            adapter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(SizeClass.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("type").equals("INCH")) {
                                    SizeClass size = new SizeClass();
                                    list.add(size);

                                    size.setName(document.getString("name"));
                                    size.setShort_name(document.getString("short_name"));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }

    public static void getCentimeterSizeAndNotify
            (ArrayList<SizeClass> list, Filter_sizeAdapter adapter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(SizeClass.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("type").equals("CM")) {
                                    SizeClass size = new SizeClass();
                                    list.add(size);

                                    size.setName(document.getString("name"));
                                    size.setShort_name(document.getString("short_name"));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }

}

