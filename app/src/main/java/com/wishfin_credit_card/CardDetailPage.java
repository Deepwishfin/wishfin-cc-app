package com.wishfin_credit_card;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardDetailPage extends Activity {

    ImageView imageView;
    TextView cardname, joiningfees, annualfees, instantapply;
    String strcardname = "", bank_code = "", insta_apply_link = "", status = "", card_id = "";
    String imagepath = "";
    KProgressHUD progressDialog;
    SharedPreferences prefs;
    RequestQueue queue;
    RecyclerView card_list;
    ArrayList<Gettersetterforall> list1 = new ArrayList<>();
    Share_Adapter radio_question_list_adapter;
    Dialog dialog;
    RelativeLayout backbutton;
    WishFinAnalytics wishFinAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_detail_page);

        wishFinAnalytics = new WishFinAnalytics(this);

        imageView = findViewById(R.id.imageView);
        cardname = findViewById(R.id.cardname);
        instantapply = findViewById(R.id.instantapply);
        annualfees = findViewById(R.id.annualfees);
        joiningfees = findViewById(R.id.joiningfees);
        card_list = findViewById(R.id.card_list);
        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        queue = Volley.newRequestQueue(CardDetailPage.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(CardDetailPage.this);

        progressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.dialogtext))
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        dialog = new Dialog(CardDetailPage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                bank_code = "";
                strcardname = "";
                status = "";
//                imagepath = "";
                card_id = "";
//                features = "";
//                strannualfees = "";
//                strjoiningfees = "";
//                lead_id = "";
                insta_apply_link = "";
            } else {
                bank_code = extras.getString("bank_code");
                strcardname = extras.getString("cardname");
                status = extras.getString("status");
//                imagepath = extras.getString("imagepath");
                card_id = extras.getString("card_id");
//                features = extras.getString("features");
//                strannualfees = extras.getString("annual");
//                strjoiningfees = extras.getString("joining");
//                lead_id = extras.getString("lead_id");
                insta_apply_link = extras.getString("insta_apply_link");
            }
        } else {
            strcardname = (String) savedInstanceState.getSerializable("cardname");
            status = (String) savedInstanceState.getSerializable("status");
            bank_code = (String) savedInstanceState.getSerializable("bank_code");
//            imagepath = (String) savedInstanceState.getSerializable("imagepath");
            card_id = (String) savedInstanceState.getSerializable("card_id");
//            features = (String) savedInstanceState.getSerializable("features");
//            strannualfees = (String) savedInstanceState.getSerializable("annual");
//            strjoiningfees = (String) savedInstanceState.getSerializable("joining");
//            lead_id = (String) savedInstanceState.getSerializable("lead_id");
            insta_apply_link = (String) savedInstanceState.getSerializable("insta_apply_link");

        }

        progressDialog.show();
        get_card_list();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(CardDetailPage.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(CardDetailPage.this) {
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

        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        card_list.addItemDecoration(new DividerItemDecoration(CardDetailPage.this, DividerItemDecoration.VERTICAL));
        card_list.setLayoutManager(layoutManager1);

        instantapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wishFinAnalytics.insta_apply_clicked();

                if (isThereInternetConnection()) {
//                    Intent intent = new Intent(CardDetailPage.this, WebviewActivity.class);
//                    intent.putExtra("url", insta_apply_link);
//                    intent.putExtra("bank_code", bank_code);
//                    intent.putExtra("strcardname", strcardname);
//                    startActivity(intent);
                    progressDialog.show();
                    get_encrypted_link();
                } else {
                    Toast.makeText(CardDetailPage.this, "Please check your internet", Toast.LENGTH_LONG).show();

                }

            }
        });

        try {
            if (status.equalsIgnoreCase("NA")) {
                progressDialog.show();
                getstatus_data();
            }
        }catch (Exception e)
        {
            progressDialog.show();
            getstatus_data();
        }


    }

    public void get_card_list() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = BuildConfig.BASE_URL + "/v1/credit-card-all-quotes?bankCode=" + bank_code;
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
//                                Gettersetterforall pack = new Gettersetterforall();


                                if (objectnew2.getString("name").equalsIgnoreCase(strcardname)) {
                                    cardname.setText(strcardname);
                                    imagepath = objectnew2.getString("image_path");
                                    Picasso.get()
                                            .load(imagepath)
                                            .into(imageView);
                                    insta_apply_link = ((objectnew2.getString("insta_apply_link")));


                                    try {
                                        JSONArray jsonArr = objectnew2.getJSONArray("feature");
                                        list1 = new ArrayList<>();
                                        list1.clear();
                                        for (int j = 0; j < jsonArr.length(); j++) {
                                            JSONObject jsonObj = jsonArr.getJSONObject(j);
                                            Gettersetterforall pack1 = new Gettersetterforall();
                                            pack1.setName(jsonObj.getString("value"));
                                            list1.add(pack1);
                                        }

                                        radio_question_list_adapter = new Share_Adapter(CardDetailPage.this, list1);
                                        card_list.setAdapter(radio_question_list_adapter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


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
                            Toast.makeText(CardDetailPage.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

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
            public Map<String, String> getHeaders() {
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

    public static class Share_Adapter extends RecyclerView.Adapter<Share_Adapter.MyViewHolder> {

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

        static class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv1;

            MyViewHolder(View view) {
                super(view);
                tv1 = view.findViewById(R.id.textView);

            }
        }

        @Override
        public Share_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.features_list_adapter, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(Share_Adapter.MyViewHolder holder, final int position) {

//            holder.tv1.setText((list_car.get(position).getName()));
            holder.tv1.setText(HtmlCompat.fromHtml(list_car.get(position).getName(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        }

        @Override
        public int getItemCount() {
            return list_car.size();
        }

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

    public void get_encrypted_link() {

        final JSONObject json = new JSONObject();
        try {
            json.put("cid", SessionManager.get_cibil_id(prefs));
            json.put("first_name", SessionManager.get_firstname(prefs));
            json.put("last_name", SessionManager.get_lastname(prefs));
            json.put("email", SessionManager.get_emailid(prefs));
            json.put("mobile", SessionManager.get_mobile(prefs));
            json.put("bank_code", bank_code);
            json.put("card_name", strcardname);
            json.put("card_id", card_id);
            json.put("card_image", imagepath);
            json.put("device_type", "Android");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getRequest = new JsonObjectRequest(
                Request.Method.POST, BuildConfig.BASE_URL + "/secure-encode", json,
                response -> {
                    try {

                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("Success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                            String url = "";
//                            if (bank_code.equalsIgnoreCase("m024")) {
//                                url = insta_apply_link;
//                            } else {
//                                url = insta_apply_link + "&view=" + jsonObject1.getString("hash");
//                            }
                            url = insta_apply_link + "&view=" + jsonObject1.getString("hash");

                            Intent intent = new Intent(CardDetailPage.this, WebviewActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("bank_code", bank_code);
                            intent.putExtra("strcardname", strcardname);
                            startActivity(intent);
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(CardDetailPage.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

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
            public Map<String, String> getHeaders() {
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

    private void getstatus_data() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = BuildConfig.BASE_URL + "/get-credit-card-status?mobile_number=" +
                SessionManager.get_mobile(prefs) + "&bank_code=" + bank_code + "&card_id=" + card_id;

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // response

                    try {

                        progressDialog.dismiss();

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                            if (jsonObject1.getString("card_status").equalsIgnoreCase("Pending") ||
                                    jsonObject1.getString("card_status").equalsIgnoreCase("Applied")) {
                                instantapply.setText(jsonObject1.getString("card_status"));
                                instantapply.setBackgroundResource(R.drawable.roundedbuttonpending);
                                instantapply.setTextColor(Color.parseColor("#6563FF"));
                                instantapply.setClickable(false);
                            } else if (jsonObject1.getString("card_status").equalsIgnoreCase("Approved")) {
                                instantapply.setText(jsonObject1.getString("card_status"));
                                instantapply.setBackgroundResource(R.drawable.roundedbuttonapproved);
                                instantapply.setTextColor(Color.parseColor("#E2A300"));
                                instantapply.setClickable(false);
                            } else if (jsonObject1.getString("card_status").equalsIgnoreCase("Not Applied")) {
                                instantapply.setText(jsonObject1.getString("card_status"));
                                instantapply.setBackgroundResource(R.drawable.roundedbuttonpending);
                                instantapply.setTextColor(Color.parseColor("#6563FF"));
                                instantapply.setClickable(true);
                            } else {
                                instantapply.setText(jsonObject1.getString("card_status"));
                                instantapply.setBackgroundResource(R.drawable.roundedbuttonpending);
                                instantapply.setTextColor(Color.parseColor("#6563FF"));
                                instantapply.setClickable(false);

                            }

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


}
