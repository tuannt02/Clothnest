package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.UserGrantPermissionsAdapter;
import nhom7.clothnest.models.User;

public class Admin_GrantPermissionsActivity extends AppCompatActivity {

    ImageView btnClose;
    TextView btnClear;
    ListView lvPermissions;
    ArrayList<User> userArrayList;
    UserGrantPermissionsAdapter userGrantPermissionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_grant_permissions);

        // Init ui
        initUi();

        //set on click
        setOnClickListener();

        userArrayList = getUser();
        userGrantPermissionsAdapter = new UserGrantPermissionsAdapter(Admin_GrantPermissionsActivity.this,R.layout.grant_permissions_item, userArrayList);
        lvPermissions.setAdapter(userGrantPermissionsAdapter);

    }

    private void initUi()   {
        btnClose = findViewById(R.id.admin_grant_permissions_btn_close);
        btnClear = findViewById(R.id.admin_grant_permissions_btn_clear);
        lvPermissions = findViewById(R.id.admin_grant_permissions_lv);
        userArrayList = new ArrayList<>();
    }

    private void setOnClickListener()   {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private ArrayList<User> getUser()   {
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(new User(
                1,
                "Phan Thanh Tú Đội",
                "https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/users%2Fanh1.jpg?alt=media&token=525cf826-05e2-4ff3-ac11-8f0c1f9e403b",
                "tuphanga@gmail.com",
                "",
                "",
                ""));

        userArrayList.add(new User(
                2,
                "Võ Thị Vân",
                "https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/users%2Fanh2.jpg?alt=media&token=d5c80216-afee-4910-8dc9-7104d250edb9",
                "vangaga@gmail.com",
                "",
                "",
                ""));
        userArrayList.add(new User(
                3,
                "Hoàng Thị Anh Tuấn",
                "https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/users%2Fanh3.jfif?alt=media&token=1df11e93-95f4-4c52-99b2-16c93f2f205c",
                "vangaga@gmail.com",
                "",
                "",
                ""));
        userArrayList.add(new User(
                1,
                "Phan Thanh Tú Đội",
                "https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/users%2Fanh1.jpg?alt=media&token=525cf826-05e2-4ff3-ac11-8f0c1f9e403b",
                "tuphanga@gmail.com",
                "",
                "",
                ""));

        userArrayList.add(new User(
                2,
                "Võ Thị Vân",
                "https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/users%2Fanh2.jpg?alt=media&token=d5c80216-afee-4910-8dc9-7104d250edb9",
                "vangaga@gmail.com",
                "",
                "",
                ""));
        userArrayList.add(new User(
                3,
                "Hoàng Thị Anh Tuấn",
                "https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/users%2Fanh3.jfif?alt=media&token=1df11e93-95f4-4c52-99b2-16c93f2f205c",
                "vangaga@gmail.com",
                "",
                "",
                ""));
        userArrayList.add(new User(
                1,
                "Phan Thanh Tú Đội",
                "https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/users%2Fanh1.jpg?alt=media&token=525cf826-05e2-4ff3-ac11-8f0c1f9e403b",
                "tuphanga@gmail.com",
                "",
                "",
                ""));

        userArrayList.add(new User(
                2,
                "Võ Thị Vân",
                "https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/users%2Fanh2.jpg?alt=media&token=d5c80216-afee-4910-8dc9-7104d250edb9",
                "vangaga@gmail.com",
                "",
                "",
                ""));
        userArrayList.add(new User(
                3,
                "Hoàng Thị Anh Tuấn",
                "https://firebasestorage.googleapis.com/v0/b/clothnest-da508.appspot.com/o/users%2Fanh3.jfif?alt=media&token=1df11e93-95f4-4c52-99b2-16c93f2f205c",
                "vangaga@gmail.com",
                "",
                "",
                ""));

        return userArrayList;
    }
}