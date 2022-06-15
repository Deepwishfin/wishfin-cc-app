package com.wishfin_credit_card;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Thankyoupage extends Activity {

    String strcardname = "", bank_code = "";
    TextView cardname, instantapply, joiningfees, annualfees;
    ImageView imageView;
    KProgressHUD progressDialog;
    SharedPreferences prefs;
    RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyoupage);

        queue = Volley.newRequestQueue(Thankyoupage.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(Thankyoupage.this);

        progressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.dialogtext))
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                strcardname = "";
                bank_code = "";

            } else {
                strcardname = extras.getString("cardname");
                bank_code = extras.getString("bank_code");

            }
        } else {
            strcardname = (String) savedInstanceState.getSerializable("cardname");
            bank_code = (String) savedInstanceState.getSerializable("bank_code");

        }

        imageView = findViewById(R.id.imageView);
        cardname = findViewById(R.id.textView);
        instantapply = findViewById(R.id.instantapply);
        joiningfees = findViewById(R.id.joiningfees);
        annualfees = findViewById(R.id.annualfees);

        progressDialog.show();
        get_card_list();

        cardname.setText(strcardname);

        instantapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Thankyoupage.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void get_card_list() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.BASE_URL) + "/v1/credit-card-all-quotes?bankCode=" + bank_code;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // response
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                            JSONArray jsonArray = (jsonObject1.getJSONArray("bank-quote"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectnew2 = jsonArray.getJSONObject(i);
                                Gettersetterforall pack = new Gettersetterforall();

                                if (objectnew2.getString("name").equalsIgnoreCase(strcardname)) {
                                    cardname.setText(strcardname);
                                    Picasso.get()
                                            .load(objectnew2.getString("image_path"))
                                            .into(imageView);

                                    try {
                                        JSONArray feesjaonarray = objectnew2.getJSONArray("fee");
                                        for (int j = 0; j < feesjaonarray.length(); j++) {
                                            JSONObject object = feesjaonarray.getJSONObject(j);
                                            try {
                                                if (object.getString("title").contains("Annual")) {
                                                    annualfees.setText(object.getString("value"));
                                                }
                                            } catch (Exception e) {
                                                annualfees.setText("NA");
                                            }
                                            try {
                                                if (object.getString("title").contains("Joining")) {
                                                    joiningfees.setText(object.getString("value"));
                                                }
                                            } catch (Exception e) {
                                                joiningfees.setText("NA");
                                            }
                                        }
                                    } catch (Exception e) {
                                        annualfees.setText("NA");
                                        joiningfees.setText("NA");
                                    }


                                }


                            }

                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(Thankyoupage.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        e.printStackTrace();
                    }


                },
                error -> {

                    try {
                        int statusCode = error.networkResponse.statusCode;
                        if (statusCode == 421) {
                        }
                        error.printStackTrace();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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
        queue.add(getRequest);

    }
}
