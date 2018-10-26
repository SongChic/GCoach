package com.ahqlab.xvic.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.fragment.pose.PoseMainFragment;
import com.ahqlab.xvic.fragment.setting.SettingMainFragment;
import com.ahqlab.xvic.fragment.swing.SwingMainFragment;
import com.ahqlab.xvic.fragment.swing.SwingSelectFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private int mTabCount;
    private BaseFragment mFragment;
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
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        mFragment = (BaseFragment) object;
    }

    @Override
    public int getCount() {
        return mTabCount;
    }
    public void setFragment( BaseFragment fragment ) {
        mFragment = fragment;
    }
    public void setUserVisibleHint(boolean isUserVisibleHint)
    {

    }

}
