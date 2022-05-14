package nhom7.clothnest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.models.Filter_CheckItem;

public class Filter_BrandAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Filter_CheckItem> brandList;
    private View mView;

    public Filter_BrandAdapter(Context mContext, ArrayList<Filter_CheckItem> brandList) {
        this.mContext = mContext;
        this.brandList = brandList;
    }

    @Override
    public int getCount() {
        return brandList.size();
    }

    @Override
    public Object getItem(int i) {
        return brandList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mView = view = inflater.inflate(R.layout.filter_check_item, null);

        CheckBox checkBox = mView.findViewById(R.id.checkItem_CheckBox);
        TextView tvBrand = mView.findViewById(R.id.checkItem_title);

        checkBox.setChecked(brandList.get(i).isChecked());
        tvBrand.setText(brandList.get(i).getTitle());


        return mView;
    }
    public static void getBrandAndNotify(ArrayList<Filter_CheckItem> list, Filter_BrandAdapter adapter){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }
}
