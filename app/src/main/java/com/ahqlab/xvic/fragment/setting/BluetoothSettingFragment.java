package com.ahqlab.xvic.fragment.setting;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.databinding.FragmentBluetoothSettingBinding;
import com.ahqlab.xvic.domain.CircleProgress;

import java.util.ArrayList;
import java.util.List;

public class BluetoothSettingFragment extends BaseFragment<BluetoothSettingFragment> {
    private FragmentBluetoothSettingBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bluetooth_setting, container, false);
        List<CircleProgress> steps = new ArrayList<>();
        CircleProgress step = CircleProgress.builder().imageResorce(R.drawable.bluetooth_icon).startColor(Color.parseColor("#c7faa0")).endColor(Color.parseColor("#21faa3")).alpha(50).type(CircleProgress.NORMAL_TYPE).size(CircleProgress.SMALL_SIZE).build();
        steps.add(step);
        binding.circleProgressView.setStep(steps);
        return binding.getRoot();
    }
}
