package nhom7.clothnest.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringNormalizer {
    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d").replaceAll("Đ", "D");
    }
}
