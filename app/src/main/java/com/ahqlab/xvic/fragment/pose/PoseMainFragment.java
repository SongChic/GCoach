package com.ahqlab.xvic.fragment.pose;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseActivity;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.databinding.FragmentPoseMainBinding;
import com.ahqlab.xvic.fragment.swing.SwingSelectFragment;

public class PoseMainFragment extends BaseFragment {
    private FragmentPoseMainBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pose_main, container, false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.pose_fragment_wrap, new PoseCorrectFragment());
        transaction.commit();

        return binding.getRoot();
    }

    public static Fragment newInstance() {
        return new PoseMainFragment();
    }
}
