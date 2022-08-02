package com.wishfin_credit_card;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.vorlonsoft.android.rate.AppRate;
import com.vorlonsoft.android.rate.StoreType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Dashboard extends Activity implements View.OnClickListener {

    RecyclerView card_list,article_list;
    ArrayList<Gettersetterforall> list1 = new ArrayList<>();
    KProgressHUD progressDialog;
    SharedPreferences prefs;
    RequestQueue queue;
    String logintype = "", IPaddress = "";
    TextView signupone, exploremore, name, goodmorning, bar5;
    LinearLayout line1, line2, line3, line5, personalised;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout bestChoice, bestreward, lifetimefree, besttravel, bestfuel, bestcashback;
    WishFinAnalytics wishFinAnalytics;
    Share_Adapter radio_question_list_adapter;
    //    private ReviewManager reviewManager;
//    ReviewInfo reviewInfo;
//    ReviewManager manager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dashboard);

//        reviewManager = ReviewManagerFactory.create(this);
//        manager = ReviewManagerFactory.create(this);


        wishFinAnalytics = new WishFinAnalytics(this);

        queue = Volley.newRequestQueue(Dashboard.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);

        // callback listener.
        AppRate.with(this)
                .setStoreType(StoreType.GOOGLEPLAY) //default is GOOGLEPLAY (Google Play), other options are
                //           AMAZON (Amazon Appstore) and
                //           SAMSUNG (Samsung Galaxy Apps)
                .setInstallDays((byte) 0) // default 10, 0 means install day
                .setLaunchTimes((byte) 3) // default 10
                .setRemindInterval((byte) 2) // default 1
                .setRemindLaunchTimes((byte) 2) // default 1 (each launch)
                .setShowLaterButton(true) // default true
                .setDebug(false)
                .setTextRateNow(getString(R.string.rateus))
                .setTitle(getString(R.string.hello)+" " + SessionManager.get_firstname(prefs))
                .setMessage(getString(R.string.rateuspopup))// default false
                //Java 8+: .setOnClickButtonListener(which -> Log.d(MainActivity.class.getName(), Byte.toString(which)))
                .setOnClickButtonListener(which -> Log.d(MainActivity.class.getName(), Byte.toString(which)))
                .monitor();

        if (AppRate.with(this).getStoreType() == StoreType.GOOGLEPLAY) {
            //Check that Google Play is available
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) {
                // Show a dialog if meets conditions
                AppRate.showRateDialogIfMeetsConditions(this);
            }
        } else {
            // Show a dialog if meets conditions
            AppRate.showRateDialogIfMeetsConditions(this);

        }

        progressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.dialogtext))
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        progressDialog.show();
        getaouth();
        get_article_list();
//        get_card_list();

        if (SessionManager.get_lastccapplydate(prefs).equalsIgnoreCase("")) {
            SessionManager.save_lastccapplydate(prefs, "01-01-2000");
        }
        long differenceDates = 0;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            Date date1, date2;
            Date date3;
            date1 = new Date();
            String todaydate = dates.format(date1);
            date2 = dates.parse(todaydate);
            date3 = dates.parse(SessionManager.get_lastccapplydate(prefs));
            long difference = 0;
            if (date2 != null) {
                if (date3 != null) {
                    difference = Math.abs(date2.getTime() - date3.getTime());
                }
            }
            differenceDates = difference / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            System.out.println("" + e);

        }
        if (differenceDates > 30) {

            SessionManager.save_lead_id(prefs, "");
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                logintype = "";
                IPaddress = "";
            } else {
                logintype = extras.getString("source");
                IPaddress = extras.getString("ipaddress");
            }
        } else {
            logintype = (String) savedInstanceState.getSerializable("source");
            IPaddress = (String) savedInstanceState.getSerializable("ipaddress");
        }

//        if (logintype != null) {
//            if (logintype.equalsIgnoreCase("signup")) {
//
//                if (progressDialog != null) {
//                    progressDialog.show();
//                }
//                get_cibil_fulfill_order("normal");
//
//            } else if (logintype.equalsIgnoreCase("insideapp")) {
//
//                get_cibil_credit_factors();
//
//            } else {
//                if (progressDialog != null) {
//                    progressDialog.show();
//                }
//                update_last_login();
//            }
//        } else {
//            if (progressDialog != null) {
//                progressDialog.show();
//            }
//            get_cibil_credit_factors();
//        }

        article_list = findViewById(R.id.article_list);
        card_list = findViewById(R.id.card_list);
        signupone = findViewById(R.id.signupone);
        exploremore = findViewById(R.id.exploremore);
        bestChoice = findViewById(R.id.bestChoice);
        bestreward = findViewById(R.id.bestreward);
        lifetimefree = findViewById(R.id.lifetimefree);
        besttravel = findViewById(R.id.besttravel);
        bestfuel = findViewById(R.id.bestfuel);
        bestcashback = findViewById(R.id.bestcashback);
        name = findViewById(R.id.name);
        goodmorning = findViewById(R.id.goodmorning);
        personalised = findViewById(R.id.personalised);
        bar5 = findViewById(R.id.bar5);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(Dashboard.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Dashboard.this) {
                    private static final float SPEED = 4000f;

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };

        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
//        card_list.addItemDecoration(new DividerItemDecoration(Dashboard.this, DividerItemDecoration.HORIZONTAL));
        article_list.setLayoutManager(layoutManager1);

        personalised.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, PersonalisedCards.class);
            intent.putExtra("type", "ExploreAll");
            startActivity(intent);
        });

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String hellotext = SessionManager.get_firstname(prefs);

        if (timeOfDay < 12) {
            name.setText(getString(R.string.good_morning_text)+" " + hellotext);
//            goodmorning.setText(getString(R.string.good_morning));
        } else if (timeOfDay < 16) {
            name.setText(getString(R.string.good_afternoon_text)+" " + hellotext);
//            goodmorning.setText(getString(R.string.good_morning));
        } else {
            name.setText(getString(R.string.good_evening_text)+" " + hellotext);
//            goodmorning.setText(getString(R.string.good_morning));
        }

        exploremore.setOnClickListener(this);
        bestChoice.setOnClickListener(this);
        bestreward.setOnClickListener(this);
        lifetimefree.setOnClickListener(this);
        besttravel.setOnClickListener(this);
        bestfuel.setOnClickListener(this);
        bestcashback.setOnClickListener(this);

        signupone.setOnClickListener(view -> {

//                showRateApp();

//                if (SessionManager.get_lead_id(prefs).equalsIgnoreCase("")) {
//                    Intent intent = new Intent(Dashboard.this, PersonalInformationPage.class);
//                    startActivity(intent);
//                } else {
            Intent intent = new Intent(Dashboard.this, ExploreCreditCard.class);
            intent.putExtra("type", "ExploreAll");
            startActivity(intent);
//                }
        });

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line5 = findViewById(R.id.line5);

        line1.setOnClickListener(this);
        line2.setOnClickListener(this);
        line3.setOnClickListener(this);
        line5.setOnClickListener(this);

//        LinearLayoutManager layoutManager1 = new LinearLayoutManager(Dashboard.this) {
//            @Override
//            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Dashboard.this) {
//                    private static final float SPEED = 4000f;
//
//                    @Override
//                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//                        return SPEED / displayMetrics.densityDpi;
//                    }
//                };
//                smoothScroller.setTargetPosition(position);
//                startSmoothScroll(smoothScroller);
//            }
//
//        };
//
//        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
//        card_list.addItemDecoration(new DividerItemDecoration(Dashboard.this, DividerItemDecoration.VERTICAL));
//        card_list.setLayoutManager(layoutManager1);


    }

        public void get_article_list() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = BuildConfig.BASE_URL + "/blog-post-details";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // response

                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        list1 = new ArrayList<>();
                        list1.clear();

                        if (jsonObject.getString("status").equalsIgnoreCase("Success")) {

                            JSONArray jsonArray = (jsonObject.getJSONArray("result"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectnew2 = jsonArray.getJSONObject(i);
                                Gettersetterforall pack = new Gettersetterforall();
                                pack.setBank_code(objectnew2.getString("content"));
                                pack.setName(objectnew2.getString("title"));
                                pack.setImage(objectnew2.getString("image_path"));
                                pack.setId(objectnew2.getString("id"));

                                list1.add(pack);
                            }

                            if (list1.size() > 0) {
                                article_list.setVisibility(View.VISIBLE);

                                radio_question_list_adapter = new Share_Adapter(Dashboard.this, list1);
                                article_list.setAdapter(radio_question_list_adapter);

                            } else {
                                article_list.setVisibility(View.GONE);

                            }


                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(Dashboard.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

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
                            getaouth();
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

//    public void get_card_list() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = BuildConfig.BASE_URL + "/v1/credit-card-all-quotes";
//        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    // response
//
//                    try {
//                        progressDialog.dismiss();
//                        JSONObject jsonObject = new JSONObject(response);
//                        list1 = new ArrayList<>();
//                        list1.clear();
//
//                        if (jsonObject.getString("status").equalsIgnoreCase("Success")) {
//
//                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
//                            JSONArray jsonArray = (jsonObject1.getJSONArray("bank-quote"));
//                            for (int i = 0; i < 4; i++) {
//                                JSONObject objectnew2 = jsonArray.getJSONObject(i);
//                                Gettersetterforall pack = new Gettersetterforall();
//                                pack.setBank_code(objectnew2.getString("bank_code"));
//                                pack.setName(objectnew2.getString("name"));
//                                pack.setImage(objectnew2.getString("image_path"));
//                                pack.setId(objectnew2.getString("id"));
//                                pack.setFeauters((objectnew2.getJSONArray("feature")));
//                                try {
//                                    JSONArray feesjaonarray = objectnew2.getJSONArray("fee");
//                                    for (int j = 0; j < feesjaonarray.length(); j++) {
//                                        JSONObject object = feesjaonarray.getJSONObject(j);
//                                        try {
//                                            if (object.getString("title").contains("Annual")) {
//                                                pack.setAnnualfees(object.getString("value"));
//                                            }
//                                        } catch (Exception e) {
//                                            pack.setAnnualfees("NA");
//                                        }
//                                        try {
//                                            if (object.getString("title").contains("Joining")) {
//                                                pack.setJoiningfees(object.getString("value"));
//                                            }
//                                        } catch (Exception e) {
//                                            pack.setJoiningfees("NA");
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    pack.setAnnualfees("NA");
//                                    pack.setJoiningfees("NA");
//                                }
//                                list1.add(pack);
//                            }
//
//                            if (list1.size() > 0) {
//                                card_list.setVisibility(View.GONE);
//
//                                radio_question_list_adapter = new Share_Adapter(Dashboard.this, list1);
//                                card_list.setAdapter(radio_question_list_adapter);
//
//                            } else {
//                                card_list.setVisibility(View.GONE);
//
//                            }
//
//
//                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {
//                            if (progressDialog != null && progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
//                            Toast.makeText(Dashboard.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    } catch (Exception e) {
//                        if (progressDialog != null && progressDialog.isShowing()) {
//                            progressDialog.dismiss();
//                        }
//                        e.printStackTrace();
//                    }
//
//
//                },
//                error -> {
//
//                    try {
//                        int statusCode = error.networkResponse.statusCode;
//                        if (statusCode == 421) {
//                            getaouth();
//                        }
//                        error.printStackTrace();
//                        if (progressDialog != null && progressDialog.isShowing()) {
//                            progressDialog.dismiss();
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> header = new HashMap<String, String>();
//                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
//                header.put("Content-Type", "application/json; charset=utf-8");
//                header.put("Accept", "application/json");
//                header.put("Authorization", bearer);
//
//                return header;
//            }
//        };
//        queue.add(getRequest);
//
//    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.line2:
            case R.id.exploremore:
                wishFinAnalytics.history_page();
                Intent intent1 = new Intent(Dashboard.this, CreditCardHistory.class);
                startActivity(intent1);

                break;
            case R.id.bestChoice:
                wishFinAnalytics.best_cc_page();
                Intent intent2 = new Intent(Dashboard.this, ExploreCreditCard.class);
                intent2.putExtra("type", "Best");
                startActivity(intent2);

                break;
            case R.id.bestreward:
                wishFinAnalytics.best_reward_cc_page();
                Intent intent3 = new Intent(Dashboard.this, ExploreCreditCard.class);
                intent3.putExtra("type", "Rewards");
                startActivity(intent3);
                break;
            case R.id.lifetimefree:
                wishFinAnalytics.lifetime_free_cc_page();
                Intent intent4 = new Intent(Dashboard.this, ExploreCreditCard.class);
                intent4.putExtra("type", "Lifetime Free");
                startActivity(intent4);

                break;
            case R.id.besttravel:
                wishFinAnalytics.best_travel_cc_page();
                Intent intent5 = new Intent(Dashboard.this, ExploreCreditCard.class);
                intent5.putExtra("type", "Travel");
                startActivity(intent5);

                break;
            case R.id.bestfuel:
                wishFinAnalytics.best_fuel_cc_page();
                Intent intent6 = new Intent(Dashboard.this, ExploreCreditCard.class);
                intent6.putExtra("type", "Fuel");
                startActivity(intent6);

                break;
            case R.id.bestcashback:
                wishFinAnalytics.best_cashback_cc_page();
                Intent intent7 = new Intent(Dashboard.this, ExploreCreditCard.class);
                intent7.putExtra("type", "Cashback");
                startActivity(intent7);

                break;
            case R.id.line3:
                Intent intent8 = new Intent(Dashboard.this, OfferlistingPge.class);
                startActivity(intent8);
                finish();
                break;
            case R.id.line5:
                Intent intent9 = new Intent(Dashboard.this, Profilepage.class);
                startActivity(intent9);
                finish();
                break;

        }

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
                Request.Method.POST, BuildConfig.BASE_URL + "/oauth", json,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        SessionManager.save_access_token(prefs, (jsonObject.getString("access_token")));
                        SessionManager.save_expirein(prefs, jsonObject.getString("expires_in"));
                        SessionManager.save_token_type(prefs, jsonObject.getString("token_type"));
                        SessionManager.save_refresh_token(prefs, jsonObject.getString("refresh_token"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }


    //    public void get_cibil_credit_factors() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
////        String url = Constants.BASE_URL + "/get-cibil-credit-factors/3061998";
//        String url = getString(R.string.BASE_URL) + "/get-cibil-credit-factors/" + SessionManager.get_cibil_id(prefs);
//        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    // response
//                    if (progressDialog != null && progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        list1 = new ArrayList<>();
//                        list1.clear();
//
//                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//
//                            JSONObject jsonObjectresult1 = new JSONObject(jsonObject.getJSONObject("result").toString());
//                            JSONObject jsonObjectresult = jsonObjectresult1.getJSONObject("userInfo");
//
//                            SessionManager.save_cibil_score(prefs, String.valueOf(jsonObjectresult.getString("cibil_score")));
//                            SessionManager.save_cibil_id(prefs, String.valueOf(jsonObjectresult.getString("cibil_id")));
//                            SessionManager.save_cibil_score(prefs, String.valueOf(jsonObjectresult.getString("cibil_score")));
//                            SessionManager.save_cibil_fetch_date(prefs, String.valueOf(jsonObjectresult.getString("cibil_score_fetch_date")));
//
//                            progressDialog.show();
//                            get_card_list();
//
//                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {
//                            if (progressDialog != null && progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
//                        }
//
//                    } catch (Exception e) {
//                        if (progressDialog != null && progressDialog.isShowing()) {
//                            progressDialog.dismiss();
//                        }
//                        e.printStackTrace();
//                    }
//                },
//                error -> {
//                    // TODO Auto-generated method stub
//                    if (progressDialog != null && progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
//                params.put("Content-Type", "application/json; charset=utf-8");
//                params.put("Accept", "application/json");
//                params.put("Authorization", bearer);
//
//                return params;
//            }
//        };
//        queue.add(getRequest);
//
//    }
//
//    public void update_last_login() {
//
//        final JSONObject json = new JSONObject();
//
//        try {
//            json.put("master_user_id", "" + SessionManager.get_masteruserid(prefs));
//            json.put("mobile_number", "" + SessionManager.get_mobile(prefs));
//            json.put("device_token", "" + SessionManager.get_device_token(prefs));
//            json.put("device_type", "Android");
////            json.put("app_version", "" + SessionManager.get_appversion(prefs));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, getString(R.string.BASE_URL) + "/update-last-login", json,
//                response -> {
//
//                    try {
//                        get_cibil_user_detail();
//                    } catch (Exception e) {
//                        get_cibil_user_detail();
//                    }
//
//                }, error -> {
//            error.printStackTrace();
//            if (progressDialog != null && progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//
//        }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> header = new HashMap<String, String>();
//                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
//                header.put("Content-Type", "application/json; charset=utf-8");
//                header.put("Accept", "application/json");
//                header.put("Authorization", bearer);
//
//                return header;
//            }
//        };
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        queue.add(jsonObjectRequest);
//    }
//
//    public void get_cibil_user_detail() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        String url = getString(R.string.BASE_URL) + "/v1/get-cibil-user-detail?master_user_id=" + SessionManager.get_masteruserid(prefs) + "&mf_user_id=" + SessionManager.get_mfuserid(prefs);
////        String url = Constants.BASE_URL + "/v1/get-cibil-user-detail?master_user_id=2616097&mf_user_id=2613505";
//        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    // response
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        {
//                            if (!jsonObject.getString("status").equalsIgnoreCase("failed")) {
//
//                                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
//
//                                if (jsonObject1.getString("cibil_score").equalsIgnoreCase("0") ||
//                                        jsonObject1.getString("cibil_score").equalsIgnoreCase("1")) {
//
//                                    if (progressDialog != null && progressDialog.isShowing()) {
//                                        progressDialog.dismiss();
//                                    }
//
//                                } else if (jsonObject1.isNull("cibil_id") || jsonObject1.getString("cibil_score").equalsIgnoreCase("-1")) {
//
//                                    if (progressDialog != null && progressDialog.isShowing()) {
//                                        progressDialog.dismiss();
//                                    }
//                                    try {
//                                        int income = Integer.parseInt(jsonObject1.getString("annual_income")) / 12;
//                                        SessionManager.save_monthly_income(prefs, "" + income);
//                                        SessionManager.save_occupation(prefs, "" + jsonObject1.getString("occupation"));
//                                    } catch (Exception e) {
//                                        SessionManager.save_monthly_income(prefs, "1");
//                                        SessionManager.save_occupation(prefs, "1");
//                                    }
//                                } else {
//                                    if (jsonObject1.getString("cibil_status").equalsIgnoreCase("Success")) {
//
//                                        String date = jsonObject1.getString("cibil_score_fetch_date");
//                                        SessionManager.save_cibil_fetch_date(prefs, date);
//                                        SessionManager.save_cibil_id(prefs, jsonObject1.getString("cibil_id"));
//                                        SessionManager.save_cibil_score(prefs, jsonObject1.getString("cibil_score"));
//
//                                        long differenceDates = 0;
//                                        try {
//                                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                                            Date date1;
//                                            Date date2;
//                                            date1 = Calendar.getInstance().getTime();
//                                            date2 = dates.parse(jsonObject1.getString("cibil_score_fetch_date"));
//                                            long difference = Math.abs(date1.getTime() - date2.getTime());
//                                            differenceDates = difference / (24 * 60 * 60 * 1000);
//                                        } catch (Exception e) {
//
//                                        }
//
//                                        get_cibil_credit_factors();
//
//                                        if (progressDialog != null && progressDialog.isShowing()) {
//                                            progressDialog.dismiss();
//                                        }
//                                        if (differenceDates > 30) {
//
//
//                                        } else {
//
//
//                                        }
//                                    } else {
//                                        get_cibil_fulfill_order("normal");
//                                    }
//
//                                }
//
//                            } else {
//                                get_cibil_fulfill_order("normal");
//                            }
//                        }
//                    } catch (Exception e) {
//                        if (progressDialog != null && progressDialog.isShowing()) {
//                            progressDialog.dismiss();
//                        }
//                        e.printStackTrace();
//                    }
//                },
//                error -> {
//                    // TODO Auto-generated method stub
//                    if (progressDialog != null && progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
//                params.put("Content-Type", "application/json; charset=utf-8");
//                params.put("Accept", "application/json");
//                params.put("Authorization", bearer);
//
//                return params;
//            }
//        };
//        queue.add(getRequest);
//
//    }
//
//    public void get_cibil_fulfill_order(String type) {
//
//        final JSONObject json = new JSONObject();
//
//        try {
//            if (type.equalsIgnoreCase("normal"))
//                json.put("first_name", "" + SessionManager.get_firstname(prefs));
//            json.put("middle_name", "");
////            if (SessionManager.get_lastname(prefs).equalsIgnoreCase("")) {
////                json.put("last_name", "" + SessionManager.get_firstname(prefs));
////            } else {
////                json.put("last_name", "" + SessionManager.get_lastname(prefs));
////            }
//            json.put("pancard", "" + SessionManager.get_pan(prefs));
//            json.put("date_of_birth", "" + SessionManager.get_dob(prefs));
//            json.put("email_id", "" + SessionManager.get_emailid(prefs));
//            json.put("annual_income", "" + Integer.parseInt(SessionManager.get_monthly_income(prefs)) * 12);
//            json.put("occupation", "" + SessionManager.get_occupation(prefs));
//            json.put("mobile_number", "" + SessionManager.get_mobile(prefs));
//            json.put("gender", "1");
//            json.put("city_name", "Default");
//            json.put("state_code", "27");
//            json.put("residence_address", "Default");
//            json.put("residence_pincode", "400001");
//            json.put("legal_response", "Accept");
//            json.put("report_trigger", "true");
//            json.put("show_report_xml", false);
//            json.put("consent_option", "");
//            json.put("website_flag", "wishfin");
//            json.put("resource_pagename", "Cibil_Wishfin_Android");
//            json.put("resource_source", "Cibil_Wishfin_Android");
//            json.put("resource_querystring", "");
//            json.put("resource_ip_address", IPaddress);
//            json.put("source", "Wishfin_Android");
//            json.put("utm_source", "" + SessionManager.get_utmsource(prefs));
//            json.put("utm_medium", "" + SessionManager.get_utmmedium(prefs));
//            json.put("referrer_address", "Wishfin_Android");
//            json.put("querystring", "");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, getString(R.string.BASE_URL) + "/v1/cibil-fulfill-order", json,
//                response -> {
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.toString());
//
//                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//
//                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
//                            String cibil_score = "", lastdate = "";
//                            try {
//                                String cibil_id = jsonObject1.getString("cibil_id");
//                                SessionManager.save_cibil_id(prefs, cibil_id);
//                            } catch (Exception e) {
//
//                            }
//                            String cibil_status = jsonObject1.getString("cibil_status");
//                            try {
//                                cibil_score = jsonObject1.getString("cibil_score");
//
//                            } catch (Exception e) {
//
//                            }
//                            try {
//                                lastdate = jsonObject1.getString("cibil_score_fetch_date");
//                            } catch (Exception e) {
//
//                            }
//                            String apicall = "";
//                            try {
//                                apicall = jsonObject1.getString("next_api_call");
//                            } catch (Exception e) {
//
//                            }
//
//                            String is_returning_customer = "";
//                            try {
//                                is_returning_customer = jsonObject1.getString("is_returning_customer");
//                            } catch (Exception e) {
//
//                            }
//                            if (cibil_status.equalsIgnoreCase("Failure")) {
//
//                                if (progressDialog != null && progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
////                                showerrordialog();
//                            }
//
//                            if (cibil_status.equalsIgnoreCase("Inprogress") && apicall.
//                                    equalsIgnoreCase("cibil-authentication-questions") && is_returning_customer.equalsIgnoreCase("1")
//                                    || (cibil_status.equalsIgnoreCase("Pending") && apicall.
//                                    equalsIgnoreCase("cibil-authentication-questions") && is_returning_customer.equalsIgnoreCase("1"))) {
//                                if (progressDialog != null && progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
////                                authenticateuestionasync(type);
//
//                            } else if (is_returning_customer.equalsIgnoreCase("1")) {
//                                if (progressDialog != null && progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
//                                Toast.makeText(Dashboard.this, "User already exist", Toast.LENGTH_SHORT).show();
//
//                            } else if (cibil_status.equalsIgnoreCase("success") && apicall.
//                                    equalsIgnoreCase("cibil-customer-assets")) {
//                                if (progressDialog != null && progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
////                                customerassets(type);
//
//                            } else if (cibil_status.equalsIgnoreCase("Inprogress") && apicall.
//                                    equalsIgnoreCase("cibil-authentication-questions")
//                                    || (cibil_status.equalsIgnoreCase("Pending") && apicall.
//                                    equalsIgnoreCase("cibil-authentication-questions"))) {
//                                if (progressDialog != null && progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
////                                authenticateuestionasync(type);
//
//                            } else if (cibil_status.equalsIgnoreCase("failed")) {
//                                if (progressDialog != null && progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
//                                String message = "";
//                                try {
//                                    message = jsonObject1.getString("message");
//                                } catch (Exception e) {
//                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("RequestError");
//                                    message = jsonObject2.getString("NO_HIT");
//                                }
//
//                                Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
//                            } else {
//                                JSONObject jsonObjectresult = new JSONObject(jsonObject.getJSONObject("result").toString());
//
//                                if (jsonObjectresult.getString("cibil_score").equalsIgnoreCase("0") ||
//                                        jsonObjectresult.getString("cibil_score").equalsIgnoreCase("1")) {
//
//                                    if (progressDialog != null && progressDialog.isShowing()) {
//                                        progressDialog.dismiss();
//                                    }
//
//                                } else if (jsonObjectresult.isNull("cibil_id") || jsonObjectresult.getString("cibil_score").equalsIgnoreCase("-1")) {
//
//                                    if (progressDialog != null && progressDialog.isShowing()) {
//                                        progressDialog.dismiss();
//                                    }
//
//                                } else {
//
//                                    SessionManager.save_login(prefs, "True");
//                                    Constants.cardresponse = "";
//                                    Constants.hardinquiryresponse = "";
//                                    Constants.ontimepaymentresponse = "";
//                                    Constants.ontimepaymentresponse = "";
//                                    Constants.loanresponse = "";
//                                    SessionManager.save_cibil_id(prefs, String.valueOf(jsonObjectresult.getString("cibil_id")));
//                                    SessionManager.save_cibil_score(prefs, String.valueOf(jsonObjectresult.getString("cibil_score")));
//                                    SessionManager.save_cibil_fetch_date(prefs, String.valueOf(jsonObjectresult.getString("cibil_score_fetch_date")));
//
//                                    if (progressDialog != null && progressDialog.isShowing()) {
//                                        progressDialog.dismiss();
//                                    }
//                                    if (type.equalsIgnoreCase("normal")) {
//                                        if (progressDialog != null) {
//                                            progressDialog.show();
//                                        }
//                                        get_cibil_credit_factors();
//                                    } else {
//                                        if (progressDialog != null) {
//                                            progressDialog.show();
//                                            get_card_list();
//                                        }
//
//                                    }
//                                }
//                            }
//
//                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {
//
//                            if (progressDialog != null && progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
//
//                        }
//
//                    } catch (Exception e) {
//                        if (progressDialog != null && progressDialog.isShowing()) {
//                            progressDialog.dismiss();
//                        }
//                        e.printStackTrace();
//                    }
//
//                }, error -> {
//
//            try {
//                int statusCode = error.networkResponse.statusCode;
//                if (statusCode == 422) {
//                    try {
//                        String string = new String(error.networkResponse.data);
//                        JSONObject jsonObject = new JSONObject(string);
//
//                    } catch (Exception ignored) {
//
//                    }
//                }
//                error.printStackTrace();
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> header = new HashMap<String, String>();
//                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
//                header.put("Content-Type", "application/json; charset=utf-8");
//                header.put("Accept", "application/json");
//                header.put("Authorization", bearer);
//
//                return header;
//            }
//        };
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        queue.add(jsonObjectRequest);
//    }

    public void get_cibil_history() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = BuildConfig.BASE_URL + "/historic-score?mobile=" + SessionManager.get_mobile(prefs);
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // response

                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        {
                            if (!jsonObject.getString("status").equalsIgnoreCase("failed")) {

                                SessionManager.save_cibil_checked_status(prefs, "true");


                            } else {
                                SessionManager.save_cibil_checked_status(prefs, "false");
                                bar5.setVisibility(View.VISIBLE);
                                SessionManager.save_logintype(prefs, "Signup");
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
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
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                String bearer = "Bearer " + SessionManager.get_access_token(prefs);
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Accept", "application/json");
                params.put("Authorization", bearer);

                return params;
            }
        };
        queue.add(getRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        get_cibil_history();
    }

//    public void showRateApp() {
//        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
//        request.addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // We can get the ReviewInfo object
//                ReviewInfo reviewInfo = task.getResult();
//
//                Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
//                flow.addOnCompleteListener(task1 -> {
//                    // The flow has finished. The API does not indicate whether the user
//                    // reviewed or not, or even whether the review dialog was shown. Thus, no
//                    // matter the result, we continue our app flow.
//                });
//            } else {
//                // There was some problem, continue regardless of the result.
//                // show native rate app dialog on error
//                showRateAppFallbackDialog();
//            }
//        });
//    }
//    private void showRateAppFallbackDialog() {
//        new MaterialAlertDialogBuilder(this)
//                .setTitle(R.string.rate_app_title)
//                .setMessage(R.string.rate_app_message)
//                .setPositiveButton(R.string.rate_btn_pos, (dialog, which) -> redirectToPlayStore())
//                .setNegativeButton(R.string.rate_btn_neg,
//                        (dialog, which) -> {
//                            // take action when pressed not now
//                        })
//                .setNeutralButton(R.string.rate_btn_nut,
//                        (dialog, which) -> {
//                            // take action when pressed remind me later
//                        })
//                .setOnDismissListener(dialog -> {
//                })
//                .show();
//    }

    // redirecting user to PlayStore
//    public void redirectToPlayStore() {
//        final String appPackageName = getPackageName();
//        try {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//        } catch (ActivityNotFoundException exception) {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//        }
//    }

//    private void Review() {
//        manager.requestReviewFlow().addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
//            @Override
//            public void onComplete(@NonNull Task<ReviewInfo> task) {
//                if (task.isSuccessful()) {
//                    reviewInfo = task.getResult();
//                    manager.launchReviewFlow(Dashboard.this, reviewInfo).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(Exception e) {
//                            Toast.makeText(Dashboard.this, "Rating Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(Dashboard.this, "Review Completed, Thank You!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception e) {
//                Toast.makeText(Dashboard.this, "In-App Request Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public class Share_Adapter extends RecyclerView.Adapter<Share_Adapter.MyViewHolder> {

        private ArrayList<Gettersetterforall> list_car;
        Activity context;

        Share_Adapter(Activity mcontext, ArrayList<Gettersetterforall> list) {
            this.list_car = list;
            this.context = mcontext;
        }

        // method for filtering our recyclerview items.
        public void filterList(ArrayList<Gettersetterforall> filterllist) {
            // below line is to add our filtered
            // list in our course array list.
            list_car = filterllist;
            // below line is to notify our adapter
            // as change in recycler view data.
            notifyDataSetChanged();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView,textView2;

            MyViewHolder(View view) {
                super(view);

                imageView = view.findViewById(R.id.imageView);
                textView = view.findViewById(R.id.textView);
                textView2 = view.findViewById(R.id.textView2);

            }
        }

        @Override
        public Share_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_list_adapter, parent, false);

            return new Share_Adapter.MyViewHolder(itemView);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(Share_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.textView.setText(list_car.get(position).getName());
            holder.textView2.setText(list_car.get(position).getBank_code());


//            Picasso.get()
//                    .load(list_car.get(position).getImage())
//                    .error(getDrawable(R.drawable.cclogo))
//                    .into(holder.imageView);


        }

        @Override
        public int getItemCount() {
            return list_car.size();
        }

    }

}
