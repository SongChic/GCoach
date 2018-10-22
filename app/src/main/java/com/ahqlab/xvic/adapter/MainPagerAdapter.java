package com.ahqlab.xvic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ahqlab.xvic.fragment.pose.PoseMainFragment;
import com.ahqlab.xvic.fragment.setting.SettingMainFragment;
import com.ahqlab.xvic.fragment.swing.SwingMainFragment;
import com.ahqlab.xvic.fragment.swing.SwingSelectFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private int mTabCount;
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public MainPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        mTabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SwingMainFragment.newInstance();
            case 1:
                return PoseMainFragment.newInstance();
            case 2:
                return SettingMainFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }
}
