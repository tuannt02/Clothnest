package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
    private CircleImageView colorItem;

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

        colorItem.setImageResource(getColorFromName(colorList.get(i).getName()));
        if (colorList.get(i).getName().compareTo("White") == 0) {
            colorItem.setBorderColor(Color.BLACK);
            colorItem.setBorderWidth(1);
        }

        getEvent(i);

        return view;
    }

    private void getEvent(int i) {
        colorItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CircleImageView temp = (CircleImageView)view.findViewById(R.id.colorItem_ivCircle);

                if (!colorList.get(i).isSelected()) {
                    temp.setBorderColor(Color.parseColor("#DF7861"));
                    temp.setBorderWidth(10);
                    colorList.get(i).setSelected(true);
                }else{
                    if (colorList.get(i).getName().compareTo("White") == 0) {
                        temp.setBorderColor(Color.BLACK);
                        temp.setBorderWidth(1);
                    }else{
                        temp.setBorderColor(Color.TRANSPARENT);
                        temp.setBorderWidth(0);
                    }
                    colorList.get(i).setSelected(false);
                }
            }
        });
    }

    private int getColorFromName(String name) {
        switch (name) {
            case "Black":
                return R.color.black;
            case "Blue":
                return R.color.blue;
            case "Lime Green":
                return R.color.lime_green;
            case "Yellow":
                return R.color.yellow;
            case "Red":
                return R.color.red;
            case "Pink":
                return R.color.pink;
            case "Green":
                return R.color.green;
            case "Gray":
                return R.color.gray;
            case "Light Pink":
                return R.color.light_pink;
            default:
                return R.color.white;
        }
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
