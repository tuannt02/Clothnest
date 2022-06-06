package nhom7.clothnest.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import nhom7.clothnest.R;
import nhom7.clothnest.models.ColorClass;

public class Filter_colorAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ColorClass> colorList;
    private View mView;
    private ImageView colorItem,  ivCheck;

    public Filter_colorAdapter(Context mContext, ArrayList<ColorClass> colorList) {
        this.mContext = mContext;
        this.colorList = colorList;
    }

    @Override
    public int getCount() {
        return colorList.size();
    }

    @Override
    public Object getItem(int i) {
        return colorList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mView = view = inflater.inflate(R.layout.filter_color_item, null);

        colorItem = mView.findViewById(R.id.colorItem_ivCircle);
        ivCheck = mView.findViewById(R.id.colorItem_ivCheck);

        colorItem.setBackgroundColor(Color.parseColor(colorList.get(i).getHex()));

        if (colorList.get(i).isSelected()) {
            ivCheck.setVisibility(View.VISIBLE);
        }else{
            ivCheck.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public static void getColorAndNotify(ArrayList<ColorClass> list, Filter_colorAdapter adapter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(ColorClass.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ColorClass color = new ColorClass();
                                list.add(color);

                                color.setHex(document.getString("hex"));
                                color.setName(document.getString("name"));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }


}
