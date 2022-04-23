package nhom7.clothnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import nhom7.clothnest.models.User;
import nhom7.clothnest.util.ValidateLogin;

public class SignUpActivity extends AppCompatActivity {

    TextView btnNavSignIn;
    TextInputLayout input_fullname, input_email, input_pw;
    ImageButton btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUi();
        initListener();

    }


    private void initUi() {
        btnNavSignIn = findViewById(R.id.sign_up_btn_nav_sign_in);
        input_email = findViewById(R.id.sign_up_input_email);
        input_pw = findViewById(R.id.sign_up_input_password);
        input_fullname = findViewById(R.id.sign_up_input_fullname);
        btnNext = findViewById(R.id.sign_up_btn_next);

    }

    private void initListener() {

        // Nav to Sign up page
        btnNavSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        // Event click btn sign up
        btnNext.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String txtFullname = input_fullname.getEditText().getText().toString().trim();
                String txtEmail = input_email.getEditText().getText().toString().trim();
                String txtPw = input_pw.getEditText().getText().toString().trim();

                boolean validateFieldFn =  validateFullname(txtFullname);
                boolean validateFieldEm =  validateEmail(txtEmail);
                boolean validateFieldPw =  validatePassword(txtPw);
                boolean validateUser = validateFieldFn && validateFieldEm && validateFieldPw;

                if(validateUser)    {
                    onClickSignUp();
                }
            }
        });
    }

    private boolean validateFullname(String str)  {
        if(ValidateLogin.isFieldEmpty(str))    {
            input_fullname.setError(ValidateLogin.ERROR_CODE1);
            return false;
        }
        input_fullname.setError(null);
        return true;
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

        if(ValidateLogin.isPassword(str) == false)  {
            input_pw.setError(ValidateLogin.ERROR_CODE3);
            return false;
        }

        input_pw.setError(null);
        return true;
    }



    private void onClickSignUp() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String txtFullname = input_fullname.getEditText().getText().toString().trim();
        String txtEmail = input_email.getEditText().getText().toString().trim();
        String txtPw = input_pw.getEditText().getText().toString().trim();

        auth.createUserWithEmailAndPassword(txtEmail, txtPw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Create user on realtime db
                            User user = new User(txtFullname, txtEmail, "", "", "");
                            User.addAppendUser(user);

                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);

                            // Close activity another
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}