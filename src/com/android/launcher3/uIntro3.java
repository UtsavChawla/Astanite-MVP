package com.android.launcher3;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class uIntro3 extends AppCompatActivity implements utab1.OnFragmentInteractionListener, utab2.OnFragmentInteractionListener, utab3.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_intro3);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        final uPagerAdapter adapter = new uPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
