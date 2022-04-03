package nhom7.clothnest;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setupBackButton();
    }

    private void setupBackButton() {
        ImageView ivBack = findViewById(R.id.imageview_back_editprofile);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileActivity.super.onBackPressed();
            }
        });
    }
}