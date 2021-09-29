package com.wishfin_credit_card;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExploreCreditCard extends Activity implements View.OnClickListener {

    RecyclerView card_list;
    KProgressHUD progressDialog;
    SharedPreferences prefs;
    RequestQueue queue;
    ArrayList<Gettersetterforall> list1 = new ArrayList<>();
    Share_Adapter radio_question_list_adapter;
    LinearLayout line1, line2, line3, line5;
    boolean doubleBackToExitPressedOnce = false;
    EditText search_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explorecreditcard);

        queue = Volley.newRequestQueue(ExploreCreditCard.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(ExploreCreditCard.this);

        progressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.dialogtext))
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        progressDialog.show();
        getaouth();
        get_card_list();

        card_list = findViewById(R.id.card_list);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line5 = findViewById(R.id.line5);

        line1.setOnClickListener(this);
        line2.setOnClickListener(this);
        line3.setOnClickListener(this);
        line5.setOnClickListener(this);
        search_bar = findViewById(R.id.search_bar);

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(ExploreCreditCard.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(ExploreCreditCard.this) {
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
        card_list.addItemDecoration(new DividerItemDecoration(ExploreCreditCard.this, DividerItemDecoration.VERTICAL));
        card_list.setLayoutManager(layoutManager1);

    }

    public void get_card_list() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.BASE_URL) + "/api/v1/credit-card-all-quotes";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // response

                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        list1 = new ArrayList<>();
                        list1.clear();

                        if (jsonObject.getString("status").equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                            JSONArray jsonArray = (jsonObject1.getJSONArray("bank-quote"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectnew2 = jsonArray.getJSONObject(i);
                                Gettersetterforall pack = new Gettersetterforall();
                                pack.setBank_code(objectnew2.getString("bank_code"));
                                pack.setName(objectnew2.getString("name"));
                                pack.setImage(objectnew2.getString("image_path"));
                                pack.setId(objectnew2.getString("id"));
                                pack.setFeauters((objectnew2.getJSONArray("feature")));
                                try {
                                    JSONArray feesjaonarray = objectnew2.getJSONArray("fee");
                                    for (int j = 0; j < feesjaonarray.length(); j++) {
                                        JSONObject object = feesjaonarray.getJSONObject(j);
                                        try {
                                            if (object.getString("title").contains("Annual")) {
                                                pack.setAnnualfees(object.getString("value"));
                                            }
                                        } catch (Exception e) {
                                            pack.setAnnualfees("NA");
                                        }
                                        try {
                                            if (object.getString("title").contains("Joining")) {
                                                pack.setJoiningfees(object.getString("value"));
                                            }
                                        } catch (Exception e) {
                                            pack.setJoiningfees("NA");
                                        }
                                    }
                                } catch (Exception e) {
                                    pack.setAnnualfees("NA");
                                    pack.setJoiningfees("NA");
                                }
                                list1.add(pack);
                            }

                            if (list1.size() > 0) {
                                card_list.setVisibility(View.VISIBLE);

                                radio_question_list_adapter = new Share_Adapter(ExploreCreditCard.this, list1);
                                card_list.setAdapter(radio_question_list_adapter);

                            } else {
                                card_list.setVisibility(View.GONE);

                            }


                        } else if (jsonObject.getString("status").equalsIgnoreCase("failed")) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(ExploreCreditCard.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

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
            TextView tv1, tv2, joiningfees, annualfees;
            ImageView reli;
            RelativeLayout relit;

            MyViewHolder(View view) {
                super(view);
                tv1 = view.findViewById(R.id.textView);
                tv2 = view.findViewById(R.id.textView2);
                joiningfees = view.findViewById(R.id.joiningfees);
                annualfees = view.findViewById(R.id.annualfees);
                reli = view.findViewById(R.id.imageView);
                relit = view.findViewById(R.id.relit);

            }
        }

        @Override
        public Share_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_list_adapter, parent, false);

            return new Share_Adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(Share_Adapter.MyViewHolder holder, final int position) {

            holder.tv1.setText(list_car.get(position).getName());
            holder.tv2.setText(list_car.get(position).getId());
            holder.joiningfees.setText(list_car.get(position).getJoiningfees());
            holder.annualfees.setText(list_car.get(position).getAnnualfees());

            Picasso.get()
                    .load(list_car.get(position).getImage())
                    .into(holder.reli);

            holder.relit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ExploreCreditCard.this, CardDetailPage.class);
                    intent.putExtra("lead_id", "");
                    intent.putExtra("bank_code", ""+list_car.get(position).getBank_code());
                    intent.putExtra("id", "" + list_car.get(position).getId());
                    intent.putExtra("cardname", "" + list_car.get(position).getName());
                    intent.putExtra("imagepath", "" + list_car.get(position).getImage());
                    intent.putExtra("features", "" + list_car.get(position).getFeauters());
                    intent.putExtra("joining", "" + list_car.get(position).getJoiningfees());
                    intent.putExtra("annual", "" + list_car.get(position).getAnnualfees());
                    intent.putExtra("from", "Explore");
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list_car.size();
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
                Request.Method.POST, getString(R.string.BASE_URL) + "/oauth", json,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        SessionManager.save_access_token(prefs, jsonObject.getString("access_token"));
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

    private void filter(String text) {

        ArrayList<Gettersetterforall> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Gettersetterforall item : list1) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            radio_question_list_adapter.filterList(filteredlist);
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.line1:
                Intent intent2 = new Intent(ExploreCreditCard.this, Dashboard.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.line3:
                Intent intent3 = new Intent(ExploreCreditCard.this, OfferlistingPge.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.line5:
                Intent intent4 = new Intent(ExploreCreditCard.this, Profilepage.class);
                startActivity(intent4);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
