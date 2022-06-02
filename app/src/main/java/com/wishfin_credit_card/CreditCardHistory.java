package com.wishfin_credit_card;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class CreditCardHistory extends Activity implements View.OnClickListener {

    RecyclerView card_list;
    KProgressHUD progressDialog;
    SharedPreferences prefs;
    RequestQueue queue;
    ArrayList<Gettersetterforall> list1 = new ArrayList<>();
    CreditCardHistory.Share_Adapter radio_question_list_adapter;
    LinearLayout line1, line2, line3, line5;
    //    boolean doubleBackToExitPressedOnce = false;
    RelativeLayout heading_relative;
    ImageView backbutton;
    TextView heading_cc_list, sub_heading_cc_list;
    String type = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credithistory);

        queue = Volley.newRequestQueue(CreditCardHistory.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(CreditCardHistory.this);

        progressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.dialogtext))
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        progressDialog.show();
//        getaouth();
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

        heading_relative = findViewById(R.id.heading_relative);
        heading_cc_list = findViewById(R.id.heading_cc_list);
        sub_heading_cc_list = findViewById(R.id.sub_heading_cc_list);
        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(CreditCardHistory.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(CreditCardHistory.this) {
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
//        card_list.addItemDecoration(new DividerItemDecoration(CreditCardHistory.this, DividerItemDecoration.VERTICAL));
        card_list.setLayoutManager(layoutManager1);

    }

    public void get_card_list() {

        final JSONObject json = new JSONObject();
        try {
            json.put("auth_key", BuildConfig.oAuthdeal4loans);
            json.put("method", "GetWFAppLeadStatusAPI");
            json.put("mobile", SessionManager.get_mobile(prefs));
            json.put("source", "wf_android");
            json.put("api_type", "PROD");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, getString(R.string.BASE_URL_Deal4Loans), json,
                response -> {
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        list1 = new ArrayList<>();
                        list1.clear();

                        if (jsonObject.getString("message").equalsIgnoreCase("Success")) {

                            JSONArray jsonArray = (jsonObject.getJSONArray("data"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectnew2 = jsonArray.getJSONObject(i);
                                Gettersetterforall pack = new Gettersetterforall();
                                pack.setBank_code(objectnew2.getString("bank_code"));
                                pack.setName(objectnew2.getString("card_name"));
                                pack.setImage(objectnew2.getString("card_image"));
                                pack.setAnnualfees(objectnew2.getString("latest_date_created"));
                                pack.setJoiningfees(objectnew2.getString("total_clicked"));
                                pack.setCard_state(objectnew2.getString("status"));

                                list1.add(pack);

                            }

                            if (list1.size() > 0) {
                                card_list.setVisibility(View.VISIBLE);

                                radio_question_list_adapter = new Share_Adapter(CreditCardHistory.this, list1);
                                card_list.setAdapter(radio_question_list_adapter);

                            } else {
                                card_list.setVisibility(View.GONE);

                            }


                        } else if (jsonObject.getString("message").equalsIgnoreCase("No Record Found")) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(CreditCardHistory.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
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
            TextView tv1, tv2, joiningfees, annualfees, viewdetails;
            ImageView reli;
            RelativeLayout relit;

            MyViewHolder(View view) {
                super(view);
                tv1 = view.findViewById(R.id.textView);
                tv2 = view.findViewById(R.id.textView2);
                joiningfees = view.findViewById(R.id.joiningfees);
                annualfees = view.findViewById(R.id.annualfees);
                viewdetails = view.findViewById(R.id.viewdetails);
                reli = view.findViewById(R.id.imageView);
                relit = view.findViewById(R.id.relit);

            }
        }

        @Override
        public Share_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_card_list_adapter, parent, false);

            return new Share_Adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(Share_Adapter.MyViewHolder holder, final int position) {

            holder.tv1.setText(list_car.get(position).getName());
            holder.tv2.setText(list_car.get(position).getId());
            holder.joiningfees.setText(list_car.get(position).getJoiningfees() + " times");
            holder.annualfees.setText(list_car.get(position).getAnnualfees());
            holder.viewdetails.setText(list_car.get(position).getCard_state());

            Picasso.get()
                    .load(list_car.get(position).getImage())
                    .into(holder.reli);


        }

        @Override
        public int getItemCount() {
            return list_car.size();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.line1:
                Intent intent2 = new Intent(CreditCardHistory.this, Dashboard.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.line3:
                Intent intent3 = new Intent(CreditCardHistory.this, OfferlistingPge.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.line5:
                Intent intent4 = new Intent(CreditCardHistory.this, Profilepage.class);
                startActivity(intent4);
                finish();
                break;
        }
    }


}
