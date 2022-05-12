package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import nhom7.clothnest.R;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Voucher;
import nhom7.clothnest.util.customizeComponent.CustomDialog;
import nhom7.clothnest.util.customizeComponent.CustomOptionMenu;

public class UserGrantPermissionsAdapter extends ArrayAdapter<User> {

    public interface ICLickListenerOnOptionBtn   {
        void grantCustomer(int position, String UID);
        void grantStaff(int position, String UID);
        void grantAdmin(int position, String UID);
    }

    private ICLickListenerOnOptionBtn mICLickListenerOnOptionBtn;

    private Context context;
    private int resource;
    private ArrayList<User> userArrayList;
    private ArrayList<User> listOriginal;

    public UserGrantPermissionsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects, ICLickListenerOnOptionBtn listener) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.userArrayList = objects;
        this.mICLickListenerOnOptionBtn = listener;

        this.listOriginal = objects;
    }

    public void setRes(ArrayList<User> object) {
        userArrayList = object;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null)    {
            convertView = LayoutInflater.from(context).inflate(R.layout.grant_permissions_item,parent, false);
        }

        User userItem;
        userItem = userArrayList.get(position);

        // Get view
        ImageView avatar = convertView.findViewById(R.id.grant_permissions_item_avatar);
        TextView name = convertView.findViewById(R.id.grant_permissions_item_name);
        TextView email = convertView.findViewById(R.id.grant_permissions_item_email);
        TextView role = convertView.findViewById(R.id.grant_permissions_item_role);
        ImageButton btnOption = (ImageButton) convertView.findViewById(R.id.grant_permissions_item_btn_more);

        // Set view
        Glide.with(getContext()).load(userItem.getIMG()).into(avatar);
        name.setText(userItem.getNAME());
        email.setText(userItem.getEMAIL());
        if(userItem.getTYPE() == 1) {
            role.setText("Client");
            role.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        if(userItem.getTYPE() == 2) {
            role.setText("Staff");
            role.setTextColor(ContextCompat.getColor(context, R.color.green_role));
        }
        if(userItem.getTYPE() == 3) {
            role.setText("Admin");
            role.setTextColor(ContextCompat.getColor(context, R.color.pri_col));
        }

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = getListOptionMenu();
                CustomOptionMenu customOptionMenu = new CustomOptionMenu(getContext(), new CustomOptionMenu.IClickListenerOnItemListview() {
                    @Override
                    public void onClickItem(int pos) {
                        // 0: Add to cart, 1: Remove from wishlist
                        if(pos == 0)    { // Customer
                            mICLickListenerOnOptionBtn.grantCustomer(position, userItem.getUID());
                        }

                        if(pos == 1)    { // Staff
                            mICLickListenerOnOptionBtn.grantStaff(position, userItem.getUID());
                        }

                        if(pos == 2)    { // Admin
                            mICLickListenerOnOptionBtn.grantAdmin(position, userItem.getUID());
                        }
                    }
                }, list, "PERMISSIONS", null);
                customOptionMenu.show();
            }
        });



        return convertView;
    }

    private ArrayList<String> getListOptionMenu()   {
        ArrayList<String> list = new ArrayList<>();
        list.add("Client");
        list.add("Staff");
        list.add("Admin");

        return list;
    }
}
