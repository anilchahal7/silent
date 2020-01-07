package in.iceberg.silent.util;

public class TextUtils {

    public static boolean isNullOrEmpty(String strText) {
        if (strText != null) {
            return strText.isEmpty();
        }
        return true;
    }

    public static boolean isNotNullOrEmpty(String strText) {
        if (strText != null) {
            return !strText.isEmpty();
        }
        return false;
    }
}
