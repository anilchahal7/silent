package in.iceberg.silent.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.iceberg.silent.R;

public final class ToastHelper {

    private static final int TOAST_LONG = 1;
    public static final int TOAST_SHORT = 2;

    private ToastHelper() {
        throw new UnsupportedOperationException("Cannot instantiate static holder.");
    }

    public static void showToast(Context context, String message, int duration) {
        if (context == null || TextUtils.isNullOrEmpty(message)) {
            return;
        }
        getCustomToast(context, message, duration).show();
    }


    private static Toast getCustomToast(Context context, String message, int duration) {
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundResource(R.drawable.toast_background);

        TextView tv = new TextView(context);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(14);
        tv.setPadding(getPixels(context, 20), getPixels(context, 8),
                getPixels(context, 20), getPixels(context, 8));

        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setText(message);
        layout.addView(tv);

        Toast toast = new Toast(context);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM, 0, getPixels(context, 70));
        toast.setDuration(duration == TOAST_LONG ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        return toast;
    }

    private static int getPixels(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dp);
    }
}
