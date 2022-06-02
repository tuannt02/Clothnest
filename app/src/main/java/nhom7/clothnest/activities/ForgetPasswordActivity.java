package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import nhom7.clothnest.R;
import nhom7.clothnest.util.ValidateLogin;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class ForgetPasswordActivity extends AppCompatActivity {

    TextInputLayout input_email;
    TextView txtSuccess;
    ImageButton btnNext;
    LinearLayout btnNavSignIn;
    CustomProgressBar customProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initUi();
        initListener();


    }

    private void initUi()   {
        input_email = findViewById(R.id.forget_pw_input_email);
        txtSuccess = findViewById(R.id.txt_success_forget_pw);
        btnNext = findViewById(R.id.forget_pw_btn_next);
        btnNavSignIn = findViewById(R.id.forget_pw_btn_nav_sign_in);
        customProgressBar = new CustomProgressBar(ForgetPasswordActivity.this);
    }

    private void initListener() {

        btnNavSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtSuccess.setText("");
                String txtEmail = input_email.getEditText().getText().toString().trim();

                boolean validateUser = validateEmail(txtEmail);
                if (validateUser)   {
                    onClickForgotPassword();
                }

            }
        });
    }

    private boolean validateEmail(String str)   {
        if(ValidateLogin.isFieldEmpty(str))    {
            input_email.setError(ValidateLogin.ERROR_CODE1);
            return false;
        }

        if(ValidateLogin.isEmail(str) == false)    {
            input_email.setError(ValidateLogin.ERROR_CODE2);
            return false;
        }

        input_email.setError(null);
        return true;
    }

    private void onClickForgotPassword() {
        String txtEmail = input_email.getEditText().getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        customProgressBar.show();
        auth.sendPasswordResetEmail(txtEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            customProgressBar.dismiss();
                            input_email.setError(null);
                            txtSuccess.setText(ValidateLogin.SUCCESS_CODE1);
                        }
                        else    {
                            input_email.setError(ValidateLogin.ERROR_CODE5);
                        }
                    }
                });
    }
}