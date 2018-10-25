package com.ahqlab.xvic.fragment.common;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.databinding.FragmentResultBinding;
import com.ahqlab.xvic.domain.CircleProgress;
import com.ahqlab.xvic.util.XvicUtil;
import com.ahqlab.xvic.view.CircleProgressView;

public class ResultFragment extends BaseFragment<ResultFragment> {
    private FragmentResultBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false);
        int selected = getArguments().getInt("selected");


        CircleProgress step = CircleProgress.builder().padding(XvicUtil.dpToPx(130)).type(CircleProgress.TEXT_TYPE).text("UNDER").build();
        binding.under.setStep(step);
        step = CircleProgress.builder().padding(XvicUtil.dpToPx(130)).type(CircleProgress.TEXT_TYPE).text("NORMAL").build();
        binding.normal.setStep(step);
        step = CircleProgress.builder().padding(XvicUtil.dpToPx(130)).type(CircleProgress.TEXT_TYPE).text("OVER").build();
        binding.over.setStep(step);
        ((CircleProgressView) binding.levelWrap.getChildAt(selected)).setSelected(true);
        return binding.getRoot();
    }
}
