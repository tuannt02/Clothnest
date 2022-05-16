package nhom7.clothnest.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.models.ClothColor;

public class CustomColorAdapter extends ArrayAdapter<ClothColor> {
    private Context context;
    private ArrayList<ClothColor> colors;
    private ArrayList<ClothColor> originColors;
    private static final String TAG = "CustomColorAdapter";

    public CustomColorAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ClothColor> objects) {
        super(context, resource, objects);
        this.context = context;
        colors = objects;
        originColors = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false);

        ClothColor color = colors.get(position);

        TextView tvName;
        TextView tvColor;
        LinearLayout llColor;
        ImageView btnRemove;

        // init view
        tvName = convertView.findViewById(R.id.textview_name);
        tvColor = convertView.findViewById(R.id.textview_color);
        llColor = convertView.findViewById(R.id.linearLayout_color);
        btnRemove = convertView.findViewById(R.id.imageview_trash);

        // set data
        tvName.setText(color.name);
        tvColor.setText(color.hex);
        try {
            llColor.setBackgroundColor(Color.parseColor(color.hex));
        } catch (Exception e) {
            Log.e(TAG, color.hex + " - " + position + " - " + color.name);
        }
        // set btnRemove onClickEvent
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Color")
                        .setMessage("Are you sure to remove this color?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseFirestore.getInstance().collection("colors").document(color.colorId)
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
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setCancelable(true)
                        .create();

                alertDialog.show();
            }
        });

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                colors = (ArrayList<ClothColor>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint.equals("")) {
                    results.count = originColors.size();
                    results.values = originColors;
                    return results;
                }

                ArrayList<ClothColor> filteredSizes = new ArrayList<>();


                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < originColors.size(); i++) {
                    String name = originColors.get(i).name;
                    if (name.toLowerCase().contains(constraint.toString())) {
                        filteredSizes.add(originColors.get(i));
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
