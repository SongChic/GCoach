package com.ahqlab.xvic.fragment.pose;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseActivity;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.databinding.FragmentPosePracticeBinding;
import com.ahqlab.xvic.domain.CircleProgress;
import com.ahqlab.xvic.fragment.common.ResultFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PosePracticeFragment extends BaseFragment<PosePracticeFragment> {
    private FragmentPosePracticeBinding binding;
    private TextToSpeech tts;
    private String title = "자세 교정 모드";
    private View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pose_practice, container, false);
        if ( root == null ) {
            root = binding.getRoot();
            ((BaseActivity) getActivity()).getFragments()[1] = this;

            tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    tts.setLanguage(Locale.KOREAN);
                }
            });

            List<CircleProgress> steps = new ArrayList<>();
            CircleProgress step = CircleProgress.builder().step(2).type(CircleProgress.ANIMATION_TYPE).imageResorce(R.drawable.play).size(CircleProgress.TINY_SIZE).onClick(new CircleProgress.ImageOnClick() {
                @Override
                public void onClick(boolean state) {
                    binding.swingSettingInfo.setText("표준 자세를 설정합니다.\n어드레스 자세를 취해주세요.");
                    binding.circleProgressView.changeStep();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.circleProgressView.setAngle(360);
                            binding.circleProgressView.start();
                        }
                    }, 1000);
                }
            }).build();
            steps.add(step);
            step = CircleProgress.builder().imageResorce(R.drawable.address).startColor(Color.parseColor("#f9ca87")).endColor(Color.parseColor("#f17d7e")).type(CircleProgress.PROGRESS_TYPE).imgAlpha(50).angleListner(new CircleProgress.AngleCallback() {
                @Override
                public void onAngleListner(int percent) {
                    Log.e(TAG, String.format("percent : %d", percent));
                    if (percent >= 100) {
                        binding.circleProgressView.stop();
                        binding.circleProgressView.changeStep();
                        binding.swingSettingInfo.setText("표준 자세를 설정합니다.\n기준 자세에서 2초간 유지해주세요.");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.swingSettingInfo.setText("어드레스 확인 되었습니다.\n연습 스윙을 하세요.");
                                binding.circleProgressView.changeStep();
                                binding.circleProgressView.start();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Fragment fragment = new ResultFragment();
                                        Random random = new Random();
                                        int selected = random.nextInt(2);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("selected", selected);
                                        fragment.setArguments(bundle);
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.pose_fragment_wrap, fragment);
                                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                        ft.addToBackStack(null);
                                        ft.commit();
                                    }
                                }, 10000);
                            }
                        }, 2000);
                    }


//                if ( percent >= 80 ) {
//                    binding.circleProgressView.reset();
//                }
                }
            }).build();
            steps.add(step);
            step = CircleProgress.builder().
                    startColor(Color.parseColor("#c7faa0"))
                    .endColor(Color.parseColor("#21faa3"))
                    .startImgColor(Color.parseColor("#fafaa0"))
                    .endImgColor(Color.parseColor("#02faa4"))
                    .type(CircleProgress.NORMAL_TYPE).build();
            steps.add(step);
            step = CircleProgress.builder().step(2).type(CircleProgress.ANIMATION_TYPE).imageResorce(R.drawable.sound_icon).size(CircleProgress.MIDDLE_SIZE).build();
            steps.add(step);
            binding.circleProgressView.setStep(steps);
        }
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tts.shutdown();
    }

    @Override
    public void onFragmentSelected(BaseActivity activity) {
        activity.setTitle(title);
    }
    @Override
    public String getTitle() {
        return title;
    }
}
