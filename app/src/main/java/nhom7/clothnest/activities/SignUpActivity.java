package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.models.User;
import nhom7.clothnest.util.ValidateLogin;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class SignUpActivity extends AppCompatActivity {

    TextView btnNavSignIn;
    TextInputLayout input_fullname, input_email, input_pw;
    ImageButton btnNext;
    CustomProgressBar customProgressBar;

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
        customProgressBar = new CustomProgressBar(SignUpActivity.this);
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

        customProgressBar.show();
        auth.createUserWithEmailAndPassword(txtEmail, txtPw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            customProgressBar.dismiss();

                            // Create user on realtime db
                            User user = new User(txtFullname,"", txtEmail, "", "", "");
                            User.addUserToFirestore(user);


                            // Auto generate default img when create new user
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 5s
                                    // Delay 5s để giải quyết tình trạng bất đồng bộ
                                    autoGenerateDefaultImg();
                                }
                            }, 5000);



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

    private void autoGenerateDefaultImg() {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference userRef = storageRef.child("users/" + userInfo.getUid());


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_avatar_default);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println(exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                    new OnCompleteListener<Uri>() {

                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String fileLink = task.getResult().toString();
                            //next work with URL
                            System.out.println(fileLink);

                            // Update one field, creating the document if it does not already exist.
                            Map<String, Object> data = new HashMap<>();
                            data.put("img", fileLink);

                            db.collection(User.COLLECTION_NAME).document(userInfo.getUid())
                                    .set(data, SetOptions.merge());

                        }
                    });
            }
        });

    }


}