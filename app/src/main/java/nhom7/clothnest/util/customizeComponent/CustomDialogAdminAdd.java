package nhom7.clothnest.util.customizeComponent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import nhom7.clothnest.R;
import nhom7.clothnest.util.ValidateLogin;

public class CustomDialogAdminAdd extends Dialog implements android.view.View.OnClickListener {

    public interface IClickListenerOnSaveBtn   {
        void onSubmit(String txtInput1, String txtInput2);
    }
    private IClickListenerOnSaveBtn mIClickListenerOnSaveBtn;

    public CustomDialogAdminAdd(@NonNull Context context, String titleDialog, int quantityInput, String hintInput1, String hintInput2, String text_input1, int text_input2, IClickListenerOnSaveBtn listener) {
        super(context);
        this.mIClickListenerOnSaveBtn = listener;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_admin_add);
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
        TextView title = findViewById(R.id.dig_admin_add_title);
        TextView btnClose = findViewById(R.id.dig_admin_add_btnCancel);
        TextView btnSave = findViewById(R.id.dig_admin_add_btnSave);
        EditText input1 = findViewById(R.id.dig_admin_add_input_1);
        TextView validate_input1 = findViewById(R.id.dig_admin_add_validate_input_1);
        EditText input2 = findViewById(R.id.dig_admin_add_input_2);
        TextView validate_input2 = findViewById(R.id.dig_admin_add_validate_input_2);


        // Assign
        title.setText(titleDialog);
        input1.setHint(hintInput1);
        input2.setHint(hintInput2);
        input1.setText(text_input1);
        if(text_input2 != -1) {
            input2.setText(String.valueOf(text_input2));
        }
        else    {
            input2.setText("");
        }
        if(quantityInput == 1)  {
            input2.setVisibility(View.GONE);
            validate_input2.setVisibility(View.GONE);
        }

        // Set on click
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtInput1 = input1.getText().toString().trim();
                String txtInput2 = input2.getText().toString().trim();

                // Validate before submit
                if(quantityInput == 1)  {
                    if(txtInput1.equals("")) {
                        validate_input1.setText(ValidateLogin.ERROR_CODE1);
                        validate_input1.setVisibility(View.VISIBLE);
                        return;
                    }
                    validate_input1.setVisibility(View.GONE);
                }

                if(quantityInput == 2)  {
                    if(txtInput1.equals("")) {
                        validate_input1.setText(ValidateLogin.ERROR_CODE1);
                        validate_input1.setVisibility(View.VISIBLE);
                    }
                    else    {
                        validate_input1.setVisibility(View.GONE);
                    }

                    if(txtInput2.equals("")) {
                        validate_input2.setText(ValidateLogin.ERROR_CODE1);
                        validate_input2.setVisibility(View.VISIBLE);
                    }
                    else    {
                        int numb = Integer.valueOf(txtInput2);

                        if(numb > 100 || numb <= 0) {
                            validate_input2.setText("The value of this field ranges from 1 to 100");
                            validate_input2.setVisibility(View.VISIBLE);
                            return;
                        }
                        else
                            validate_input2.setVisibility(View.GONE);
                    }

                    if(txtInput1.equals("") || txtInput2.equals(""))    return;
                }

                mIClickListenerOnSaveBtn.onSubmit(txtInput1, txtInput2);
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
