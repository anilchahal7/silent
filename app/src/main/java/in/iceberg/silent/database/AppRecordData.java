package in.iceberg.silent.database;

import android.content.SharedPreferences;

public class AppRecordData {

    private static final String HOME_ADDRESS = "homeAddress";
    private static final String OFFICE_ADDRESS = "officeAddress";

    private static final String DND_START_HOUR = "dndStartHour";
    private static final String DND_START_MINUTE = "dndStartMinute";

    private static final String DND_END_HOUR = "dndEndHour";
    private static final String DND_END_MINUTE = "dndEndMinute";


    private synchronized static SharedPreferences getSharedPreferences() {
        return SharedPreferencesCreator.getInstance().getSharedPreferences();
    }

    private synchronized static SharedPreferences.Editor getEditor() {
        return SharedPreferencesCreator.getInstance().getEditor();
    }

    public synchronized static void setHomeAddress(String homeAddress) {
        getEditor().putString(HOME_ADDRESS, homeAddress).apply();
    }

    public synchronized static String getHomeAddress() {
        return getSharedPreferences().getString(HOME_ADDRESS, null);
    }

    public synchronized static void setOfficeAddress(String homeAddress) {
        getEditor().putString(OFFICE_ADDRESS, homeAddress).apply();
    }

    public synchronized static String getOfficeAddress() {
        return getSharedPreferences().getString(OFFICE_ADDRESS, null);
    }

    public synchronized static int getDNDStartHour() {
        return getSharedPreferences().getInt(DND_START_HOUR, -1);
    }

    public static void setDNDStartHour(int hour) {
        getEditor().putInt(DND_START_HOUR, hour).apply();
    }

    public synchronized static int getDNDStartMinute() {
        return getSharedPreferences().getInt(DND_START_MINUTE, -1);
    }

    public static void setDNDStartMinute(int minute) {
        getEditor().putInt(DND_START_MINUTE, minute).apply();
    }

    public synchronized static int getDNDEndHour() {
        return getSharedPreferences().getInt(DND_END_HOUR, -1);
    }

    public static void setDNDENDHour(int hour) {
        getEditor().putInt(DND_END_HOUR, hour).apply();
    }

    public synchronized static int getDNDENDMinute() {
        return getSharedPreferences().getInt(DND_END_MINUTE, -1);
    }

    public static void setDNDEndMinute(int minute) {
        getEditor().putInt(DND_END_MINUTE, minute).apply();
    }
}

