package com.ahqlab.xvic.fragment.swing;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.constant.XvicConstant;
import com.ahqlab.xvic.databinding.FragmentSwingSettingBinding;
import com.ahqlab.xvic.domain.CircleProgress;

import java.util.ArrayList;
import java.util.List;

public class SwingSettingFragment extends BaseFragment<SwingSettingFragment> {
    FragmentSwingSettingBinding binding;
    private boolean backState = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_swing_setting, container, false);
        setTitle("표준 스윙 설정");
        binding.setFragment(this);

        List<CircleProgress> steps = new ArrayList<>();
        CircleProgress step = CircleProgress.builder().startColor(Color.parseColor("#f9ca87")).endColor(Color.parseColor("#f17d7e")).alpha(50).type(CircleProgress.PROGRESS_TYPE).build();
        steps.add(step);
        step = CircleProgress.builder().
                startColor(Color.parseColor("#c7faa0"))
                .endColor(Color.parseColor("#21faa3"))
                .startImgColor(Color.parseColor("#fafaa0"))
                .endImgColor(Color.parseColor("#02faa4"))
                .alpha(50)
                .type(CircleProgress.NORMAL_TYPE).build();
        steps.add(step);
        step = CircleProgress.builder().imageResorce(R.drawable.finish_icon).startColor(Color.parseColor("#c7faa0")).endColor(Color.parseColor("#21faa3")).alpha(50).type(CircleProgress.NORMAL_TYPE).build();
        steps.add(step);
        binding.circleProgressView.setStep(steps);

//        binding.circleProgressView.setImage(R.drawable.address, Color.parseColor("#c7faa0"), Color.parseColor("#21faa3"), 50);
//
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                binding.circleProgressView.changeStep();

//                binding.circleProgressView.setProgressState(false);
//                binding.circleProgressView.changeCircleColor(Color.parseColor("#c7faa0"), Color.parseColor("#21faa3"));
//                binding.circleProgressView.changeImageColor( Color.parseColor("#fafaa0"), Color.parseColor("#02faa4") );
//                binding.swingSettingInfo.setText("표준 스윙을 시작합니다\n기준 자세에서 2초간 유지해주세요.");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.circleProgressView.changeStep();
//                        binding.circleProgressView.changeImage(R.drawable.finish_icon);
//                        binding.swingSettingInfo.setText("표준 스윙 설정이 완료되었습니다.");
                    }
                }, 10000);
            }
        }, 10000);
        int level = getArguments().getInt(XvicConstant.SWING_LEVEL_KEY);
        Log.e(TAG, String.format("level : %d", level));
        backState = false;
        return binding.getRoot();
    }
    public static Fragment newInstance() {
        return new SwingSettingFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    public void onClick (View v) {
        if ( v.equals(binding.backBtn) ) {
            getActivity().onBackPressed();
        }
    }
}
