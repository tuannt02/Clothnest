package nhom7.clothnest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.adapters.UserGrantPermissionsAdapter;
import nhom7.clothnest.models.User;
import nhom7.clothnest.models.Wishlist;
import nhom7.clothnest.notifications.NetworkChangeReceiver;
import nhom7.clothnest.util.customizeComponent.CustomDialog;

public class Admin_GrantPermissionsActivity extends AppCompatActivity {

    ImageView btnClose;
    TextView btnClear;
    EditText inputSearch;
    ListView lvPermissions;
    ArrayList<User> userArrayList;
    ArrayList<User> listOriginal;
    UserGrantPermissionsAdapter userGrantPermissionsAdapter;

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_grant_permissions);

        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Init ui
        initUi();

        //set on click
        setOnClickListener();

        setOnTextChange();

        userArrayList = new ArrayList<>();
        userGrantPermissionsAdapter = new UserGrantPermissionsAdapter(Admin_GrantPermissionsActivity.this, R.layout.grant_permissions_item, userArrayList, new UserGrantPermissionsAdapter.ICLickListenerOnOptionBtn() {
            @Override
            public void grantCustomer(int position, String UID) {
                CustomDialog customDialog = new CustomDialog(Admin_GrantPermissionsActivity.this,
                        "Confirm",
                        "Are you sure you want to move this user as CLIENT?",
                        2,
                        new CustomDialog.IClickListenerOnOkBtn() {
                            @Override
                            public void onResultOk() {
                                User user = userArrayList.get(position);
                                user.setTYPE(1);
                                userArrayList.set(position, user);
                                userGrantPermissionsAdapter.notifyDataSetChanged();

                                updateFieldTypeOfUser(UID, 1);
                            }
                        });

                customDialog.show();
            }

            @Override
            public void grantStaff(int position, String UID) {
                CustomDialog customDialog = new CustomDialog(Admin_GrantPermissionsActivity.this,
                        "Confirm",
                        "Are you sure you want to move this user as STAFF?",
                        2,
                        new CustomDialog.IClickListenerOnOkBtn() {
                            @Override
                            public void onResultOk() {
                                User user = userArrayList.get(position);
                                user.setTYPE(2);
                                userArrayList.set(position, user);
                                userGrantPermissionsAdapter.notifyDataSetChanged();

                                updateFieldTypeOfUser(UID, 2);
                            }
                        });

                customDialog.show();
            }

            @Override
            public void grantAdmin(int position, String UID) {
                CustomDialog customDialog = new CustomDialog(Admin_GrantPermissionsActivity.this,
                        "Confirm",
                        "Are you sure you want to move this user as ADMIN?",
                        2,
                        new CustomDialog.IClickListenerOnOkBtn() {
                            @Override
                            public void onResultOk() {
                                User user = userArrayList.get(position);
                                user.setTYPE(3);
                                userArrayList.set(position, user);
                                userGrantPermissionsAdapter.notifyDataSetChanged();

                                updateFieldTypeOfUser(UID, 3);
                            }
                        });

                customDialog.show();
            }
        });
        lvPermissions.setAdapter(userGrantPermissionsAdapter);
        lvPermissions.setTextFilterEnabled(true);

        getUserAndShowOnListview();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void initUi()   {
        btnClose = findViewById(R.id.admin_grant_permissions_btn_close);
        btnClear = findViewById(R.id.admin_grant_permissions_btn_clear);
        inputSearch = findViewById(R.id.admin_grant_permissions_input_search);
        lvPermissions = findViewById(R.id.admin_grant_permissions_lv);
        userArrayList = new ArrayList<>();
        listOriginal = new ArrayList<>();
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
                                        userItem.setTYPE(type);
                                    }

                                    if(key.equals("img"))   {
                                        String img = (String)tempObject.get(key);
                                        userItem.setIMG(img);
                                    }
                                }

                                userArrayList.add(userItem);
                                listOriginal.add(userItem);
                                userGrantPermissionsAdapter.notifyDataSetChanged();

                            }
//                            customProgressBar.dismiss();
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void updateFieldTypeOfUser(String UID, int role)    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
        data.put("type", role);

        db.collection(User.COLLECTION_NAME).document(UID)
                .set(data, SetOptions.merge());
    }

    private void setOnTextChange()  {
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String inputSearch = charSequence.toString().toLowerCase();
                if(inputSearch == null || inputSearch.length() == 0)    {
                    userArrayList.clear();
                    userArrayList.addAll(listOriginal);
                    userGrantPermissionsAdapter.notifyDataSetChanged();
                }
                else    {
                    ArrayList<User> listFilter = getFilterByEmailOrName(inputSearch);

                    userArrayList.clear();
                    userArrayList.addAll(listFilter);
                    userGrantPermissionsAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private ArrayList<User> getFilterByEmailOrName(String inputSearch) {

        ArrayList<User> listAfterFiltered = new ArrayList<>();
        for(int k=0; k < listOriginal.size(); k++) {
            User newUser = listOriginal.get(k);
            if(newUser.getEMAIL().contains(inputSearch) || newUser.getNAME().toLowerCase().contains(inputSearch))    {
                listAfterFiltered.add(newUser);
            }
        }
        return listAfterFiltered;

    }
}