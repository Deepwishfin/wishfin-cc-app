package com.wishfin_credit_card;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
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
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CardDetailPage extends Activity {

    ImageView imageView;
    TextView cardname, joiningfees, annualfees, instantapply;
    String strcardname = "", strannualfees = "", strjoiningfees = "", from = "", lead_id = "", bank_code = "";
    String imagepath = "";
    String id = "";
    String features = "";
    KProgressHUD progressDialog;
    SharedPreferences prefs;
    RequestQueue queue;
    RecyclerView card_list;
    ArrayList<Gettersetterforall> list1 = new ArrayList<>();
    Share_Adapter radio_question_list_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_detail_page);

        imageView = findViewById(R.id.imageView);
        cardname = findViewById(R.id.cardname);
        instantapply = findViewById(R.id.instantapply);
        annualfees = findViewById(R.id.annualfees);
        joiningfees = findViewById(R.id.joiningfees);
        card_list = findViewById(R.id.card_list);

        queue = Volley.newRequestQueue(CardDetailPage.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(CardDetailPage.this);

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
                imagepath = "";
                id = "";
                features = "";
                strannualfees = "";
                strjoiningfees = "";
                from = "";
                lead_id = "";
                bank_code = "";
            } else {
                strcardname = extras.getString("cardname");
                imagepath = extras.getString("imagepath");
                id = extras.getString("id");
                features = extras.getString("features");
                strannualfees = extras.getString("annual");
                strjoiningfees = extras.getString("joining");
                from = extras.getString("from");
                lead_id = extras.getString("lead_id");
                bank_code = extras.getString("bank_code");
            }
        } else {
            strcardname = (String) savedInstanceState.getSerializable("cardname");
            imagepath = (String) savedInstanceState.getSerializable("imagepath");
            id = (String) savedInstanceState.getSerializable("id");
            features = (String) savedInstanceState.getSerializable("features");
            strannualfees = (String) savedInstanceState.getSerializable("annual");
            strjoiningfees = (String) savedInstanceState.getSerializable("joining");
            from = (String) savedInstanceState.getSerializable("from");
            lead_id = (String) savedInstanceState.getSerializable("lead_id");
            bank_code = (String) savedInstanceState.getSerializable("bank_code");
        }

        try {
            if (from.equalsIgnoreCase("Dashboard")) {
                instantapply.setVisibility(View.VISIBLE);
            } else if (from.equalsIgnoreCase("Explore")) {
                instantapply.setVisibility(View.VISIBLE);
            } else if (from.equalsIgnoreCase("Eligible")) {
                instantapply.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            instantapply.setVisibility(View.VISIBLE);
        }

        cardname.setText(strcardname);

        if (strannualfees.equalsIgnoreCase("null")) {
            annualfees.setText("NA");
        } else {
            annualfees.setText(strannualfees);
        }

        if (strjoiningfees.equalsIgnoreCase("null")) {
            joiningfees.setText("NA");
        } else {
            joiningfees.setText(strjoiningfees);

        }

        try {
            JSONArray jsonArr = new JSONArray(features);
            list1 = new ArrayList<>();
            list1.clear();
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                Gettersetterforall pack = new Gettersetterforall();
                pack.setName(jsonObj.getString("value"));
                list1.add(pack);
            }

            radio_question_list_adapter = new Share_Adapter(CardDetailPage.this, list1);
            card_list.setAdapter(radio_question_list_adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Picasso.get()
                .load(imagepath)
                .into(imageView);

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

                if (from.equalsIgnoreCase("Eligible")) {
                    progressDialog.show();
                    select_opted_bank();
                } else {
                    if (SessionManager.get_lead_id(prefs).equalsIgnoreCase("")) {
                        Intent intent = new Intent(CardDetailPage.this, PersonalInformationPage.class);
                        startActivity(intent);
                        finish();
                    } else {

                        long differenceDates = 0;
                        try {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Date date1;
                            Date date2;
                            date1 = Calendar.getInstance().getTime();
                            date2 = dates.parse(SessionManager.get_lastccapplydate(prefs));
                            long difference = Math.abs(date1.getTime() - date2.getTime());
                            differenceDates = difference / (24 * 60 * 60 * 1000);
                        } catch (Exception e) {

                        }

                        int remainingdays = (int) (31 - differenceDates);
                        String message = "Wait " + remainingdays + " Days";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CardDetailPage.this, EligibleCardsListing.class);
                        intent.putExtra("id", SessionManager.get_lead_id(prefs));
                        startActivity(intent);
                        finish();
                    }


                }


            }
        });
    }

    public void select_opted_bank() {

        final JSONObject json = new JSONObject();

        try {
            json.put("bank_code", "" + bank_code);
            json.put("lead_id", "" + lead_id);
            json.put("type", "cc");
            json.put("credit_card_id", "" + id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, getString(R.string.BASE_URL) + "/select-opted-bank", json,
                response -> {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(CardDetailPage.this, Thakyoupage.class);
                    startActivity(intent);

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

            return new Share_Adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(Share_Adapter.MyViewHolder holder, final int position) {

            holder.tv1.setText(HtmlCompat.fromHtml(list_car.get(position).getName(), HtmlCompat.FROM_HTML_MODE_LEGACY));


        }

        @Override
        public int getItemCount() {
            return list_car.size();
        }

    }

}
