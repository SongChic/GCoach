package com.ahqlab.xvic.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahqlab.xvic.fragment.pose.PoseCorrectFragment;
import com.ahqlab.xvic.fragment.swing.SwingPracticeFragment;
import com.ahqlab.xvic.fragment.swing.SwingSelectFragment;
import com.ahqlab.xvic.fragment.swing.SwingSettingFragment;

public abstract class BaseFragment<D extends Fragment> extends Fragment {
    protected final String TAG = getClass().getSimpleName();
    private String mTitle;
    public void setTitle (String title) {
        mTitle = title;
        ((BaseActivity) getActivity()).setTitle(title);
    }
    public String getTitle () {
        return mTitle;
    }
    public void onFragmentSelected( BaseActivity activity ) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if ( this instanceof SwingSelectFragment || this instanceof SwingSettingFragment || this instanceof SwingPracticeFragment)
            ((BaseActivity) getActivity()).getFragments()[0] = this;
        else if ( this instanceof PoseCorrectFragment)
            ((BaseActivity) getActivity()).getFragments()[0] = this;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    //    public abstract void onFragmentSelected();
}
