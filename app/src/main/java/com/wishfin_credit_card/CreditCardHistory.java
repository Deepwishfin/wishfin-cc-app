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
    RelativeLayout backreli;
    TextView heading_cc_list, sub_heading_cc_list,bar5;
    String bank_code = "";


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
        bar5 = findViewById(R.id.bar5);
        backreli = findViewById(R.id.backreli);

        backreli.setOnClickListener(new View.OnClickListener() {
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
            TextView tv1, tv2, joiningfees, annualfees, viewdetails, applied;
            ImageView reli;
            RelativeLayout relit;

            MyViewHolder(View view) {
                super(view);
                tv1 = view.findViewById(R.id.textView);
                tv2 = view.findViewById(R.id.textView2);
                joiningfees = view.findViewById(R.id.joiningfees);
                annualfees = view.findViewById(R.id.annualfees);
                viewdetails = view.findViewById(R.id.viewdetails);
                applied = view.findViewById(R.id.applied);
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
//            holder.viewdetails.setText(list_car.get(position).getCard_state());

            Picasso.get()
                    .load(list_car.get(position).getImage())
                    .into(holder.reli);

            if (list_car.get(position).getCard_state().equalsIgnoreCase("Applied")) {
                holder.viewdetails.setVisibility(View.GONE);
                holder.applied.setVisibility(View.VISIBLE);
                holder.applied.setText("Applied");
            } else if (list_car.get(position).getCard_state().equalsIgnoreCase("Pending")) {
                holder.viewdetails.setVisibility(View.GONE);
                holder.applied.setVisibility(View.VISIBLE);
                holder.applied.setText("Pending");
            } else {
                holder.viewdetails.setVisibility(View.VISIBLE);
                holder.applied.setVisibility(View.GONE);
            }
            holder.viewdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressDialog.show();
                    bank_code = list_car.get(position).getBank_code();
                    get_encrypted_link(list_car.get(position).getBank_code(), list_car.get(position).getImage(), list_car.get(position).getName());
                }
            });

            holder.reli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreditCardHistory.this, CardDetailPage.class);
                    intent.putExtra("bank_code", "" + list_car.get(position).getBank_code());
                    intent.putExtra("status", "" + list_car.get(position).getCard_state());
                    intent.putExtra("cardname", "" + list_car.get(position).getName());
//                            intent.putExtra("imagepath", "" + list_car.get(position).getImage());
//                            intent.putExtra("features", "" + list_car.get(position).getFeauters());
//                            intent.putExtra("joining", "" + list_car.get(position).getJoiningfees());
//                            intent.putExtra("annual", "" + list_car.get(position).getAnnualfees());
//                            intent.putExtra("insta_apply_link", "" + list_car.get(position).getInsta_apply_link());
                    startActivity(intent);
                }
            });

            holder.tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreditCardHistory.this, CardDetailPage.class);
                    intent.putExtra("bank_code", "" + list_car.get(position).getBank_code());
                    intent.putExtra("status", "" + list_car.get(position).getCard_state());
                    intent.putExtra("cardname", "" + list_car.get(position).getName());
//                            intent.putExtra("imagepath", "" + list_car.get(position).getImage());
//                            intent.putExtra("features", "" + list_car.get(position).getFeauters());
//                            intent.putExtra("joining", "" + list_car.get(position).getJoiningfees());
//                            intent.putExtra("annual", "" + list_car.get(position).getAnnualfees());
//                            intent.putExtra("insta_apply_link", "" + list_car.get(position).getInsta_apply_link());
                    startActivity(intent);
                }
            });


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

    public void get_encrypted_link(String bank_code, String imagepath, String strcardname) {

        final JSONObject json = new JSONObject();
        try {
            json.put("auth_key", BuildConfig.oAuthdeal4loans);
            json.put("method", "EncodeString");
            json.put("text", "product=CC&bank_code=" + bank_code + "&device_type=android&cid=" +
                    SessionManager.get_cibil_id(prefs) + "&mobile=" + SessionManager.get_mobile(prefs) +
                    "&card_image=" + imagepath + "&card_name=" + strcardname);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, getString(R.string.BASE_URL_Deal4Loans), json,
                response -> {
                    try {

                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.getString("message").equalsIgnoreCase("Success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String url = "https://www.deal4loans.com/wfapppage.php?" + jsonObject1.getString("encoded_text");

                            Intent intent = new Intent(CreditCardHistory.this, WebviewActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("bank_code", bank_code);
                            intent.putExtra("imagepath", imagepath);
                            intent.putExtra("strcardname", strcardname);
                            startActivity(intent);

//                            Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                            startActivity(openUrlIntent);
                            progressDialog.dismiss();
                        } else {
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

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.show();
//        getaouth();
        get_card_list();
        get_cibil_history();

    }

    public void get_cibil_history() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.BASE_URL) + "/historic-score?mobile=" + SessionManager.get_mobile(prefs);
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
