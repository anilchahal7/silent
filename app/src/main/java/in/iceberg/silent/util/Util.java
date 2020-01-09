package in.iceberg.silent.util;

public class Util {
    public static String getTime(int hours, int min) {
        String timeSet;
        if (hours > 12) {
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }
        String minutes;
        if (min < 10) {
            minutes = "0" + min;
        } else {
            minutes = String.valueOf(min);
        }
        return String.valueOf(hours) + ':' + minutes + " " + timeSet;
    }
}
