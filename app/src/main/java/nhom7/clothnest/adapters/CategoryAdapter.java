package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.CategoryItem;

public class CategoryAdapter extends ArrayAdapter<CategoryItem> {
    Context mContext;
    private List<CategoryItem> listCategory;
    private List<CategoryItem> originListCategory;

    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<CategoryItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listCategory = objects;
        this.originListCategory = objects;
    }

    @Override
    public int getCount() {
        return listCategory.size();
    }

    @Override
    public CategoryItem getItem(int i) {
        return listCategory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.category_item, null);

        TextView textView = view.findViewById(R.id.category_itemName);
        ImageView icon = view.findViewById(R.id.category_itemButton);

        textView.setText(listCategory.get(i).getCategoryName());
        icon.setImageResource(listCategory.get(i).icon);

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                if (charSequence.equals("")) {
                    results.count = originListCategory.size();
                    results.values = originListCategory;
                    return results;
                }

                ArrayList<CategoryItem> filteredCategory = new ArrayList<CategoryItem>();


                // perform your search here using the searchConstraint String.

                charSequence = charSequence.toString().toLowerCase();
                for (int i = 0; i < originListCategory.size(); i++) {
                    String category_name = originListCategory.get(i).getCategoryName();
                    if (category_name.toLowerCase().contains(charSequence.toString())) {
                        filteredCategory.add(originListCategory.get(i));
                    }
                }

                results.count = filteredCategory.size();
                results.values = filteredCategory;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listCategory = (ArrayList<CategoryItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }
}
