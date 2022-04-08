package nhom7.clothnest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView avatar, changePassword, btnDatePicker;
    private EditText fullName, dob, username, email, password, phoneNumber;
    private RadioButton male, female, other;
    private RadioGroup gender;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initView();
        setupViewListener();
        setupBackButton();
        setupSaveButton();
        setupDatePicker();
        setupChangePassword();
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
        username = findViewById(R.id.edittext_username_editProfile);
        email = findViewById(R.id.edittext_email_editProfile);
        password = findViewById(R.id.edittext_phoneNumber_editProfile);
//        male = findViewById(R.id.rdo_male_editProfile);
//        female = findViewById(R.id.rdo_female_editProfile);
//        other = findViewById(R.id.rdo_other_editProfile);
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
                if (validate())
                    finish();
            }
        });
    }

    // Validate
    private Boolean validate() {
        // full name
        if (fullName.getText().toString().isEmpty()) {
            fullName.setError("This field must be entered");
            return false;
        } else {
            fullName.setError(null);
        }

        // dob
        if (dob.getText().toString().isEmpty()) {
            dob.setError("You must pick a date");
            return true;
        } else {
            dob.setText(null);
        }

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
                        dob.setText(format.format(calendar.getTime()));
                    }
                }, year, month, date);

                pickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

                pickerDialog.show();
            }
        });
    }
}