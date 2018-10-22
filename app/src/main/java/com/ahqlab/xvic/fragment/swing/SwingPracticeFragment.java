package com.ahqlab.xvic.fragment.swing;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.databinding.FragmentSwingPracticeBinding;
import com.ahqlab.xvic.domain.CircleProgress;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SwingPracticeFragment extends BaseFragment<SwingPracticeFragment> {
    private FragmentSwingPracticeBinding binding;
    private TextToSpeech tts;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setTitle("스윙 연습 모드");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_swing_practice, container, false);

        ArrayList<CircleProgress> data = (ArrayList<CircleProgress>) getArguments().getSerializable("test");

        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
            }
        });

        List<CircleProgress> steps = new ArrayList<>();
        CircleProgress step = CircleProgress.builder().step(1).alpha(1).type(CircleProgress.PROGRESS_TYPE).build();
        steps.add(step);
        step = CircleProgress.builder().step(2).type(CircleProgress.ANIMATION_TYPE).btnImageResource(
                new int[] {
                        R.drawable.play,
                        R.drawable.pause
                }
        ).size(CircleProgress.TINY_SIZE).onClick(new CircleProgress.ImageOnClick() {
            @Override
            public void onClick(boolean state) {

            }
        }).build();
        steps.add(step);
        binding.circleProgressView.setStep(steps);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.circleProgressView.changeStep();
            }
        }, 2000);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tts.shutdown();
    }
}
