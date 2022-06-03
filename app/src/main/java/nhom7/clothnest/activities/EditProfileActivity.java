package nhom7.clothnest.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import nhom7.clothnest.R;
import nhom7.clothnest.localDatabase.UserInfo_Sqlite;
import nhom7.clothnest.models.User;
import nhom7.clothnest.notifications.NetworkChangeReceiver;
import nhom7.clothnest.util.OpenGallery;
import nhom7.clothnest.util.ValidateLogin;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;
import nhom7.clothnest.util.customizeComponent.CustomToast;

public class EditProfileActivity extends AppCompatActivity {
    public static final int REQ_CODE_READ_EXTERNAL_STORAGE = 10;
    private Uri mUri;
    private Bitmap avatarUser;
    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if(intent == null)  return;
                mUri = intent.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);

                    // Set img frpm gallery to bitmap var
                    avatarUser = bitmap;
                    setAvatar(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private ImageView avatar, changeAvatar, changePassword, btnDatePicker;
    private EditText email;
    private TextInputLayout fullName, dob, phoneNumber;
    private RadioButton male, female, other;
    private RadioGroup gender;
    private MaterialButton btnSave;
    private CustomProgressBar customProgressBar;

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initView();
        setupProfileFromFirebase();
        setupBackButton();
        setupSaveButton();
        setupDatePicker();
        setupChangeAvatar();
        setupChangePassword();

        broadcastReceiver = new NetworkChangeReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    // Thiet lap profile tu database
    private void setupProfileFromFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(user == null)    {
            return;
        }

        String user_ID = user.getUid();
        String emailUser = user.getEmail();

        // Set info from firestore
        DocumentReference userRef = db.collection(User.COLLECTION_NAME).document(user_ID);
        customProgressBar.show();
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                fullName.getEditText().setText(user.getNAME());
                dob.getEditText().setText(user.getDOB());
                phoneNumber.getEditText().setText(user.getPHONE());
                Glide.with(EditProfileActivity.this).load(user.getIMG()).error(R.drawable.ic_avatar_default).into(avatar);
                setFieldGender(user.getGENDER());

                // Load img to bitmap
//                Glide.with(EditProfileActivity.this)
//                        .asBitmap()
//                        .load(user.getIMG())
//                        .into(new CustomTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                avatarUser = resource;
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//                            }
//                        });

                customProgressBar.dismiss();
            }
        });



        // Set info from Service Authentication
        email.setText(emailUser);
        System.out.println("setupProfileFromFirebase function");
    }

    // Thiet lap button change password
    private void setupChangePassword() {
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        avatar = findViewById(R.id.imageview_avatar_editProfile);
        fullName = findViewById(R.id.edittext_fullname_editProfile);
        dob = findViewById(R.id.edittext_dateOfBirth_editProfile);
        email = findViewById(R.id.edittext_email_editProfile);
        phoneNumber = findViewById(R.id.edittext_phoneNumber_editProfile);
        male = findViewById(R.id.rdo_male_editProfile);
        female = findViewById(R.id.rdo_female_editProfile);
        other = findViewById(R.id.rdo_other_editProfile);
        gender = findViewById(R.id.rdog_gender_editProfile);
        changeAvatar = findViewById(R.id.button_changeAvatar_editProfile);
        changePassword = findViewById(R.id.button_changePassword_editProfile);
        btnSave = findViewById(R.id.button_save_editProfile);
        btnDatePicker = findViewById(R.id.datepicker_editProfile);
        customProgressBar = new CustomProgressBar(EditProfileActivity.this);
    }



    private void setupChangeAvatar()    {
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery.onClickOpenGallery(EditProfileActivity.this, mActivityResultLauncher);
            }
        });

        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onClickRequestPermission();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_CODE_READ_EXTERNAL_STORAGE)   {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                OpenGallery.openGallery(mActivityResultLauncher);
//                openGallery();
            }
            else    {
                Toast.makeText(this, "You denied, please allow the app to access the gallery", Toast.LENGTH_SHORT).show();
            }
        }
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

    // Thiet lap save button
    private void setupSaveButton() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtFullname = fullName.getEditText().getText().toString().trim();
                String txtDob = dob.getEditText().getText().toString().trim();
                String txtPhoneNum = phoneNumber.getEditText().getText().toString().trim();
                String txtEmail = email.getText().toString().trim();
                String gender = getFieldGender();

                System.out.println(gender);

                boolean validateFieldFn = validateFullname(txtFullname);
                boolean validateFieldDob = validateDob(txtDob);
                boolean validateFieldPhoneN = validatePhoneNum(txtPhoneNum);
                boolean validateUser =  validateFieldFn &&
                                        validateFieldDob &&
                                        validateFieldPhoneN;

                if(validateUser)    {
                    User user = new User(txtFullname,"", txtEmail, txtDob, txtPhoneNum, gender);

                    User.updateUserProfileAuthentication(txtFullname, null);
                    User.updateUserProfileFirestore(user);

                    if(mUri != null) {
                        updateImgToFirestoreAndStorage(avatarUser);
                    }
                    finish();
                }

            }
        });
    }

    private boolean validateFullname(String str)  {
        if(ValidateLogin.isFieldEmpty(str))    {
            fullName.setError(ValidateLogin.ERROR_CODE1);
            return false;
        }
        fullName.setError(null);
        return true;
    }

    private boolean validateDob(String str) {
        if(ValidateLogin.isFieldEmpty(str))    {
            dob.setError(ValidateLogin.ERROR_CODE9);
            return false;
        }
        dob.setError(null);
        return true;
    }

    private boolean validatePhoneNum(String str)    {
        if(ValidateLogin.isFieldEmpty(str)) {
            phoneNumber.setError(ValidateLogin.ERROR_CODE1);
            return false;
        }

        if(ValidateLogin.checkMin(str,10) == false)  {
            phoneNumber.setError(ValidateLogin.ERROR_CODE10);
            return false;
        }

        phoneNumber.setError(null);
        return true;
    }

    // Thiet lap date picker
    private void setupDatePicker() {
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int date = calendar.get(Calendar.DATE),
                        month = calendar.get(Calendar.MONTH),
                        year = calendar.get(Calendar.YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        SimpleDateFormat format = new SimpleDateFormat("dd / MM / yyyy");
                        calendar.set(i, i1, i2);
                        dob.getEditText().setText(format.format(calendar.getTime()));
                    }
                }, year, month, date);

                pickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

                pickerDialog.show();
            }
        });
    }

    private void setFieldGender(String genderName)  {
        if(genderName != "")  {
            switch (genderName)   {
                case "MALE":
                    male.setChecked(true);
                    break;
                case "FEMALE":
                    female.setChecked(true);
                    break;
                case "OTHER":
                    other.setChecked(true);

            }
        }
    }

    private String getFieldGender()    {

        int selectedId = gender.getCheckedRadioButtonId();
        switch(selectedId) {
            case R.id.rdo_male_editProfile:
                    return "MALE";
            case R.id.rdo_female_editProfile:
                    return "FEMALE";
            case R.id.rdo_other_editProfile:
                    return "OTHER";
        }
        return "";
    }

    private void setAvatar(Bitmap bitmap)   {
        avatar.setImageBitmap(bitmap);
    }

    private void updateImgToFirestoreAndStorage(Bitmap bitmap)   {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference userRef = storageRef.child("users/" + userInfo.getUid());

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

                            // Mỗi Url là duy nhất cho một lần upload ảnh. Vậy nên mỗi khi đổi
                            // ảnh mới phải đổi luôn cái token. Gán token mới này vào firestore
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
                CustomToast.DisplayToast(EditProfileActivity.this, 1, "Upload successfully");
            }
        });
    }
}