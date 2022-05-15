package nhom7.clothnest.util;

import android.widget.EditText;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class ValidateAddress {
    public static final String ERROR_EMPTY_FIELD = "You must fill in this field!";
    public static final String ERROR_PROVINCE = "You must choose province!";
    public static final String ERROR_DISTRICT = "You must choose district!";
    public static final String ERROR_WARD = "You must choose ward!";

    public static boolean isValid(
            EditText etName, MaterialAutoCompleteTextView cbProvince, MaterialAutoCompleteTextView cbDistrict
            , MaterialAutoCompleteTextView cbWard, EditText etDetail, EditText etPhone
    ) {
        return checkIsEditTextEmpty(etName) && checkIsEditTextEmpty(etDetail) &&
        checkIsEditTextEmpty(etPhone) && checkIsComboBoxIsSelected(cbProvince, ERROR_PROVINCE) &&
        checkIsComboBoxIsSelected(cbDistrict, ERROR_DISTRICT) && checkIsComboBoxIsSelected(cbWard, ERROR_WARD);

    }

    public static boolean checkIsEditTextEmpty(EditText editText) {
        if (editText.length() == 0) {
            editText.setError(ERROR_EMPTY_FIELD);
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    public static boolean checkIsComboBoxIsSelected(MaterialAutoCompleteTextView cb, String errorText) {
        if (cb.length() == 0) {
            cb.setError(errorText);
            return false;
        } else {
            cb.setError(null);
            return true;
        }
    }
}
