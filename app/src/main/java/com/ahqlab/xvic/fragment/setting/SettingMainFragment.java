package com.ahqlab.xvic.fragment.setting;

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
import com.ahqlab.xvic.databinding.FragmentSettingMainBinding;
import com.ahqlab.xvic.fragment.swing.SwingSelectFragment;

public class SettingMainFragment extends BaseFragment {
    private FragmentSettingMainBinding binding;

    public static Fragment newInstance() {
        return new SettingMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_main, container, false);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.setting_fragment_wrap, new SettingFragment());
        ft.commit();
        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(getFragmentManager().findFragmentById(R.id.setting_fragment_wrap)).commit();
        ((BaseActivity) getActivity()).getFragments()[2] = new SettingFragment();
    }

}
