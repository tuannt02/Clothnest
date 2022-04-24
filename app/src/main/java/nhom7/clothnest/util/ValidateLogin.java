package nhom7.clothnest.util;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nhom7.clothnest.activities.MainActivity;
import nhom7.clothnest.activities.SignInActivity;
import nhom7.clothnest.localDatabase.UserInfo_Sqlite;

public class ValidateLogin {
    public static final String EMAIL_VERIFICATION = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile(EMAIL_VERIFICATION, Pattern.CASE_INSENSITIVE);

    public static final String ERROR_CODE1 = "Please enter this field";
    public static final String ERROR_CODE2 = "Invalid email. Please re-enter";
    public static final String ERROR_CODE3 = "Min 8 characters, 1 lowercase character," +
            " 1 upppercase character and 1 number";
    public static final String ERROR_CODE4 = "Email or Password is incorrect. Please re-enter";
    public static final String ERROR_CODE5 = "Email may not exist. Please check again";
    public static final String ERROR_CODE6 = "Current password is incorrect";
    public static final String ERROR_CODE7 = "The new password does not match the old password";
    public static final String ERROR_CODE8 = "A few things have been changed recently. " +
            "Please check again";
    public static final String ERROR_CODE9 = "You must pick a date";
    public static final String ERROR_CODE10 = "Phone number at least 10 digits";

    public static final String SUCCESS_CODE1 = "Submitted successfully! Please check your email";
    public static final String SUCCESS_CODE2 = "Your password has been updated";

    public static boolean isEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
    public static boolean isFieldEmpty(String field)    {
        return field.equals("");
    }
    public static boolean isPassword(String pw) {
        return (checkMin(pw, 8) &&
                checkOneLetterLowercaseInString(pw) &&
                checkOneLetterUppercaseInString(pw) &&
                checkOneLetterNumberInString(pw)) ? true : false;
    }
    public static boolean checkMin(String str, int minLength)  {
        return str.length() >= minLength ? true : false;
    }


    public static boolean checkOneLetterLowercaseInString(String str)    {
        for (int i=0;i<str.length();i++)    {
            char character = str.charAt(i);
            int ascii = (int) character;
            if(ascii >= 97 && ascii <= 122)
                return true;

        }
        return false;
    }


    public static boolean checkOneLetterUppercaseInString(String str)   {
        for (int i=0;i<str.length();i++)    {
            char character = str.charAt(i);
            int ascii = (int) character;
            if(ascii >= 65 && ascii <= 90)
                return true;

        }
        return false;
    }

    public static boolean checkOneLetterNumberInString(String str)   {
        for (int i=0;i<str.length();i++)    {
            char character = str.charAt(i);
            int ascii = (int) character;
            if(ascii >= 48 && ascii <= 57)
                return true;

        }
        return false;
    }

    public static boolean checkMatchTwoString(String str1, String str2)  {
        return str1.equals(str2);
    }

    public static void reAuthentication(FirebaseUser user, Context context) {

        // Get infouser past
        UserInfo_Sqlite userInfo_sqlite = new UserInfo_Sqlite(context);
        userInfo_sqlite.setInfoUser();


        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(UserInfo_Sqlite.EMAIL, UserInfo_Sqlite.PASSWORD);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {


                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                        else    {
                            Intent intent = new Intent(context, SignInActivity.class);
                            context.startActivity(intent);
                        }
                    }

                });
    }

}
