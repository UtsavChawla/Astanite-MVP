package com.android.launcher3;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class uIntro1 extends AppCompatActivity {

    TextView textView;
    ProgressBar load;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_intro1);

        textView = findViewById(R.id.text);
        next = findViewById(R.id.goButton);
        Typeface poppins = Typeface.createFromAsset(getAssets(), "poppins.ttf");
        textView.setTypeface(poppins);
        next.setTypeface(poppins);

        load = findViewById(R.id.load);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setVisibility(View.INVISIBLE);
                load.setVisibility(View.VISIBLE);
                next.setVisibility(View.INVISIBLE);
                Intent myIntent = new Intent(uIntro1.this, uIntro2.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        textView.setVisibility(View.VISIBLE);
        load.setVisibility(View.INVISIBLE);
        next.setVisibility(View.VISIBLE);
        super.onResume();
    }

}
