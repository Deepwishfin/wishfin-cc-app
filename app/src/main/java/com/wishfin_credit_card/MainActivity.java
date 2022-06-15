package com.wishfin_credit_card;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class MainActivity extends AppCompatActivity {

    TextView signup, login;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        SpringDotsIndicator springDotsIndicator = findViewById(R.id.spring_dots_indicator);

        ViewPager viewPager = findViewById(R.id.view_pager);
        DotIndicatorPagerAdapter adapter = new DotIndicatorPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        springDotsIndicator.setViewPager(viewPager);

        login = findViewById(R.id.signin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Loginpage.class);
                intent.putExtra("login_type", "login");
                startActivity(intent);
                finish();
            }
        });


    }


}