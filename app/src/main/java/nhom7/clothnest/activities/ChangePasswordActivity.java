package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nhom7.clothnest.R;
import nhom7.clothnest.localDatabase.UserInfo_Sqlite;
import nhom7.clothnest.notifications.NetworkChangeReceiver;
import nhom7.clothnest.util.ValidateLogin;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputLayout currentPassword, newPassword, confirmPassword;
    int passwordInputType;
    ImageView btnBack;
    TextView txtSuccessChangePassword;
    MaterialButton btnSave;
    CustomProgressBar customProgressBar;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Init view
        initView();

        // Thiet lap gia tri cho passwordInputType;
        // passwordInputType = currentPassword.getInputType();

        // Thiet lap button back
        setupBtnBack();

        // Thiet lap button Save
        setupBtnSave();

        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void initView() {
        currentPassword = findViewById(R.id.edittext_currentPassword_changePassword);
        newPassword = findViewById(R.id.edittext_newPassword_changePassword);
        confirmPassword = findViewById(R.id.edittext_confirmNewPassword_changePassword);
        btnBack = findViewById(R.id.imageview_back_changePassword);
        txtSuccessChangePassword = findViewById(R.id.txt_success_change_pw);
        btnSave = findViewById(R.id.button_save_changePassword);
        customProgressBar = new CustomProgressBar(ChangePasswordActivity.this);
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
                String txtCurrentPassword = currentPassword.getEditText().getText().toString().trim();
                String txtNewPassword = newPassword.getEditText().getText().toString().trim();
                String txtConfirmPassword = confirmPassword.getEditText().getText().toString().trim();

                boolean validateCurrentPasswordTxt = validateCurrentPassword(txtCurrentPassword);
                boolean validateNewPasswordTxt = validateNewPassword(txtNewPassword);
                boolean validateConfirmPasswordTxt = validateConfirmPassword(txtNewPassword, txtConfirmPassword);
                boolean validateUser =  validateCurrentPasswordTxt &&
                                        validateNewPasswordTxt &&
                                        validateConfirmPasswordTxt;

                if(validateUser)    {
                    onClickChangePassword(txtNewPassword);

                }
            }
        });
    }


    private boolean validateCurrentPassword(String str)    {

        if(ValidateLogin.isFieldEmpty(str)) {
            currentPassword.setError(ValidateLogin.ERROR_CODE1);
            return false;
        }

        if(ValidateLogin.checkMatchTwoString(str, UserInfo_Sqlite.PASSWORD) == false)    {
            currentPassword.setError(ValidateLogin.ERROR_CODE6);
            return false;
        }

        currentPassword.setError(null);
        return true;
    }

    private boolean validateNewPassword(String str)    {
        if(ValidateLogin.isFieldEmpty(str)) {
            newPassword.setError(ValidateLogin.ERROR_CODE1);
            return false;
        }

        if(ValidateLogin.isPassword(str) == false)  {
            newPassword.setError(ValidateLogin.ERROR_CODE3);
            return false;
        }

        newPassword.setError(null);
        return true;
    }

    private boolean validateConfirmPassword(String new_pw, String re_enter_pw)    {
        if(ValidateLogin.isFieldEmpty(re_enter_pw)) {
            confirmPassword.setError(ValidateLogin.ERROR_CODE1);
            return false;
        }

        if(ValidateLogin.checkMatchTwoString(new_pw, re_enter_pw) == false)  {
            confirmPassword.setError(ValidateLogin.ERROR_CODE7);
            return false;
        }

        confirmPassword.setError(null);
        return true;
    }

    private void onClickChangePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        customProgressBar.show();
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            customProgressBar.dismiss();

                            // Update local db
                            UserInfo_Sqlite userInfo_sqlite = new UserInfo_Sqlite(ChangePasswordActivity.this);
                            userInfo_sqlite.updateTableAcc(newPassword);
                            userInfo_sqlite.setInfoUser();

                            confirmPassword.setError(null);

                            txtSuccessChangePassword.setText(ValidateLogin.SUCCESS_CODE2);
                            // Delay 2s, show success message
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);


                        }
                        else    {
                            confirmPassword.setError(ValidateLogin.ERROR_CODE8);
                        }

                    }
                });
    }
}