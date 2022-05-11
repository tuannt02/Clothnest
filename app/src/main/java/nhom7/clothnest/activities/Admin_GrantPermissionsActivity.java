package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.UserGrantPermissionsAdapter;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;
import nhom7.clothnest.util.customizeComponent.CustomDialog;

public class Admin_GrantPermissionsActivity extends AppCompatActivity {

    ImageView btnClose;
    TextView btnClear;
    EditText inputSearch;
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

        userArrayList = new ArrayList<>();
        userGrantPermissionsAdapter = new UserGrantPermissionsAdapter(Admin_GrantPermissionsActivity.this, R.layout.grant_permissions_item, userArrayList, new UserGrantPermissionsAdapter.ICLickListenerOnOptionBtn() {
            @Override
            public void grantCustomer(int position) {
                CustomDialog customDialog = new CustomDialog(Admin_GrantPermissionsActivity.this,
                        "Confirm",
                        "Are you sure you want to move this user as CLIENT?",
                        new CustomDialog.IClickListenerOnOkBtn() {
                            @Override
                            public void onResultOk() {
                                User user = userArrayList.get(position);
                                user.setTYPE(1);
                                userArrayList.set(position, user);
                                userGrantPermissionsAdapter.notifyDataSetChanged();
                            }
                        });

                customDialog.show();
            }

            @Override
            public void grantStaff(int position) {
                CustomDialog customDialog = new CustomDialog(Admin_GrantPermissionsActivity.this,
                        "Confirm",
                        "Are you sure you want to move this user as STAFF?",
                        new CustomDialog.IClickListenerOnOkBtn() {
                            @Override
                            public void onResultOk() {
                                User user = userArrayList.get(position);
                                user.setTYPE(2);
                                userArrayList.set(position, user);
                                userGrantPermissionsAdapter.notifyDataSetChanged();
                            }
                        });

                customDialog.show();
            }

            @Override
            public void grantAdmin(int position) {
                CustomDialog customDialog = new CustomDialog(Admin_GrantPermissionsActivity.this,
                        "Confirm",
                        "Are you sure you want to move this user as ADMIN?",
                        new CustomDialog.IClickListenerOnOkBtn() {
                            @Override
                            public void onResultOk() {
                                User user = userArrayList.get(position);
                                user.setTYPE(3);
                                userArrayList.set(position, user);
                                userGrantPermissionsAdapter.notifyDataSetChanged();
                            }
                        });

                customDialog.show();
            }
        });
        lvPermissions.setAdapter(userGrantPermissionsAdapter);

        getUserAndShowOnListview();

    }

    private void initUi()   {
        btnClose = findViewById(R.id.admin_grant_permissions_btn_close);
        btnClear = findViewById(R.id.admin_grant_permissions_btn_clear);
        inputSearch = findViewById(R.id.admin_grant_permissions_input_search);
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
                inputSearch.setText("");
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

    private void getUserAndShowOnListview() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy tất cả user
        db.collection(User.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Lặp qua từng document
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User userItem = new User();
                                Map<String, Object> tempObject;
                                tempObject = document.getData();

                                String docID = document.getId();
                                userItem.setUID(docID);

                                // Lặp qua từng field của một document
                                Iterator myVeryOwnIterator = tempObject.keySet().iterator();
                                while(myVeryOwnIterator.hasNext()) {
                                    String key=(String)myVeryOwnIterator.next();

                                    if(key.equals("name"))   {
                                        String name = (String)tempObject.get(key);
                                        userItem.setNAME(name);
                                    }

                                    if(key.equals("email"))   {
                                        String email = (String)tempObject.get(key);
                                        userItem.setEMAIL(email);
                                    }

                                    if(key.equals("type"))   {
                                        int type =Integer.valueOf(tempObject.get(key).toString());
//                                        System.out.println(tempObject.get(key).toString());
                                        userItem.setTYPE(type);
                                    }

                                    if(key.equals("img"))   {
                                        String img = (String)tempObject.get(key);
                                        userItem.setIMG(img);
                                    }





                                }

                                userArrayList.add(userItem);
                                userGrantPermissionsAdapter.notifyDataSetChanged();

                            }
//                            customProgressBar.dismiss();
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}