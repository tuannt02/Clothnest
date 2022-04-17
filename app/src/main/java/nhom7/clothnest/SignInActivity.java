package nhom7.clothnest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    TextView btnNavSignUp, btnForgetPw;
    TextInputLayout input_email, input_pw;
    ImageButton btnNext;

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
    }
}