package nhom7.clothnest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static final String SUCCESS_CODE1 = "Submitted successfully! Please check your email";

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



}
