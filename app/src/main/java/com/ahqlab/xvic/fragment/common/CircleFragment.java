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
import com.ahqlab.xvic.constant.XvicConstant;
import com.ahqlab.xvic.databinding.FragmentCircleBinding;
import com.ahqlab.xvic.domain.CircleProgress;

import java.util.ArrayList;
import java.util.List;

public class CircleFragment extends BaseFragment<CircleFragment> {

    private int level = 0;
    private FragmentCircleBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_circle, container, false);
        level = getArguments().getInt(XvicConstant.SWING_LEVEL_KEY);
        setTitle( getArguments().getString(XvicConstant.FRAGMENT_TITLE_KEY) );
        binding.infoText.setText(getArguments().getString(XvicConstant.INFO_TEXT_ARGUMENT_KEY));
        List<CircleProgress> data = (ArrayList<CircleProgress>) getArguments().getSerializable(XvicConstant.CIRCLE_DATA_KEY);
        binding.circleProgressView.setStep(data);

        return binding.getRoot();
    }
}
