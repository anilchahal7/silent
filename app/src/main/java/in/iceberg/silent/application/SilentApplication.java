package in.iceberg.silent.application;

import android.app.Application;

public class SilentApplication extends Application {

    private static SilentApplication mApplication;

    public static SilentApplication getAttendanceApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }
}
