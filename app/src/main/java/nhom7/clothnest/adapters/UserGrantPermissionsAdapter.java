package nhom7.clothnest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class UserGrantPermissionsAdapter extends ArrayAdapter<User> {

    private Context context;
    private int resource;
    private ArrayList<User> userArrayList;

    public UserGrantPermissionsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.userArrayList = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User userItem = userArrayList.get(position);

        if (convertView == null)    {
            convertView = LayoutInflater.from(context).inflate(R.layout.grant_permissions_item,parent, false);
        }

        // Get view
        ImageView avatar = convertView.findViewById(R.id.grant_permissions_item_avatar);
        TextView name = convertView.findViewById(R.id.grant_permissions_item_name);
        TextView email = convertView.findViewById(R.id.grant_permissions_item_name);
        TextView role = convertView.findViewById(R.id.grant_permissions_item_role);

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


        return convertView;
    }
}
