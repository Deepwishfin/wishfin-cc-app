package com.wishfin_credit_card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class OfferlistingPge extends Activity implements View.OnClickListener {

    LinearLayout line1, line2, line3, line5;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offerlistingpage);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line5 = findViewById(R.id.line5);

        line1.setOnClickListener(this);
        line2.setOnClickListener(this);
        line3.setOnClickListener(this);
        line5.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.line1:
                Intent intent2 = new Intent(OfferlistingPge.this, Dashboard.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.line2:
                Intent intent3 = new Intent(OfferlistingPge.this, ExploreCreditCard.class);
                intent3.putExtra("type", "ExploreAll");
                startActivity(intent3);
                break;
            case R.id.line5:
                Intent intent4 = new Intent(OfferlistingPge.this, Profilepage.class);
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
