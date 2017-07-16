package com.uwaterloo.proxtimeity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Naima on 2017-07-15.
 */

public class CompletedPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CompletedPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                CompletedTimeRemindersFragment tab1 = new CompletedTimeRemindersFragment();
                return tab1;
            case 1:
                CompletedLocationRemindersFragment tab2 = new CompletedLocationRemindersFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
