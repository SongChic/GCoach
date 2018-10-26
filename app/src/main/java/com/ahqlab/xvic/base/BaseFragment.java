package com.ahqlab.xvic.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahqlab.xvic.fragment.pose.PoseCorrectFragment;
import com.ahqlab.xvic.fragment.pose.PoseSettingFragment;
import com.ahqlab.xvic.fragment.setting.SettingFragment;
import com.ahqlab.xvic.fragment.swing.SwingPracticeFragment;
import com.ahqlab.xvic.fragment.swing.SwingSelectFragment;
import com.ahqlab.xvic.fragment.swing.SwingSettingFragment;

public abstract class BaseFragment<D extends Fragment> extends Fragment {
    protected final String TAG = getClass().getSimpleName();
    private String mTitle;
    public void setTitle (String title) {
        mTitle = title;
        if ( getActivity() == null ) return;
        ((BaseActivity) getActivity()).setTitle(title);
        setBackButton();
    }
    public String getTitle () {
        return mTitle;
    }
    public void setTitleStr (String title) {
        mTitle = title;
    }
    public void onFragmentSelected( BaseActivity activity ) {
        activity.setTitle(this.mTitle);
    }
    public void onFragmentSelected( ) {
    }
    public void setBackButton() {
        BaseFragment fragment = this;
        Log.e(TAG, String.format("instance of : %b", fragment instanceof PoseCorrectFragment));
        if ( getActivity() == null ) return;
        if ( fragment instanceof SwingSelectFragment || fragment instanceof PoseCorrectFragment || fragment instanceof SettingFragment)
            ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        else
            ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
