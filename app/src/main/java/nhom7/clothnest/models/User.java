package nhom7.clothnest.models;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import nhom7.clothnest.localDatabase.UserInfo_Sqlite;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;

public class User {

    public static final String COLLECTION_NAME = "users";

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
                ", NAME='" + NAME + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", DOB='" + DOB + '\'' +
                ", PHONE='" + PHONE + '\'' +
                ", GENDER='" + GENDER + '\'' +
                ", TYPE=" + TYPE +
                '}';
    }

    public static void addUserToFirestore(User user) {
        // Get info from Authentication
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_NAME).document(userInfo.getUid()).set(user);
    }

    public static void updateUserProfileFirestore(User user)    {
        FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_NAME).document(userInfo.getUid()).set(user);
    }


    public static void updateUserProfileAuthentication(String fullname, Uri uri)   {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;


        UserProfileChangeRequest profileUpdates;

        if(uri != null) {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(fullname)
                    .setPhotoUri(uri)
                    .build();
        }
        else    {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(fullname)
                    .build();
        }


        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Thanh cong");
                        }
                        else    {
                            System.out.println("That bai");
                        }
                    }
                });
    }

}