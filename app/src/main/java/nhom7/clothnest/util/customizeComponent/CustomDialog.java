package nhom7.clothnest.util.customizeComponent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import nhom7.clothnest.R;

public class CustomDialog extends Dialog implements android.view.View.OnClickListener{

    public interface IClickListenerOnOkBtn   {
        void onResultOk();
    }
    private IClickListenerOnOkBtn mIClickListenerOnOkBtn;

    public CustomDialog(@NonNull Context context, String titleDialog, String contentDialog, IClickListenerOnOkBtn listener) {
        super(context);
        this.mIClickListenerOnOkBtn = listener;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom);
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        if(window == null)  {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // Init view
        TextView title = findViewById(R.id.dig_title);
        TextView btnClose = findViewById(R.id.dig_btn_cancel);
        TextView btnOke = findViewById(R.id.dig_btn_ok);
        TextView content = findViewById(R.id.dig_content);

        // Assign
        title.setText(titleDialog);
        content.setText(contentDialog);

        // Set on click
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickListenerOnOkBtn.onResultOk();
            }
        });

    }

    @Override
    public void onClick(View view) {

    }
}
