package com.android.launcher3.popup;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.launcher3.AbstractFloatingView;
import com.android.launcher3.InfoDropTarget;
import com.android.launcher3.ItemInfo;
import com.android.launcher3.Launcher;
import com.android.launcher3.R;
import com.android.launcher3.allapps.search.SoundExClass;
import com.android.launcher3.model.WidgetItem;
import com.android.launcher3.util.PackageUserKey;
import com.android.launcher3.widget.WidgetsBottomSheet;
import com.utsav.mConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.android.launcher3.userevent.nano.LauncherLogProto.Action;
import static com.android.launcher3.userevent.nano.LauncherLogProto.ControlType;

/**
 * Represents a system shortcut for a given app. The shortcut should have a static label and
 * icon, and an onClickListener that depends on the item that the shortcut services.
 *
 * Example system shortcuts, defined as inner classes, include Widgets and AppInfo.
 */
public abstract class SystemShortcut extends ItemInfo {
    private final int mIconResId;
    private int mLabelResId;

    public SystemShortcut(int iconResId, int labelResId) {
        mIconResId = iconResId;
        mLabelResId = labelResId;
    }

    public Drawable getIcon(Context context) {
        return context.getResources().getDrawable(mIconResId, context.getTheme());
    }

    public String getLabel(Context context) {
        return context.getString(mLabelResId);
    }

    public abstract View.OnClickListener getOnClickListener(final Launcher launcher,
            final ItemInfo itemInfo);

    public static class Custom extends SystemShortcut {

        public Custom() {
            super(R.drawable.ic_edit_no_shadow, R.string.action_preferences);
        }

        @Override
        public View.OnClickListener getOnClickListener(Launcher launcher, ItemInfo itemInfo) {
            return null;
        }
    }

    public static class Widgets extends SystemShortcut {

        public Widgets() {
            super(R.drawable.ic_widget, R.string.widget_button_text);
        }

        @Override
        public View.OnClickListener getOnClickListener(final Launcher launcher,
                final ItemInfo itemInfo) {
            final List<WidgetItem> widgets = launcher.getWidgetsForPackageUser(new PackageUserKey(
                    itemInfo.getTargetComponent().getPackageName(), itemInfo.user));
            if (widgets == null) {
                return null;
            }
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AbstractFloatingView.closeAllOpenViews(launcher);
                    WidgetsBottomSheet widgetsBottomSheet =
                            (WidgetsBottomSheet) launcher.getLayoutInflater().inflate(
                                    R.layout.widgets_bottom_sheet, launcher.getDragLayer(), false);
                    widgetsBottomSheet.populateAndShow(itemInfo);
                    launcher.getUserEventDispatcher().logActionOnControl(Action.Touch.TAP,
                            ControlType.WIDGETS_BUTTON, view);
                }
            };
        }
    }

    public static class AppInfo extends SystemShortcut {

        private Context mContext;

        public AppInfo(Context temp) {
            super(R.drawable.ic_info_no_shadow, R.string.app_info_drop_target_label);
            mContext = temp;
        }

        @Override
        public View.OnClickListener getOnClickListener(final Launcher launcher,
                final ItemInfo itemInfo) {
            final String title = itemInfo.title.toString().toLowerCase();
            final String packname = itemInfo.getTargetComponent().getPackageName().toLowerCase();

            final SharedPreferences preferences = mContext.getSharedPreferences(mConstants.Sharedprefname, Context.MODE_PRIVATE);
            final Set<String> titleset = preferences.getStringSet(mConstants.flaggedtitlekey, new HashSet<String>());
            final Set<String> packnameset = preferences.getStringSet(mConstants.flaggedpackagekey, new HashSet<String>());
            final Set<String> flaggedsoundex = preferences.getStringSet(mConstants.flaggedsoundextitle, new HashSet<String>());

            final Boolean toflag;

            if(titleset.contains(title))
            {
                toflag = false;
                super.mLabelResId = R.string.unflag_app;
            }
            else
            {
                toflag = true;
                super.mLabelResId = R.string.flag_app;
            }

            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AbstractFloatingView.closeAllOpenViews(launcher);
                    if(toflag)
                    {
                        titleset.add(title);
                        packnameset.add(packname);
                        flaggedsoundex.add(SoundExClass.getGode(title));
                        launcher.getWorkspace().removeAbandonedPromise(packname, user);
                    }
                    else
                    {
                        titleset.remove(title);
                        packnameset.remove(packname);
                    }

                    SharedPreferences.Editor editing = preferences.edit();

                    editing.remove(mConstants.flaggedpackagekey).apply();
                    editing.remove(mConstants.flaggedtitlekey).apply();
                    editing.remove(mConstants.flaggedsoundextitle).apply();

                    editing.putStringSet(mConstants.flaggedpackagekey, packnameset).apply();
                    editing.putStringSet(mConstants.flaggedtitlekey, titleset).apply();
                    editing.putStringSet(mConstants.flaggedsoundextitle, flaggedsoundex).apply();

                    launcher.launcherflagclick();

                    //Rect sourceBounds = launcher.getViewBounds(view);
                    //Bundle opts = launcher.getActivityLaunchOptions(view);
                    //InfoDropTarget.startDetailsActivityForInfo(itemInfo, launcher, null, sourceBounds, opts);
                    //launcher.getUserEventDispatcher().logActionOnControl(Action.Touch.TAP,
                    //ControlType.APPINFO_TARGET, view);
                }
            };
        }
    }
}
