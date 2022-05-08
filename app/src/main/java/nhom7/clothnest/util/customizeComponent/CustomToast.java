package nhom7.clothnest.util.customizeComponent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import nhom7.clothnest.R;

public class CustomToast    {

    public static void DisplayToast(@NonNull Context context, int type, String content)   {

        Toast toast = new Toast(context);

        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_customize, (ViewGroup) ((Activity) context).findViewById(R.id.layout_custom_toast1));

        RelativeLayout bg = view.findViewById(R.id.toast_bg);
        ImageView ic = view.findViewById(R.id.toast_ic);
        TextView titleState = view.findViewById(R.id.toast_state);
        TextView contentStr = view.findViewById(R.id.toast_content);

        //Assign
        if(type == 1)   { // Success
            bg.setBackgroundResource(R.drawable.border_radius_linear3);
            ic.setImageResource(R.drawable.ic_success);
            titleState.setText("Success");
        }
        if(type == 2)   { // Error
            bg.setBackgroundResource(R.drawable.border_radius_linear4);
            ic.setImageResource(R.drawable.ic_info);
            titleState.setText("Error");
        }
        if(type == 3)   { // Warning
            bg.setBackgroundResource(R.drawable.border_radius_linear5);
            ic.setImageResource(R.drawable.ic_info);
            titleState.setText("Warning");
        }
        if(type == 4)   { // Infomation
            bg.setBackgroundResource(R.drawable.border_radius_linear6);
            ic.setImageResource(R.drawable.ic_info);
            titleState.setText("Infomation");
        }
        contentStr.setText(content);

        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

    }

}
