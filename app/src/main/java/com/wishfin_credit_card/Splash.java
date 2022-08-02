package com.wishfin_credit_card;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Splash extends Activity {

    SharedPreferences prefs;
    boolean permission = false;
    int PERMISSION_ALL = 1;
    Dialog dialog;
    WishFinAnalytics wishFinAnalytics;
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        prefs = PreferenceManager.getDefaultSharedPreferences(Splash.this);

        if (SessionManager.get_app_lang(prefs).equalsIgnoreCase("")) {
            myLocale = new Locale("en");
        } else {
            myLocale = new Locale("" + SessionManager.get_app_lang(prefs));
        }

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);

        dialog = new Dialog(Splash.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        wishFinAnalytics = new WishFinAnalytics(this);

        if (isThereInternetConnection()) {
            checkforpermission();
            getdevicetoken();
        } else {

            Objects.requireNonNull(dialog.getWindow()).setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            dialog.setContentView(R.layout.nointernet);
            dialog.show();
            TextView submit = dialog.findViewById(R.id.btnsubmit);
            submit.setOnClickListener(view -> {
                if (isThereInternetConnection()) {
                    dialog.dismiss();
                    checkforpermission();

                } else {
                    Toast.makeText(Splash.this, "Please check your internet", Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    private void getdevicetoken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        SessionManager.save_device_token(prefs, "Not Found");
//                        Toast.makeText(Splash.this, "This : is not found", Toast.LENGTH_SHORT).show();
                    } else {
                        // Get new FCM registration token
                        String token = task.getResult();
                        SessionManager.save_device_token(prefs, token);
//                        Toast.makeText(Splash.this, "This : " + token, Toast.LENGTH_SHORT).show();
                    }

                });
    }


    private void checkforpermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            if (!permission) {
                if (checkAndRequestPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.
                    checkForUpdate();
                    permission = true;
                }
            }
        } else {
            checkForUpdate();
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int permissionReadstorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionwritestorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();


        if (permissionReadstorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permissionwritestorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (permissionCheckInternet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), PERMISSION_ALL);
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
    }

    protected boolean isThereInternetConnection() {
        boolean isConnected;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            checkForUpdate();

        } else {
            finish();
        }
    }

    private void checkForUpdate() {
        new Handler().postDelayed(() -> {
            if (SessionManager.get_login(prefs).equalsIgnoreCase("True")) {

                wishFinAnalytics.openApp();

                Intent intent = new Intent(Splash.this, Dashboard.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }, 2000);
    }

}