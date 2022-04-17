package nhom7.clothnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nhom7.clothnest.localDatabase.UserInfo_Sqlite;

public class SignInActivity extends AppCompatActivity {

    TextView btnNavSignUp, btnForgetPw;
    TextInputLayout input_email, input_pw;
    ImageButton btnNext;
    UserInfo_Sqlite userInfo_sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initUi();
        initListener();

    }

    private void initUi()   {
        btnNavSignUp = findViewById(R.id.sign_in_btn_nav_sign_up);
        btnForgetPw = findViewById(R.id.sign_in_btn_forget_pw);
        input_email = findViewById(R.id.sign_in_input_email);
        input_pw = findViewById(R.id.sign_in_input_password);
        btnNext = findViewById(R.id.sign_in_btn_next);
    }

    private void initListener() {
        btnNavSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = input_email.getEditText().getText().toString().trim();
                String txtPw = input_pw.getEditText().getText().toString().trim();

                boolean validateFieldEm =  validateEmail(txtEmail);
                boolean validateFieldPw =  validatePassword(txtPw);
                boolean validateUser = validateFieldEm && validateFieldPw;
                if(validateUser)    {
                    onCickSignIn();
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

    private boolean validatePassword(String str)    {
        if(ValidateLogin.isFieldEmpty(str)) {
            input_pw.setError(ValidateLogin.ERROR_CODE1);
            return false;
        }

        input_pw.setError(null);
        return true;
    }

    private void onCickSignIn() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String txtEmail = input_email.getEditText().getText().toString().trim();
        String txtPw = input_pw.getEditText().getText().toString().trim();


        auth.signInWithEmailAndPassword(txtEmail, txtPw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            // add local database, target: re-authentication
                            userInfo_sqlite = new UserInfo_Sqlite(SignInActivity.this);
                            userInfo_sqlite.deleteTableAcc();
                            userInfo_sqlite.initAccount(txtEmail, txtPw);

                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            input_pw.setError(ValidateLogin.ERROR_CODE4);
                        }
                    }
                });

    }
}