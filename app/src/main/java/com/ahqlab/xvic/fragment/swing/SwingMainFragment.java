package com.ahqlab.xvic.fragment.swing;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.databinding.FragmentSwingMainBinding;

public class SwingMainFragment extends BaseFragment {
    private FragmentSwingMainBinding binding;
    public static Fragment newInstance() {
        return new SwingMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_swing_main, container, false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.swing_root, new SwingSelectFragment());
        transaction.commit();
        return binding.getRoot();
    }
}