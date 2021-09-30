package com.wishfin_credit_card;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class Dynamicdisplaypage extends Activity {

    RelativeLayout back;
    String detailid = "";
    RequestQueue queue;
    KProgressHUD progressDialog;
    SharedPreferences prefs;
    TextView heading, desc;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dynamicdisplay);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                detailid = "";
            } else {
                detailid = extras.getString("type");
            }
        } else {
            detailid = (String) savedInstanceState.getSerializable("type");
        }

        queue = Volley.newRequestQueue(Dynamicdisplaypage.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(Dynamicdisplaypage.this);

        progressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.dialogtext))
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

//        progressDialog = new ProgressDialog(Dynamicdisplaypage.this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setTitle("Please wait");
//        progressDialog.setCancelable(false);

        back = findViewById(R.id.backbutton);
        heading = findViewById(R.id.heading);
        desc = findViewById(R.id.description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            desc.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        if (detailid != null) {
            if (detailid.equalsIgnoreCase("terms-and-conditions")) {
                heading.setText(getString(R.string.terms_and_condition));
            } else if (detailid.equalsIgnoreCase("privacy-policy")) {
                heading.setText(getString(R.string.privacy_policy));
            } else if (detailid.equalsIgnoreCase("help")) {
                heading.setText(getString(R.string.help));
            } else if (detailid.equalsIgnoreCase("referral_terms")) {
                heading.setText(getString(R.string.terms_and_condition));
            }
        } else {
            detailid = "cibil-terms-and-conditions";
            heading.setText(getString(R.string.terms_and_condition));

        }

        back.setOnClickListener(v -> finish());

        get_cibil_credit_factors();

    }

    public void get_cibil_credit_factors() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.BASE_URL) + "/page-setting-detail?page_name=" + detailid;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // response

                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            JSONObject jsonObjectresult1 = new JSONObject(jsonObject.getJSONObject("result").toString());

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                desc.setText(Html.fromHtml(jsonObjectresult1.getString("content").trim(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                desc.setText(Html.fromHtml(jsonObjectresult1.getString("content").trim()));
                            }

//                            desc.setText(Html.fromHtml(jsonObjectresult1.getString("content").trim().replaceAll("\\<[^>]*>", "")));
                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    } catch (Exception e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        e.printStackTrace();
                    }
                },
                error -> {
                    // TODO Auto-generated method stub
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
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
