package com.google.android.apps.nexuslauncher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Process;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;

import com.android.launcher3.AppInfo;
import com.android.launcher3.Launcher;
import com.android.launcher3.R;
import com.android.launcher3.Utilities;
import com.android.launcher3.compat.WallpaperManagerCompat;
import com.android.launcher3.config.FeatureFlags;
import com.android.launcher3.uIntro1;
import com.android.launcher3.util.ComponentKeyMapper;
import com.android.launcher3.util.ViewOnDrawExecutor;
import com.google.android.libraries.gsa.launcherclient.LauncherClient;
import com.utsav.mConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.security.AccessController.getContext;

public class NexusLauncherActivity extends Launcher {
    private final static String PREF_IS_RELOAD = "pref_reload_workspace";
    private NexusLauncher mLauncher;
    private boolean mIsReload;
    private String mThemeHints;

    public AlertDialog alertDialog;

    public NexusLauncherActivity() {
        mLauncher = new NexusLauncher(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: " );
        FeatureFlags.QSB_ON_FIRST_SCREEN = showSmartspace();
        mThemeHints = themeHints();

        SharedPreferences prefs = Utilities.getPrefs(this);
        if (!PixelBridge.isInstalled(this)) {
            prefs.edit().putBoolean(SettingsActivity.ENABLE_MINUS_ONE_PREF, false).apply();
        }

        super.onCreate(savedInstanceState);

        if (mIsReload = prefs.getBoolean(PREF_IS_RELOAD, false)) {
            prefs.edit().remove(PREF_IS_RELOAD).apply();

            // Go back to overview after a reload
            showOverviewMode(false);

            // Fix for long press not working
            // This is overwritten in Launcher.onResume
            setWorkspaceLoading(false);
        }

        if(getApplicationContext().getSharedPreferences(mConstants.Sharedprefname,MODE_PRIVATE).getBoolean(mConstants.intro,true))
        {
            startActivity(new Intent(NexusLauncherActivity.this, uIntro1.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isMyLauncherDefault())
        {
            if(!getApplicationContext().getSharedPreferences(mConstants.Sharedprefname, MODE_PRIVATE).getBoolean(mConstants.intro,true))
            {
                Log.e(TAG, "Take action " );

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(NexusLauncherActivity.this, R.style.DialogAlert));
                builder.setTitle("Get Started")
                        .setMessage("Enable Astanite as your default Launcher/Home App." + '\n')
                        .setIcon(R.drawable.intrologo)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    @Override
    protected void onPause() {
        if(getSharedPreferences(mConstants.Sharedprefname, MODE_PRIVATE).getBoolean(mConstants.intofinal,true))
        {
            Log.e(TAG, "onPause: " );
            Set<String> packnames = getSharedPreferences(mConstants.Sharedprefname, MODE_PRIVATE).getStringSet(mConstants.flaggedpackagekey, new HashSet<String>());

            for(String temp : packnames)
            {
                getWorkspace().removeAbandonedPromise(temp, Process.myUserHandle());
            }

            getSharedPreferences(mConstants.Sharedprefname, MODE_PRIVATE).edit().putBoolean(mConstants.intofinal, false).apply();
        }
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        boolean themeChanged = !mThemeHints.equals(themeHints());
        if (FeatureFlags.QSB_ON_FIRST_SCREEN != showSmartspace() || themeChanged) {
            if (themeChanged) {
                WallpaperManagerCompat.getInstance(this).updateAllListeners();
            }
            Utilities.getPrefs(this).edit().putBoolean(PREF_IS_RELOAD, true).apply();
            recreate();
        }
    }

    @Override
    public void recreate() {
        if (Utilities.ATLEAST_NOUGAT) {
            super.recreate();
        } else {
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public void clearPendingExecutor(ViewOnDrawExecutor executor) {
        super.clearPendingExecutor(executor);
        if (mIsReload) {
            mIsReload = false;

            // Call again after the launcher has loaded for proper states
            showOverviewMode(false);

            // Strip empty At A Glance page
            getWorkspace().stripEmptyScreens();
        }
    }

    private boolean showSmartspace() {
        return Utilities.getPrefs(this).getBoolean(SettingsActivity.SMARTSPACE_PREF, true);
    }

    private String themeHints() {
        return Utilities.getPrefs(this).getString(Utilities.THEME_OVERRIDE_KEY, "");
    }

    @Override
    public void overrideTheme(boolean isDark, boolean supportsDarkText, boolean isTransparent) {
        int flags = Utilities.getDevicePrefs(this).getInt(NexusLauncherOverlay.PREF_PERSIST_FLAGS, 0);
        int orientFlag = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 16 : 8;
        boolean useGoogleInOrientation = (orientFlag & flags) != 0;
        supportsDarkText &= Utilities.ATLEAST_NOUGAT;
        if (useGoogleInOrientation && isDark) {
            setTheme(R.style.GoogleSearchLauncherThemeDark);
        } else if (useGoogleInOrientation && supportsDarkText) {
            setTheme(R.style.GoogleSearchLauncherThemeDarkText);
        } else if (useGoogleInOrientation && isTransparent) {
            setTheme(R.style.GoogleSearchLauncherThemeTransparent);
        } else if (useGoogleInOrientation) {
            setTheme(R.style.GoogleSearchLauncherTheme);
        } else {
            super.overrideTheme(isDark, supportsDarkText, isTransparent);
        }
    }

    public List<ComponentKeyMapper<AppInfo>> getPredictedApps() {
        return mLauncher.mCallbacks.getPredictedApps();
    }

    public LauncherClient getGoogleNow() {
        return mLauncher.mClient;
    }

    boolean isMyLauncherDefault() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);

        final String myPackageName = getPackageName();
        List<ComponentName> activities = new ArrayList<ComponentName>();
        final PackageManager packageManager = (PackageManager) getPackageManager();

        // You can use name of your package here as third argument
        packageManager.getPreferredActivities(filters, activities, null);

        for (ComponentName activity : activities) {
            if (myPackageName.equals(activity.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
