package nhom7.clothnest.models;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nhom7.clothnest.EditProfileActivity;

public class User {

    public static final String TABLE_NAME = "users";

    String USER_ID;
    String NAME;
    String EMAIL;
    String DOB;
    String PHONE;
    String GENDER;
    int TYPE;

    public User()   {

    }

    public User(String NAME, String EMAIL, String DOB, String PHONE, String GENDER) {
        this.NAME = NAME;
        this.EMAIL = EMAIL;
        this.DOB = DOB;
        this.PHONE = PHONE;
        this.GENDER = GENDER;
        this.TYPE = 1;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public String getUSER_ID() { return USER_ID; }

    public String getNAME() {
        return NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public String getDOB() {
        return DOB;
    }

    public String getPHONE() {
        return PHONE;
    }

    public String getGENDER() {
        return GENDER;
    }

    public int getTYPE() { return TYPE; }

    @Override
    public String toString() {
        return "User{" +
                "USER_ID='" + USER_ID + '\'' +
                ", NAME='" + NAME + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", DOB='" + DOB + '\'' +
                ", PHONE='" + PHONE + '\'' +
                ", GENDER='" + GENDER + '\'' +
                ", TYPE=" + TYPE +
                '}';
    }

    public static void addAppendUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference(TABLE_NAME);
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();

        String key = userInfo.getUid();
        user.USER_ID = key;
        dbReference.child(key).setValue(user);
    }

//    public static User getProfileFromRealtimeDB(String user_ID)   {
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = db.getReference(User.TABLE_NAME);
//
//        ArrayList<User> arrayList = new ArrayList<>();
//
//        myRef.child(user_ID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//                arrayList.add(user);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        return arrayList.get(0);
//    }
}