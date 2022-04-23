package nhom7.clothnest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    Context mContext;
    private int mLayout;
    private List<CategoryItem> listCategory;

    public CategoryAdapter(Context mContext, int mLayout, List<CategoryItem> listCategory) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.listCategory = listCategory;
    }

    @Override
    public int getCount() {
        return listCategory.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.category_item, null);

        TextView textView = view.findViewById(R.id.category_itemName);
        ImageView icon = view.findViewById(R.id.category_itemButton);

        textView.setText(listCategory.get(i).categoryName);
        icon.setImageResource(listCategory.get(i).icon);

        return view;
    }
}
