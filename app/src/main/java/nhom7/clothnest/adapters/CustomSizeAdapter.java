package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.ChatRoom;
import nhom7.clothnest.models.Size;

public class CustomSizeAdapter extends ArrayAdapter {
    Context context;
    ArrayList<Size> sizes;
    ArrayList<Size> originSizes;

    public CustomSizeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Size> objects) {
        super(context, resource, objects);
        this.context = context;
        this.originSizes = objects;
        this.sizes = objects;
    }

    @Override
    public int getCount() {
        return sizes.size();
    }

    @Override
    public Object getItem(int i) {
        return sizes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_size,viewGroup,false);

        Size size = sizes.get(i);

        TextView tvShortName;
        TextView tvName;
        ImageView btnRemove;

        // init view
        tvShortName = view.findViewById(R.id.textview_shortName);
        tvName = view.findViewById(R.id.textview_name);
        btnRemove = view.findViewById(R.id.imageview_trash);

        // set data
        tvShortName.setText(size.shortName);
        tvName.setText(size.name);

        // set btnRemove onClickEvent
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("sizes").document(size.sizeId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Removed failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                sizes = (ArrayList<Size>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint.equals("")) {
                    results.count = originSizes.size();
                    results.values = originSizes;
                    return results;
                }

                ArrayList<Size> filteredSizes = new ArrayList<Size>();


                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < originSizes.size(); i++) {
                    String name = originSizes.get(i).name;
                    if (name.toLowerCase().contains(constraint.toString()))  {
                        filteredSizes.add(originSizes.get(i));
                    }
                }

                results.count = filteredSizes.size();
                results.values = filteredSizes;

                return results;
            }
        };

        return filter;
    }
}
