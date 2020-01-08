package in.iceberg.silent.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import in.iceberg.silent.R;
import in.iceberg.silent.database.AppRecordData;
import in.iceberg.silent.util.TextUtils;

public class MainActivity extends AppCompatActivity {

    private int onDoNotDisturb = 1000;
    private AudioManager audioManager;
    private Switch homeAddressSwitch;
    private Switch officeAddressSwitch;
    private TextView homeAddressTextView;
    private TextView officeAddressTextView;

    private Double latitude = 0.00;
    private Double longitude = 0.00;
    private String savedHomeAddress, savedOfficeAddress;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savedHomeAddress = AppRecordData.getHomeAddress();
        savedOfficeAddress = AppRecordData.getOfficeAddress();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requestMultiplePermissions();
        checkLocation();

        homeAddressSwitch = findViewById(R.id.home_address_simple_switch);
        officeAddressSwitch = findViewById(R.id.office_address_simple_switch);
        homeAddressTextView = findViewById(R.id.home_address_text);
        officeAddressTextView = findViewById(R.id.office_address_text);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult(intent, onDoNotDisturb);
        }
        if (TextUtils.isNotNullOrEmpty(savedHomeAddress)) {
            homeAddressTextView.setText(savedHomeAddress);
        }
        if (TextUtils.isNotNullOrEmpty(savedOfficeAddress)) {
            officeAddressTextView.setText(savedOfficeAddress);
        }
        homeAddressSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            homeAddressSwitch.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            setRingerMode(b);
        });
        officeAddressSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            officeAddressSwitch.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            setRingerMode(b);
        });
    }

    private void setRingerMode(boolean status) {
        if (status && audioManager != null) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else if (audioManager != null) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == onDoNotDisturb ) {
            setRingerMode(false);
        }
    }

    private void requestMultiplePermissions(){
        Dexter.withActivity(this)
            .withPermissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        onConnectionSet();
                    }
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        openSettingsDialog();
                    }
                }
                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).
            withErrorListener(error -> Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show())
            .onSameThread()
            .check();
    }

    private void onConnectionSet() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (fusedLocationProviderClient != null) {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(location -> {
                if(location == null) {
                    Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
                } else {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            });
        }
    }

    private void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void checkLocation() {
        if(!isLocationEnabled()) {
            showAlert();
        }
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", (paramDialogInterface, paramInt) -> {

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                })
                .setNegativeButton("Cancel", (paramDialogInterface, paramInt) -> {

                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return false;
    }

    public void onHomeAddressClick(View view) {
        if (TextUtils.isNotNullOrEmpty(savedHomeAddress)) {
            String header = "Home Address is already saved, Do you want to update the address?";
            openDialog(header, true);
        } else {
            setAddress(latitude, longitude, true);
        }
    }

    public void onOfficeAddressClick(View view) {
        if (TextUtils.isNotNullOrEmpty(savedOfficeAddress)) {
            String header = "Office Address is already saved, Do you want to update the address?";
            openDialog(header, false);
        } else {
            setAddress(latitude, longitude, false);
        }
    }

    private void setAddress(Double latitude, Double longitude, boolean isHomeAddress) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                if (isHomeAddress) {
                    AppRecordData.setHomeAddress(address);
                    homeAddressTextView.setText(address);
                } else {
                    AppRecordData.setOfficeAddress(address);
                    officeAddressTextView.setText(address);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDialog(String header, boolean isHomeAddress) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(header);
        alertDialogBuilder.setPositiveButton("Yes",
            (arg0, arg1) -> {
                if (isHomeAddress) {
                    setAddress(latitude, longitude, true);
                } else {
                    setAddress(latitude, longitude, false);
                }
        });
        alertDialogBuilder.setNegativeButton("No", (dialog, which) ->
                dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
