package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nhom7.clothnest.util.ValidateLogin;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextActivity();
    }



    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)    {
            // User is not logged in
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            //finish();
        }
        else    {
            // User logged in
            // Some security-sensitive actions—such as deleting an account,
            // setting a primary email address, and changing a password -> reAuthen
            ValidateLogin.reAuthentication(user, SplashScreen.this);
            //finish();
        }
    }


}