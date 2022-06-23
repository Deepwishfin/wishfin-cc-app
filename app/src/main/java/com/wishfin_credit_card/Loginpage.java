package com.wishfin_credit_card;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Loginpage extends Activity implements SMSReceiver.OTPReceiveListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView signupone, signupthree, resentotp, lastmobiletext, mobilenumberhead;
    LinearLayout linearone, linearthree;
    ImageView backbutton;
    EditText mobilenumber, otpone, otptwo, otpthree, otpfour;
    KProgressHUD progressDialog;
    String strmobilenumber = null, secret_key = "";
    RequestQueue queue;
    SharedPreferences prefs;
    int page = 1;
    private SMSReceiver smsReceiver;
    private boolean broadcast = true;
    private int RESOLVE_HINT = 2;
    GoogleApiClient mGoogleApiClient;
    String IPaddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loginpage);

        backbutton = findViewById(R.id.backbutton);
        signupone = findViewById(R.id.signupone);
        signupthree = findViewById(R.id.signupthree);

        linearone = findViewById(R.id.linearone);
        linearthree = findViewById(R.id.linearthree);

        mobilenumber = findViewById(R.id.mobilenumber);
        mobilenumberhead = findViewById(R.id.mobilenumberhead);
        otpone = findViewById(R.id.otpone);
        otptwo = findViewById(R.id.otptwo);
        otpthree = findViewById(R.id.otpthree);
        otpfour = findViewById(R.id.otpfour);
        resentotp = findViewById(R.id.resentotp);
        lastmobiletext = findViewById(R.id.lastmobiletext);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                strmobilenumber = null;
            } else {
                strmobilenumber = extras.getString("mobile");
            }
        } else {
            strmobilenumber = (String) savedInstanceState.getSerializable("mobile");
        }

        progressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.dialogtext))
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        NetwordDetect();

        otpone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otpone.getText().length() == 0) {
                        otpone.requestFocus();
                    }
                }
                return false;
            }
        });

        otptwo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otptwo.getText().length() == 0) {
                        otpone.requestFocus();
                    }
                }
                return false;
            }
        });

        otpthree.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otpthree.getText().length() == 0) {
                        otptwo.requestFocus();
                    }
                }
                return false;
            }
        });

        otpfour.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otpfour.getText().length() == 0) {
                        otpthree.requestFocus();
                    }
                }
                return false;
            }
        });

        mobilenumber.addTextChangedListener(new MyTextWatcher(mobilenumber));
        otpone.addTextChangedListener(new MyTextWatcher(otpone));
        otptwo.addTextChangedListener(new MyTextWatcher(otptwo));
        otpthree.addTextChangedListener(new MyTextWatcher(otpthree));
        otpfour.addTextChangedListener(new MyTextWatcher(otpfour));

        queue = Volley.newRequestQueue(Loginpage.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(Loginpage.this);


        getaouth();

        getHintPhoneNumber();

        if (strmobilenumber != null) {
            mobilenumber.setText(strmobilenumber);
            String lastnumber = strmobilenumber.substring(strmobilenumber.length() - 4);

            lastmobiletext.setText("We have sent an OTP on ******" + lastnumber);

            progressDialog.show();
            get_otp_data();

        }

        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(Loginpage.this);
        Constants.hashkey = appSignatureHashHelper.getAppSignatures().get(0);

        resentotp.setOnClickListener(v -> {
            progressDialog.show();
            get_otp_data();

        });

        backbutton.setOnClickListener(v -> {

            if (page == 1) {

                Intent intent = new Intent(Loginpage.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else if (page == 2) {

                linearone.setVisibility(View.VISIBLE);
                linearthree.setVisibility(View.GONE);
                otpone.setText("");
                otptwo.setText("");
                otpthree.setText("");
                otpfour.setText("");
                page--;

            }
        });

        signupone.setOnClickListener(v -> {

            submitFormone();
        });

        signupthree.setOnClickListener(v -> {

            String otpstring = otpone.getText().toString() + "" + otptwo.getText().toString() + "" + otpthree.getText().toString() + "" + otpfour.getText().toString();

            if (isThereInternetConnection()) {
                long currenttimestam = System.currentTimeMillis() / 1000;
                if (Constants.apihittime == 0) {
                    if (otpstring.length() != 4) {
                        Toast.makeText(Loginpage.this, "Please Enter OTP", Toast.LENGTH_LONG).show();

                    } else {
                        login_api("Button clicked");
                    }
                } else {
                    if ((currenttimestam - Constants.apihittime) > 160) {
                        if (otpstring.length() != 4) {
                            Toast.makeText(Loginpage.this, "Please Enter OTP", Toast.LENGTH_LONG).show();

                        } else {
                            login_api("Button clicked");
                        }
                    } else {

                        try {
                            JSONObject jsonObject = new JSONObject(Constants.apiresponse);
                            if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                                JSONObject jsonObjectresult = new JSONObject(jsonObject.getJSONObject("result").toString());

                                String mobile = jsonObjectresult.getString("mobile_number");

                                SessionManager.save_firstname(prefs, jsonObjectresult.getString("first_name"));
                                SessionManager.save_mname(prefs, jsonObjectresult.getString("middle_name"));
                                SessionManager.save_lastname(prefs, jsonObjectresult.getString("last_name"));
                                SessionManager.save_dob(prefs, jsonObjectresult.getString("date_of_birth"));
                                SessionManager.save_pan(prefs, jsonObjectresult.getString("pancard"));
                                SessionManager.save_mobile(prefs, jsonObjectresult.getString("mobile_number"));
                                SessionManager.save_emailid(prefs, jsonObjectresult.getString("email_id"));
                                SessionManager.save_masteruserid(prefs, jsonObjectresult.getString("master_user_id"));
                                SessionManager.save_mfuserid(prefs, jsonObjectresult.getString("mf_user_id"));
                                SessionManager.save_login(prefs, "True");
                                SessionManager.save_logintype(prefs, "Login");
                                SessionManager.save_app_time(prefs, "0");

                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Intent intent = new Intent(Loginpage.this, Dashboard.class);
                                intent.putExtra("source", "login");
                                intent.putExtra("ipaddress", IPaddress);
                                startActivity(intent);
                                finish();
                                Toast.makeText(Loginpage.this, "Welcome Back " + SessionManager.get_firstname(prefs), Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception ignored) {

                        }
                    }
                }

            } else {
                Toast.makeText(Loginpage.this, "Please check your internet", Toast.LENGTH_LONG).show();

            }

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

    public void getaouth() {
        final JSONObject json = new JSONObject();
        try {
            json.put("username", BuildConfig.oAuthUserName);
            json.put("password", BuildConfig.oAuthPassword);
            json.put("client_id", BuildConfig.oAuthClientId);
            json.put("grant_type", BuildConfig.oAuthGrantType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, getString(R.string.BASE_URL) + "/oauth", json,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        SessionManager.save_access_token(prefs, String.valueOf(jsonObject.getString("access_token")));
                        SessionManager.save_expirein(prefs, String.valueOf(jsonObject.getString("expires_in")));
                        SessionManager.save_token_type(prefs, String.valueOf(jsonObject.getString("token_type")));
                        SessionManager.save_refresh_token(prefs, String.valueOf(jsonObject.getString("refresh_token")));

//                        Toast.makeText(Loginpage.this, "" + SessionManager.get_access_token(prefs), Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    public void get_otp_data() {

        final JSONObject json = new JSONObject();

        try {
            json.put("email_mobile", "" + mobilenumber.getText().toString());
            json.put("type", "signin_app");
            json.put("hash", "" + Constants.hashkey);
            json.put("device_type", "Android");
            json.put("app_version", "" + SessionManager.get_appversion(prefs));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, getString(R.string.BASE_URL) + "/v1/get-otp-data", json,
                response -> {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    try {
                        timer();
                        JSONObject jsonObject = new JSONObject(response.toString());

                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            JSONObject jsonObjectresult = new JSONObject(jsonObject.getJSONObject("result").toString());
                            secret_key = jsonObjectresult.getString("secret_key");
                            SessionManager.save_secret_key(prefs, jsonObjectresult.getString("secret_key"));
                            linearone.setVisibility(View.GONE);
                            linearthree.setVisibility(View.VISIBLE);
                            page = 2;
                            if (!jsonObject.getString("message").equalsIgnoreCase("ok")) {
                                Toast.makeText(Loginpage.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } else if (jsonObject.getString("message").equalsIgnoreCase("ok")) {
                                Toast.makeText(Loginpage.this, "Resent OTP", Toast.LENGTH_LONG).show();

                            }
                            startSMSListener();

                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {

                            if (jsonObject.getString("message").equalsIgnoreCase("No Record found")) {
                                Constants.apihittime = 0;
                                Toast.makeText(Loginpage.this, "" + jsonObject.getString("message") + ". kindly signup first", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Loginpage.this, Signuppage.class);
                                intent.putExtra("mobile", mobilenumber.getText().toString());
                                startActivity(intent);
                                finish();

                            } else if (jsonObject.getString("message").equalsIgnoreCase("ok")) {
                                JSONObject jsonObjectresult = new JSONObject(jsonObject.getJSONObject("result").toString());
                                if (jsonObjectresult.getString("otp_status").equalsIgnoreCase("Same OTP Send")
                                        || jsonObjectresult.getString("otp_status").contains("OTP Already Sent")) {
                                    secret_key = jsonObjectresult.getString("secret_key");
                                    SessionManager.save_secret_key(prefs, jsonObjectresult.getString("secret_key"));
                                    linearone.setVisibility(View.GONE);
                                    linearthree.setVisibility(View.VISIBLE);
                                    page = 2;
                                    startSMSListener();
                                }
                            }
                        }

                    } catch (Exception e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        e.printStackTrace();
                    }

                }, error -> {
            error.printStackTrace();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
                header.put("Content-Type", "application/json; charset=utf-8");
                header.put("Accept", "application/json");
                header.put("Authorization", bearer);

                return header;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    public void login_api(String enter_otp_type) {

        final JSONObject json = new JSONObject();

        try {
            String otpstring = otpone.getText().toString() + "" + otptwo.getText().toString() + "" + otpthree.getText().toString() + "" + otpfour.getText().toString();
            json.put("email_mobile", "" + mobilenumber.getText().toString());
            json.put("otp_password", "" + otpstring);
            json.put("type", "signin_app");
            json.put("case", "MO");
            if (secret_key.equalsIgnoreCase("")) {
                json.put("secret_key", "" + SessionManager.get_secret_key(prefs));
            } else {
                json.put("secret_key", secret_key);
            }
            json.put("device_type", "Android");
            json.put("app_version", "" + SessionManager.get_appversion(prefs));
            json.put("enter_otp_type", enter_otp_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, getString(R.string.BASE_URL) + "/v1/login", json,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        Constants.apiresponse = response.toString();

                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            Constants.apihittime = System.currentTimeMillis() / 1000;

                            JSONObject jsonObjectresult = new JSONObject(jsonObject.getJSONObject("result").toString());

                            String mobile = jsonObjectresult.getString("mobile_number");

                            SessionManager.save_firstname(prefs, jsonObjectresult.getString("first_name"));
                            SessionManager.save_mname(prefs, jsonObjectresult.getString("middle_name"));
                            SessionManager.save_lastname(prefs, jsonObjectresult.getString("last_name"));
                            SessionManager.save_dob(prefs, jsonObjectresult.getString("date_of_birth"));
                            SessionManager.save_pan(prefs, jsonObjectresult.getString("pancard"));
                            SessionManager.save_mobile(prefs, jsonObjectresult.getString("mobile_number"));
                            SessionManager.save_emailid(prefs, jsonObjectresult.getString("email_id"));
                            SessionManager.save_masteruserid(prefs, jsonObjectresult.getString("master_user_id"));
                            SessionManager.save_mfuserid(prefs, jsonObjectresult.getString("mf_user_id"));
                            SessionManager.save_login(prefs, "True");
                            SessionManager.save_logintype(prefs, "Login");
                            SessionManager.save_app_time(prefs, "0");


                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent(Loginpage.this, Dashboard.class);
                            intent.putExtra("source", "login");
                            intent.putExtra("ipaddress", IPaddress);
                            startActivity(intent);
                            finish();
                            if (smsReceiver != null) {
                                LocalBroadcastManager.getInstance(Loginpage.this).unregisterReceiver(smsReceiver);
                            }

                            Toast.makeText(Loginpage.this, "Welcome Back " + SessionManager.get_firstname(prefs), Toast.LENGTH_LONG).show();

                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(Loginpage.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        e.printStackTrace();
                    }

                }, error -> {
            error.printStackTrace();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
                header.put("Content-Type", "application/json; charset=utf-8");
                header.put("Accept", "application/json");
                header.put("Authorization", bearer);

                return header;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.mobilenumber:
                    validateNumber();
                    break;
                case R.id.otpone:
                    if (otpone.getText().toString().equalsIgnoreCase("")) {
                        otpone.requestFocus();
                    } else {
                        otptwo.requestFocus();
                    }
                    break;
                case R.id.otptwo:
                    if (otptwo.getText().toString().equalsIgnoreCase("")) {
                        otpone.requestFocus();
                    } else {
                        otpthree.requestFocus();
                    }
                    break;
                case R.id.otpthree:
                    if (otpthree.getText().toString().equalsIgnoreCase("")) {
                        otptwo.requestFocus();
                    } else {
                        otpfour.requestFocus();
                    }
                    break;
                case R.id.otpfour:
                    if (otpfour.getText().toString().equalsIgnoreCase("")) {
                        otpthree.requestFocus();
                    }
//                    login_api();
                    break;

            }
        }
    }

    private boolean validateNumber() {
        if (!mobilenumber.getText().toString().trim().matches("[5-9][0-9]{9}") || mobilenumber.getText().length() != 10) {
            mobilenumberhead.setTextColor(Color.parseColor("#FF0000"));
            mobilenumberhead.setText("Enter 10 Digits Mobile Number");
//            Toast.makeText(getApplicationContext(),"Enter 10 Digits Mobile Number",Toast.LENGTH_SHORT).show();
            requestFocus(mobilenumber);
            return false;
        } else {
            mobilenumberhead.setText("Mobile Number");
            mobilenumberhead.setTextColor(Color.parseColor("#304258"));
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void submitFormone() {
        if (!validateNumber()) {
            return;
        }

        if (isThereInternetConnection()) {
            check_email_exist();
        } else {
            Toast.makeText(Loginpage.this, "Please check your internet", Toast.LENGTH_LONG).show();

        }

    }

    private void check_email_exist() {

        final JSONObject json = new JSONObject();

        try {
            json.put("mobile_number", "" + mobilenumber.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, getString(R.string.BASE_URL) + "/check-mobile-email-exist", json,
                response -> {
                    try {

                        JSONObject jsonObject = new JSONObject(response.toString());

                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            String lastnumber = mobilenumber.getText().toString().substring(mobilenumber.getText().toString().length() - 4);

                            lastmobiletext.setText("We have sent an OTP on ******" + lastnumber);

                            hideKeyboard(Loginpage.this);
                            get_otp_data();

                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {

                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(Loginpage.this, "No record found" + ", kindly signup first", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Loginpage.this, Signuppage.class);
                            intent.putExtra("mobile", mobilenumber.getText().toString());
                            startActivity(intent);
                            finish();

                        }

                    } catch (Exception e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        e.printStackTrace();
                    }

                }, error -> {
            error.printStackTrace();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
                header.put("Content-Type", "application/json; charset=utf-8");
                header.put("Accept", "application/json");
                header.put("Authorization", bearer);

                return header;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);

    }

    private void startSMSListener() {
        try {
            smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            Loginpage.this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(Loginpage.this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // API successfully started
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail to start API
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void timer() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                resentotp.setClickable(false);
                String resend_otp_text = "Resend OTP in: " + millisUntilFinished / 1000 + " sec";
                resentotp.setText(resend_otp_text);
                backbutton.setVisibility(View.GONE);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                resentotp.setText(getString(R.string.resend_otp));
                resentotp.setClickable(true);
                backbutton.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onOTPReceived(String otp) {
        if (broadcast) {
            broadcast = false;
            otpone.setText("");
            otptwo.setText("");
            otpthree.setText("");
            otpfour.setText("");
            otpone.setText(String.valueOf(otp.charAt(0)));
            otptwo.setText(String.valueOf(otp.charAt(1)));
            otpthree.setText(String.valueOf(otp.charAt(2)));
            otpfour.setText(String.valueOf(otp.charAt(3)));
            requestFocus(otpfour);
            unregisterReceiver(smsReceiver);
            if (smsReceiver != null) {
                LocalBroadcastManager.getInstance(Loginpage.this).unregisterReceiver(smsReceiver);
            }

            login_api("Auto populate");
        }
    }

    @Override
    public void onOTPTimeOut() {

    }

    @Override
    public void onOTPReceivedError(String error) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(Loginpage.this).unregisterReceiver(smsReceiver);
        }
    }


    public void getHintPhoneNumber() {
        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result if we want hint number
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId();  <-- will need to process phone number string
                    mobilenumber.setText(credential.getId().substring(3));
                }

            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void NetwordDetect() {

        boolean WIFI = false;

        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = new NetworkInfo[0];
        if (CM != null) {
            networkInfo = CM.getAllNetworkInfo();
        }

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if (WIFI) {
            IPaddress = GetDeviceipWiFiData();

        }

        if (MOBILE) {

            IPaddress = GetDeviceipMobileData();

        }

    }

    public String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return Formatter.formatIpAddress(inetAddress.hashCode());
                    }
                }
            }
        } catch (Exception ex) {

        }
        return null;
    }

    public String GetDeviceipWiFiData() {

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        @SuppressWarnings("deprecation")

        String ip = null;
        if (wm != null) {
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        }

        return ip;

    }

}
