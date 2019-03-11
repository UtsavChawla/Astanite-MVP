package com.android.launcher3;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class uRAdapter extends RecyclerView.Adapter<uRAdapter.ViewHolder> {
    private List<uAppInfo> appsList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public ImageView img;
        public CheckBox checkBox;


        //This is the subclass ViewHolder which simply
        //'holds the views' for us to show on each row
        public ViewHolder(View itemView) {
            super(itemView);

            //Finds the views from our row.xml
            textView =  itemView.findViewById(R.id.text);
            img =  itemView.findViewById(R.id.img);
            checkBox = itemView.findViewById(R.id.checkbox);

            Typeface roboto = Typeface.createFromAsset(context.getAssets(), "robotolight.ttf");
            textView.setTypeface(roboto);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick (View v) {
            int pos = getAdapterPosition();

            appsList.get(pos).flag = !appsList.get(pos).flag;
            checkBox.setChecked(appsList.get(pos).flag);

        }
    }



    public uRAdapter(Context c) {

        //This is where we build our list of app details, using the app
        //object we created to store the label, package name and icon

        PackageManager pm = c.getPackageManager();
        appsList = new ArrayList<>();
        context = c;

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
        for(ResolveInfo ri:allApps) {
            uAppInfo app = new uAppInfo();
            app.label = ri.loadLabel(pm).toString();
            app.packageName = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(pm);
            if(!app.label.equals("Astanite"))
            appsList.add(app);
        }

        for(int x =0; x<appsList.size(); x++)
        {
            for(int j =0; j<appsList.size(); j++)
            {
                if(appsList.get(x).label.toLowerCase().compareTo(appsList.get(j).label.toLowerCase())<0)
                {
                    uAppInfo temp = appsList.get(j);
                    appsList.set(j,appsList.get(x));
                    appsList.set(x,temp);
                }
            }
        }

    }

    @Override
    public void onBindViewHolder(uRAdapter.ViewHolder viewHolder, int i) {

        //Here we use the information in the list we created to define the views

        String appLabel = appsList.get(i).label;
        String appPackage = appsList.get(i).packageName;
        Drawable appIcon = appsList.get(i).icon;

        TextView textView = viewHolder.textView;
        textView.setText(appLabel);
        ImageView imageView = viewHolder.img;
        imageView.setImageDrawable(appIcon);

        CheckBox checkBox = viewHolder.checkBox;

            checkBox.setChecked(appsList.get(i).flag);

    }


    @Override
    public int getItemCount() {

        //This method needs to be overridden so that Androids knows how many items
        //will be making it into the list

        return appsList.size();
    }


    @Override
    public uRAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //This is what adds the code we've written in here to our target view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.urow, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
}