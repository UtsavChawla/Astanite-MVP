package com.android.launcher3;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class uIntro2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_intro2);

        RecyclerView recyclerView = findViewById(R.id.appsList);
        uRAdapter radapter = new uRAdapter(this);
        recyclerView.setAdapter(radapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView description = findViewById(R.id.description);

        Typeface roboto = Typeface.createFromAsset(getAssets(), "robotolight.ttf");
        description.setTypeface(roboto);
    }

}
