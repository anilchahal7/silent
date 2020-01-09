package in.iceberg.silent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import in.iceberg.silent.R;
import in.iceberg.silent.database.AppRecordData;
import in.iceberg.silent.util.Util;

public class SettingPageActivity extends AppCompatActivity {

    private int dndStartHour, dndStartMinute;
    private int dndEndHour, dndEndMinute;

    private TextView dndTimeTextView, dndEndTimeTextView;

    public final int DEFAULT_START_DND_HOUR = 23;
    public final int DEFAULT_START_DND_MINUTE = 30;

    public final int DEFAULT_END_DND_HOUR = 6;
    public final int DEFAULT_END_DND_MINUTE = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        dndTimeTextView = findViewById(R.id.time_text_view);
        dndEndTimeTextView = findViewById(R.id.dnd_end_time_text_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.settings));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        getDNDTimings();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        return super.onOptionsItemSelected(item);
    }


    public void onDNDStartTimeClick(View view) {
        createdDNDStartDialog().show();
    }

    public void onDNDEndTimeClick(View view) {
        createdDNDEndDialog().show();
    }

    protected Dialog createdDNDStartDialog() {
        return new TimePickerDialog(this, dndStartTimePickerListener, dndStartHour, dndStartMinute, true);
    }

    protected Dialog createdDNDEndDialog() {
        return new TimePickerDialog(this, dndEndTimePickerListener, dndEndHour, dndEndMinute, true);
    }

    private TimePickerDialog.OnTimeSetListener dndStartTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            dndStartHour = hourOfDay;
            dndStartMinute = minutes;
            updateDNDTime(dndStartHour, dndStartMinute, false);
        }
    };

    private TimePickerDialog.OnTimeSetListener dndEndTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            dndEndHour = hourOfDay;
            dndEndMinute = minutes;
            updateDNDTime(dndEndHour, dndEndMinute, true);
        }
    };

    private void updateDNDTime(int hours, int min, boolean isEndTime) {
        String minutes;
        if (min < 10) {
            minutes = "0" + min;
        } else {
            minutes = String.valueOf(min);
        }
        String aTime = Util.getTime(hours, min);
        if (!isEndTime) {
            dndTimeTextView.setText(aTime);
            AppRecordData.setDNDStartHour(hours);
            AppRecordData.setDNDStartMinute(Integer.parseInt(minutes));
        } else {
            dndEndTimeTextView.setText(aTime);
            AppRecordData.setDNDENDHour(hours);
            AppRecordData.setDNDEndMinute(Integer.parseInt(minutes));
        }
    }

    private void getDNDTimings() {
        if (AppRecordData.getDNDStartHour() == -1) {
            dndStartHour = DEFAULT_START_DND_HOUR;
            AppRecordData.setDNDStartHour(dndStartHour);
        } else {
            dndStartHour = AppRecordData.getDNDStartHour();
        }
        if (AppRecordData.getDNDStartMinute() == -1) {
            dndStartMinute = DEFAULT_START_DND_MINUTE;
            AppRecordData.setDNDStartMinute(dndStartMinute);
        } else {
            dndStartMinute = AppRecordData.getDNDStartMinute();
        }
        updateDNDTime(dndStartHour, dndStartMinute, false);


        if (AppRecordData.getDNDEndHour() == -1) {
            dndEndHour = DEFAULT_END_DND_HOUR;
            AppRecordData.setDNDENDHour(dndEndHour);
        } else {
            dndEndHour = AppRecordData.getDNDEndHour();
        }
        if (AppRecordData.getDNDENDMinute() == -1) {
            dndEndMinute = DEFAULT_END_DND_MINUTE;
            AppRecordData.setDNDEndMinute(dndEndMinute);
        } else {
            dndEndMinute = AppRecordData.getDNDENDMinute();
        }

        updateDNDTime(dndEndHour, dndEndMinute, true);
    }
}
