package com.ahqlab.xvic.fragment.setting;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.databinding.FragmentLevelSettingBinding;

public class LevelSettingFragment extends BaseFragment<LevelSettingFragment> {
    private FragmentLevelSettingBinding binding;
    private int levelLow = Color.parseColor("#87fcd3"),
            levelMiddle = Color.parseColor("#f9ca87"),
            levelHigh = Color.parseColor("#f17d7e"),
            colorFrom;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_level_setting, container, false);
        colorFrom = levelLow;
        binding.levelSeekbar.setMax(100);
        binding.levelSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e(TAG, String.format("progress : %d", progress));
                int level = binding.levelSeekbar.getMax() / 3;
                int colorTo = 0;
                if ( progress < level ) {
                    colorTo = levelLow;
                    if ( colorTo != colorFrom ) {
                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(1000); // milliseconds
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                binding.levelSeekbar.getProgressDrawable().setColorFilter((int) animator.getAnimatedValue(), PorterDuff.Mode.SRC_IN);
                                colorFrom = levelLow;
                            }

                        });
                        colorAnimation.start();
                    }
                } else if ( progress >= level && progress < level * 2 ) {
                    colorTo = levelMiddle;
                    if ( colorTo != colorFrom ) {
                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(1000); // milliseconds
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                binding.levelSeekbar.getProgressDrawable().setColorFilter((int) animator.getAnimatedValue(), PorterDuff.Mode.SRC_IN);
                                colorFrom = levelMiddle;
                            }

                        });
                        colorAnimation.start();
                    }
                } else if ( progress >= level * 2 ) {
                    colorTo = levelHigh;
                    if ( colorTo != colorFrom ) {
                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(1000); // milliseconds
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                binding.levelSeekbar.getProgressDrawable().setColorFilter((int) animator.getAnimatedValue(), PorterDuff.Mode.SRC_IN);
                                colorFrom = levelHigh;
                            }

                        });
                        colorAnimation.start();
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return binding.getRoot();
    }
}
