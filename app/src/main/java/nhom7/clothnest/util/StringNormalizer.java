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

    public static String convertPrice(int price)  {
        String priceAfterConvert = Integer.toString(price);
        String comma = ",";
        int k = 0;
        for(int i = priceAfterConvert.length() - 1; i >= 0; i--)   {
            ++k;
            if(k%3 ==0 && i!=0) {
                priceAfterConvert = priceAfterConvert.substring(0, i) + comma + priceAfterConvert.substring(i,priceAfterConvert.length());
            }
        }

        return priceAfterConvert;
    }
}
