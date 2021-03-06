package com.ahqlab.xvic.fragment.swing;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseActivity;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.databinding.FragmentSwingMainBinding;

import java.util.List;

public class SwingMainFragment extends BaseFragment {
    private FragmentSwingMainBinding binding;
    private View wrap;
    public static Fragment newInstance() {
        return new SwingMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if ( wrap == null ) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_swing_main, container, false);
            ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            SwingSelectFragment fragment = new SwingSelectFragment();
//            BaseFragment fragment = ((BaseActivity) getActivity()).getFragments()[0];
            transaction.replace(R.id.swing_root, fragment);
            transaction.commit();
            wrap = binding.getRoot();
        }

        return wrap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(getFragmentManager().findFragmentById(R.id.swing_root)).commit();
        ((BaseActivity) getActivity()).getFragments()[0] = new SwingSelectFragment();
    }

}
