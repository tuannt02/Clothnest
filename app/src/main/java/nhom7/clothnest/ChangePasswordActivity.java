package nhom7.clothnest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText currentPassword, newPassword;
    int passwordInputType;
    ImageView btnBack;
    MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Init view
        initView();

        // Thiet lap gia tri cho passwordInputType;
        passwordInputType = currentPassword.getInputType();

        // Thiet lap button back
        setupBtnBack();

        // Thiet lap button Save
        setupBtnSave();
    }

    private void initView() {
        currentPassword = findViewById(R.id.edittext_currentPassword_changePassword);
        newPassword = findViewById(R.id.edittext_newPassword_changePassword);
        btnBack = findViewById(R.id.imageview_back_changePassword);
        btnSave = findViewById(R.id.button_save_changePassword);
    }

    private void setupBtnBack() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupBtnSave() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}