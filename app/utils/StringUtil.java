package utils;

public class StringUtil {

    public static int countOccurrencesOf(String string, String subString) {
        return string.length() - string.replace(subString, "").length();
    }

    public static String trimToLength(String string, int length) {
        return string.trim().substring(0, Math.min(string.trim().length(), length));
    }

}
