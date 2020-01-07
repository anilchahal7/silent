package in.iceberg.silent.database;

import android.content.SharedPreferences;

public class AppRecordData {

    private static final String HOME_ADDRESS = "homeAddress";
    private static final String OFFICE_ADDRESS = "officeAddress";

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

}

