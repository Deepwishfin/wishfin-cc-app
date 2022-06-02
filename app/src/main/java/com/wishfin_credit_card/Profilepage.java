package com.wishfin_credit_card;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Profilepage extends Activity implements View.OnClickListener {

    LinearLayout line1, line2, line3, line5;
    ImageView back;
    TextView logout;
    LinearLayout referfriendrelative, termsandconditionrelative, privacypolicyrelative, helpsupportrelative;
    Dialog dialog;
    SharedPreferences prefs;
    TextView heading, desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepage);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line5 = findViewById(R.id.line5);

        line1.setOnClickListener(this);
        line2.setOnClickListener(this);
        line3.setOnClickListener(this);
        line5.setOnClickListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(Profilepage.this);

        referfriendrelative = findViewById(R.id.referfriendrelative);
        termsandconditionrelative = findViewById(R.id.termsandconditionrelative);
        privacypolicyrelative = findViewById(R.id.privacypolicyrelative);
        helpsupportrelative = findViewById(R.id.helpsupportrelative);
        logout = findViewById(R.id.logout);

        dialog = new Dialog(Profilepage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        dialog.setContentView(R.layout.logout);
        back = findViewById(R.id.backbutton);
        heading = findViewById(R.id.heading);
        desc = findViewById(R.id.desc);

        String heading_text = SessionManager.get_firstname(prefs);
        heading.setText(heading_text);
        String number = "+91-" + SessionManager.get_mobile(prefs) + " | " + SessionManager.get_emailid(prefs);
        desc.setText(number);

        back.setOnClickListener(v -> {
            if (isThereInternetConnection()) {
                Intent intent5 = new Intent(Profilepage.this, Dashboard.class);
                startActivity(intent5);
                finish();
            } else {
                Objects.requireNonNull(dialog.getWindow()).setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                dialog.setContentView(R.layout.nointernet);
                dialog.show();
                TextView submit = dialog.findViewById(R.id.btnsubmit);
                submit.setOnClickListener(view -> {
                    if (isThereInternetConnection()) {
                        dialog.dismiss();
                        Intent intent5 = new Intent(Profilepage.this, Dashboard.class);
                        startActivity(intent5);
                        finish();

                    } else {
                        Toast.makeText(Profilepage.this, "Please check your internet", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        termsandconditionrelative.setOnClickListener(v -> {
            if (isThereInternetConnection()) {
                Intent intent1 = new Intent(Profilepage.this, Dynamicdisplaypage.class);
                intent1.putExtra("type", "terms-and-conditions");
                startActivity(intent1);
            } else {
                Objects.requireNonNull(dialog.getWindow()).setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                dialog.setContentView(R.layout.nointernet);
                dialog.show();
                TextView submit = dialog.findViewById(R.id.btnsubmit);
                submit.setOnClickListener(view -> {
                    if (isThereInternetConnection()) {
                        dialog.dismiss();
                        Intent intent1 = new Intent(Profilepage.this, Dynamicdisplaypage.class);
                        intent1.putExtra("type", "terms-and-conditions");
                        startActivity(intent1);

                    } else {
                        Toast.makeText(Profilepage.this, "Please check your internet", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        privacypolicyrelative.setOnClickListener(v -> {
            if (isThereInternetConnection()) {
                Intent intent1 = new Intent(Profilepage.this, Dynamicdisplaypage.class);
                intent1.putExtra("type", "privacy-policy");
                startActivity(intent1);
            } else {
                Objects.requireNonNull(dialog.getWindow()).setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                dialog.setContentView(R.layout.nointernet);
                dialog.show();
                TextView submit = dialog.findViewById(R.id.btnsubmit);
                submit.setOnClickListener(view -> {
                    if (isThereInternetConnection()) {
                        dialog.dismiss();
                        Intent intent1 = new Intent(Profilepage.this, Dynamicdisplaypage.class);
                        intent1.putExtra("type", "privacy-policy");
                        startActivity(intent1);

                    } else {
                        Toast.makeText(Profilepage.this, "Please check your internet", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

//        helpsupportrelative.setOnClickListener(v -> {
//
//            Intent intent1 = new Intent(Profilepage.this, Helpsupportdisplaypage.class);
//            startActivity(intent1);
//
//        });

        logout.setOnClickListener(v -> {

            Objects.requireNonNull(dialog.getWindow()).setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            dialog.setContentView(R.layout.logout);
            dialog.show();
            TextView cancle = dialog.findViewById(R.id.cancle);
            TextView ok = dialog.findViewById(R.id.ok);

            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SessionManager.dataclear(prefs);
                    dialog.dismiss();

                    Intent intent1 = new Intent(Profilepage.this, Loginpage.class);
                    startActivity(intent1);
                    finish();

                }
            });

        });

        referfriendrelative.setOnClickListener(v -> {

            String sharetxt = "Just checked my CIBIL Score on the Wishfin App for Free. It’s a must-have tool to maintain Financial Discipline. The best part is you can check your score monthly for free, track progress over time & get tips to improve your credit health. It’s privacy-protected & checking does not bring down your score.\n" +
                    "\n" +
                    "Thought you would find it useful. Download the Wishfin App & check your CIBIL Free at https://play.google.com/store/apps/details?id=com.mywish.wishfin.view";

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_TEXT, sharetxt);
            startActivity(Intent.createChooser(share, "Share"));

        });
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent5 = new Intent(Profilepage.this, Dashboard.class);
        startActivity(intent5);
        finish();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.line1:
                Intent intent2 = new Intent(Profilepage.this, Dashboard.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.line2:
                Intent intent3 = new Intent(Profilepage.this, CreditCardHistory.class);
                startActivity(intent3);
                break;
            case R.id.line3:
                Intent intent4 = new Intent(Profilepage.this, OfferlistingPge.class);
                startActivity(intent4);
                finish();
                break;

        }
    }

//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
//    }

}
