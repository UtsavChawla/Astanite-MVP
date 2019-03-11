package com.android.launcher3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class uPagerAdapter extends FragmentStatePagerAdapter {
    public uPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: utab1 t1 = new utab1();
                return t1;
            case 1: utab2 t2 = new utab2();
                return t2;
            case 2: utab3 t3 = new utab3();
                return t3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
