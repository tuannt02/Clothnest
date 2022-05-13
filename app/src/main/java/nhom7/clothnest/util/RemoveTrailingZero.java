package nhom7.clothnest.util;

public class RemoveTrailingZero {
    public static String removeTrailing(Double num) {
        return num.toString().replaceAll("\\.?0*$", "");
    }
}
