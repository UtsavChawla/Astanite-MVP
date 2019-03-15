package com.android.launcher3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.launcher3.allapps.search.SoundExClass;
import com.utsav.mConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class uIntro2 extends AppCompatActivity {

    ProgressBar load;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_intro2);

        recyclerView = findViewById(R.id.appsList);
        final uRAdapter radapter = new uRAdapter(this);
        recyclerView.setAdapter(radapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView description = findViewById(R.id.description);
        load = findViewById(R.id.load);

        Typeface roboto = Typeface.createFromAsset(getAssets(), "robotolight.ttf");
        description.setTypeface(roboto);

        final ImageButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nFlaggedApps = 0;
                List<uAppInfo> ListCopy = radapter.appsList;

                for(uAppInfo temp: ListCopy)
                {
                    if(temp.flag)
                        nFlaggedApps++;
                }

                Log.e( "onClick: ", Integer.toString(nFlaggedApps) );

                if(nFlaggedApps>=2)
                {

                    Set<String> flagpackages = new HashSet<>();
                    Set<String> flagtitles = new HashSet<>();
                    Set<String> flagsoundex = new HashSet<>();

                    for(uAppInfo instance : ListCopy)
                    {
                        if(instance.flag)
                        {
                            flagpackages.add(instance.packageName.toLowerCase());
                            flagtitles.add(instance.label.toLowerCase());
                            flagsoundex.add(SoundExClass.getGode(instance.label));
                        }
                    }

                    SharedPreferences.Editor editing = getSharedPreferences(mConstants.Sharedprefname, Context.MODE_PRIVATE).edit();

                    editing.remove(mConstants.flaggedpackagekey).apply();
                    editing.remove(mConstants.flaggedtitlekey).apply();
                    editing.remove(mConstants.flaggedsoundextitle).apply();

                    editing.putStringSet(mConstants.flaggedpackagekey, flagpackages).apply();
                    editing.putStringSet(mConstants.flaggedtitlekey, flagtitles).apply();
                    editing.putStringSet(mConstants.flaggedsoundextitle, flagsoundex).apply();

                    load.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(uIntro2.this, uIntro3.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Select at least 2 apps", Toast.LENGTH_SHORT).show();
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
