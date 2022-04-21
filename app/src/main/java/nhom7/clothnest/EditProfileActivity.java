package nhom7.clothnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nhom7.clothnest.models.User;
import nhom7.clothnest.util.ValidateLogin;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView avatar, changePassword, btnDatePicker;
    private EditText email;
    private TextInputLayout fullName, dob, phoneNumber;
    private RadioButton male, female, other;
    private RadioGroup gender;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initView();
        setupProfileFromFirebase();
        setupViewListener();
        setupBackButton();
        setupSaveButton();
        setupDatePicker();
        setupChangePassword();
    }

    // Thiet lap profile tu database
    private void setupProfileFromFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(User.TABLE_NAME);

        if(user == null)    {
            return;
        }

        String user_ID = user.getUid();
        String emailUser = user.getEmail();
        Uri avaUrlUser = user.getPhotoUrl();


        myRef.child(user_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                // Set info from Realtime Database
                fullName.getEditText().setText(user.getNAME());
                dob.getEditText().setText(user.getDOB());
                phoneNumber.getEditText().setText(user.getPHONE());
                setFieldGender(user.getGENDER());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Set info from Service Authentication
        email.setText(emailUser);
        Glide.with(this).load(avaUrlUser).error(R.drawable.ic_avatar_default).into(avatar);
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
        changePassword = findViewById(R.id.button_changePassword_editProfile);
        btnSave = findViewById(R.id.button_save_editProfile);
        btnDatePicker = findViewById(R.id.datepicker_editProfile);
    }

    private void setupViewListener() {
        setupChangePasswordListener();
    }

    private void setupChangePasswordListener() {
        Log.d("changePassword", "Set up successfully ");
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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

                boolean validateFieldFn = validateFullname(txtFullname);
                boolean validateFieldDob = validateDob(txtDob);
                boolean validateFieldPhoneN = validatePhoneNum(txtPhoneNum);
                boolean validateUser =  validateFieldFn &&
                                        validateFieldDob &&
                                        validateFieldPhoneN;

                String a = validateFieldDob ? "oke" : "not oke";
                System.out.println(a);
                System.out.println(txtDob);
                if(validateUser)    {
//                    Call Api
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

        if(ValidateLogin.checkMin(str,10))  {
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
}