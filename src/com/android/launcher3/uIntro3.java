package com.android.launcher3;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class uIntro3 extends AppCompatActivity implements utab1.OnFragmentInteractionListener, utab2.OnFragmentInteractionListener, utab3.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_intro3);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        final uPagerAdapter adapter = new uPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        final Button nextButton = findViewById(R.id.nextButton);
        final ImageView dots = findViewById(R.id.dots);

        Typeface poppins = Typeface.createFromAsset(getAssets(), "poppins.ttf");
        nextButton.setTypeface(poppins);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager.getCurrentItem())
                {
                    case 0: viewPager.setCurrentItem(1);
                        break;
                    case 1: viewPager.setCurrentItem(2);
                        break;
                    case 2:
                        break;
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i)
                {
                    case 0: nextButton.setText("next");
                        dots.setImageResource(R.drawable.dots1);
                        break;
                    case 1: nextButton.setText("next");
                        dots.setImageResource(R.drawable.dots2);
                        break;
                    case 2: nextButton.setText("get started");
                        dots.setImageResource(R.drawable.dots3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
