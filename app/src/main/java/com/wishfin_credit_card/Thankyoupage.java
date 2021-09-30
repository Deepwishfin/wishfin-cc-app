package com.wishfin_credit_card;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

public class Thankyoupage extends Activity {

    String strcardname = "", strannualfees = "", strjoiningfees = "", lead_id = "", bank_code = "";
    String imagepath = "";
    TextView cardname;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyoupage);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                strcardname = "";
                imagepath = "";
                strannualfees = "";
                strjoiningfees = "";
                lead_id = "";
                bank_code = "";
            } else {
                strcardname = extras.getString("cardname");
                imagepath = extras.getString("imagepath");
                strannualfees = extras.getString("annual");
                strjoiningfees = extras.getString("joining");
                lead_id = extras.getString("lead_id");
                bank_code = extras.getString("bank_code");
            }
        } else {
            strcardname = (String) savedInstanceState.getSerializable("cardname");
            imagepath = (String) savedInstanceState.getSerializable("imagepath");
            strannualfees = (String) savedInstanceState.getSerializable("annual");
            strjoiningfees = (String) savedInstanceState.getSerializable("joining");
            lead_id = (String) savedInstanceState.getSerializable("lead_id");
            bank_code = (String) savedInstanceState.getSerializable("bank_code");
        }

        imageView=findViewById(R.id.imageView);
        cardname=findViewById(R.id.textView);
        cardname.setText(strcardname);

        Picasso.get()
                .load(imagepath)
                .into(imageView);

    }
}
