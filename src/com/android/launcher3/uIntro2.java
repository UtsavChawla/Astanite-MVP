package com.android.launcher3;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class uIntro2 extends AppCompatActivity {

    ProgressBar load;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_intro2);

        recyclerView = findViewById(R.id.appsList);
        uRAdapter radapter = new uRAdapter(this);
        recyclerView.setAdapter(radapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView description = findViewById(R.id.description);
        load = findViewById(R.id.load);

        Typeface roboto = Typeface.createFromAsset(getAssets(), "robotolight.ttf");
        description.setTypeface(roboto);

        final FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                startActivity(new Intent(uIntro2.this, uIntro3.class));
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    Log.e( "onScrollStateChanged: ", "Idle" );
                    fab.show();
                }else if(newState == RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    Log.e( "onScrollStateChanged: ", "Dragging" );
                    fab.hide();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        load.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        super.onResume();
    }
}
