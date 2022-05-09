package nhom7.clothnest.util.customizeComponent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.OptionMenuAdapter;

public class CustomOptionMenu extends Dialog implements android.view.View.OnClickListener {
    public interface IClickListenerOnItemListview   {
        void onClickItem(int pos);
    }
    private IClickListenerOnItemListview mIClickListenerOnItemListview;

    public CustomOptionMenu(@NonNull Context context, IClickListenerOnItemListview listener, ArrayList<String> list, String titleOptionMenu, int[] listImg) {
        super(context);
        this.mIClickListenerOnItemListview = listener;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.option_menu_customize);
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        if(window == null)  {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);

        TextView title = findViewById(R.id.option_menu_title);
        ImageView btnClose = findViewById(R.id.option_menu_btnClose);
        ListView lv = findViewById(R.id.option_menu_lv);

        title.setText(titleOptionMenu);
        OptionMenuAdapter optionMenuAdapter = new OptionMenuAdapter(getContext(), R.layout.option_menu_item, list, listImg);
        lv.setAdapter(optionMenuAdapter);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                mIClickListenerOnItemListview.onClickItem(position);
                dismiss();
            }
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

    }
}
