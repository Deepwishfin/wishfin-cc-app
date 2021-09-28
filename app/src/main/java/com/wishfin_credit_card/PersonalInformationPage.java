package com.wishfin_credit_card;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonalInformationPage extends Activity {

    EditText monthlyincome, annualincome;
    AutoCompleteTextView autocompletetext_companyname, autocompletetext_cityname;
    TextView continuebtn;
    KProgressHUD progressDialog;
    SharedPreferences prefs;
    RequestQueue queue;
    String str_companyname = "", str_cityname = "", strgender = "1";
    ArrayList<Gettersetterforall> companylist = new ArrayList<>();
    ArrayList<String> stringcompanylist = new ArrayList<>();
    ArrayList<Gettersetterforall> citylist = new ArrayList<>();
    ArrayList<String> stringcitylist = new ArrayList<>();
    ArrayAdapter<String> adapter;
    RadioButton maleradio, femaleradio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalinfopage);

        queue = Volley.newRequestQueue(PersonalInformationPage.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(PersonalInformationPage.this);

        progressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.dialogtext))
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        monthlyincome = findViewById(R.id.monthly_income);
        annualincome = findViewById(R.id.annualincome);
        autocompletetext_companyname = findViewById(R.id.autocompletetext_companyname);
        autocompletetext_cityname = findViewById(R.id.autocompletetext_cityname);
        continuebtn = findViewById(R.id.continuebtn);
        maleradio = findViewById(R.id.maleradio);
        femaleradio = findViewById(R.id.femaleradio);

        maleradio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                strgender = "1";
            }
        });

        femaleradio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                strgender = "2";
            }
        });


        autocompletetext_companyname.addTextChangedListener(textWatcher1);
        autocompletetext_cityname.addTextChangedListener(textWatcher2);

        get_city_listing();
        get_company_listing("");

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                create_lead();


            }
        });

    }

    public void create_lead() {

        final JSONObject json = new JSONObject();
        try {
            json.put("master_user_id", "");
            json.put("resource_pagename", "credit-cards");
            json.put("resource_source", "");
            json.put("resource_referal", "");
            json.put("resource_ip_address", "");
            json.put("resource_querystring", "");
            json.put("utm_source", "");
            json.put("utm_medium", "");
            json.put("utm_campaign", "");
            json.put("source", "");
            json.put("wish_id", "");
            json.put("annualincome", "" + annualincome.getText().toString());
            json.put("occupation", "1");
            json.put("companyname", "" + str_companyname);
            json.put("city", "" + str_cityname);
            json.put("fullname", "" + SessionManager.get_firstname(prefs));
            json.put("mobileno", "" + SessionManager.get_mobile(prefs));
            json.put("emailid", "" + SessionManager.get_emailid(prefs));
            json.put("dob", "" + SessionManager.get_dob(prefs));
            json.put("cc_holder", "1");
            json.put("creditcardbank", "others");
            json.put("accept", "1");
            json.put("submit", "Explore Credit Cards");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, getString(R.string.BASE_URL) + "/credit-card", json,
                response -> {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                            Intent intent = new Intent(PersonalInformationPage.this, EligibleCardsListing.class);
                            intent.putExtra("id", jsonObject1.getString("id"));
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {

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

    TextWatcher textWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {
                if (s.length() != 0) {
                    str_companyname = "";
                    if (s.length() > 1) {
                        if (isThereInternetConnection()) {
                            get_company_listing(s.toString());
                        } else {
                            Toast.makeText(PersonalInformationPage.this, "Please check your internet", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            } catch (Exception ignored) {

            }
        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };

    TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {
                if (s.length() != 0) {
                    str_cityname = "";
                }
            } catch (Exception ignored) {

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

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

    private void get_company_listing(String text) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.BASE_URL) + "/company-list-credit-card?startsWith=" + text;

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // response

                    try {
                        companylist = new ArrayList<>();
                        companylist.clear();
                        stringcompanylist = new ArrayList<>();
                        stringcompanylist.clear();

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            JSONArray jsonArray = (jsonObject.getJSONArray("result"));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectnew2 = jsonArray.getJSONObject(i);
                                Gettersetterforall pack = new Gettersetterforall();
                                pack.setName(objectnew2.getString("company_name"));

                                stringcompanylist.add(objectnew2.getString("company_name"));
                                companylist.add(pack);

                            }
//                            progressDialog.dismiss();
                            adapter = new ArrayAdapter<String>
                                    (this, android.R.layout.select_dialog_item, stringcompanylist);
                            autocompletetext_companyname.setThreshold(2);
                            autocompletetext_companyname.setAdapter(adapter);

                        }

                    } catch (Exception e) {
//                        progressDialog.dismiss();

                        e.printStackTrace();
                    }
                },
                error -> {
                    // TODO Auto-generated method stub
//                    progressDialog.dismiss();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Accept", "application/json");
                params.put("Authorization", bearer);

                return params;
            }
        };
        queue.add(getRequest);

    }

    private void get_city_listing() {

        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.BASE_URL) + "/city-list";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // response

                    try {
                        citylist = new ArrayList<>();
                        citylist.clear();
                        stringcitylist = new ArrayList<>();
                        stringcitylist.clear();

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            JSONArray jsonArray = (jsonObject.getJSONArray("result"));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectnew2 = jsonArray.getJSONObject(i);
                                Gettersetterforall pack = new Gettersetterforall();
                                pack.setName(objectnew2.getString("city"));
                                pack.setId(objectnew2.getString("id"));
                                pack.setDesc(objectnew2.getString("state"));
                                pack.setCard_state(objectnew2.getString("std_code"));

                                stringcitylist.add(objectnew2.getString("city"));
                                citylist.add(pack);

                            }
                            progressDialog.dismiss();

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (this, android.R.layout.select_dialog_item, stringcitylist);
                            autocompletetext_cityname.setThreshold(1);
                            autocompletetext_cityname.setAdapter(adapter);

                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {

                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();

                        e.printStackTrace();
                    }
                },
                error -> {
                    // TODO Auto-generated method stub
                    progressDialog.dismiss();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Accept", "application/json");
                params.put("Authorization", bearer);

                return params;
            }
        };
        queue.add(getRequest);

    }

}
