package in.iceberg.silent.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import in.iceberg.silent.application.SilentApplication;

class SharedPreferencesCreator {
    private static SharedPreferencesCreator sharedPreferencesCreator;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    private SharedPreferencesCreator() {
        sharedPreferences = SilentApplication.getAttendanceApplication().getSharedPreferences
                ("in.iceberg.silent.preference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    static synchronized SharedPreferencesCreator getInstance() {
        if (sharedPreferencesCreator == null) {
            sharedPreferencesCreator = new SharedPreferencesCreator();
        }
        return sharedPreferencesCreator;
    }

    synchronized SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    synchronized SharedPreferences.Editor getEditor() {
        return editor;
    }
}
