package com.ahqlab.xvic.base;

import android.support.v4.app.Fragment;

public abstract class BaseFragment<D extends Fragment> extends Fragment {
    protected final String TAG = getClass().getSimpleName();
    public void setTitle (String title) {
        ((BaseActivity) getActivity()).setTitle(title);
    }
}
