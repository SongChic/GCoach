package com.ahqlab.xvic.fragment.swing;

import android.content.Context;
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
import android.widget.RelativeLayout;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseActivity;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.constant.XvicConstant;
import com.ahqlab.xvic.databinding.FragmentSwingSelectBinding;
import com.ahqlab.xvic.domain.CircleProgress;
import com.ahqlab.xvic.view.FadeSlider;
import com.ahqlab.xvic.view.XvicProgress;

import java.util.ArrayList;

public class SwingSelectFragment extends BaseFragment<SwingSelectFragment> {
    private FragmentSwingSelectBinding binding;
    private RelativeLayout wrap;
    private int level = 0;
    String title = "스윙 구간 선택";
    private boolean chageState = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        ((BaseActivity) getActivity()).getFragments()[0] = this;
        ((BaseActivity) getActivity()).setFragment(this);
        if ( chageState ) {
            setTitle(title);

        }
        chageState = false;
        if ( wrap != null ) {
            return wrap;
        } else {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_swing_select, container, false);
            wrap = (RelativeLayout) binding.getRoot();
            binding.setFragment(this);
            binding.progress.setOnClickListner(new XvicProgress.ClickListner() {
                @Override
                public void OnClickListener(int position) {
                    binding.fadeSlider.fadeAnim(position);
                    level = position;
                }
            });
            binding.progress.setIcon(
                    new int[]{
                            R.drawable.step_01,
                            R.drawable.step_02,
                            R.drawable.step_03,
                            R.drawable.step_04,
                            R.drawable.step_05,
                            R.drawable.step_06
                    }
            );
            binding.fadeSlider.setItem(
                    new int[]{
                            R.drawable.step_01_pose,
                            R.drawable.step_02_pose,
                            R.drawable.step_03_pose,
                            R.drawable.step_04_pose,
                            R.drawable.step_05_pose,
                            R.drawable.step_06_pose
                    }

            );
            binding.fadeSlider.setOnClickListner(new FadeSlider.IndexCallback() {
                @Override
                public void IndexCallbackListener(int position) {
                    binding.fadeSlider.fadeAnim(position);
                    binding.progress.selectedIcon(position);
                }
            });
            return wrap;
        }
    }
    public static Fragment newInstance() {
        return new SwingSelectFragment();
    }
    public void onClick ( View v ) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if ( getActivity() == null )
                    return;
                BaseFragment fragment = ((BaseActivity) getActivity()).getFragments()[0];
                fragment.setTitle(fragment.getTitle());
            }
        });
        BaseFragment fragment = null;
        Bundle bundle = new Bundle();

        switch (v.getId()) {
            case R.id.setting_btn :
                fragment = new SwingSettingFragment();
                fragment.onFragmentSelected(((BaseActivity) getActivity()));
                bundle.putInt(XvicConstant.SWING_LEVEL_KEY, level);
                fragment.setArguments(bundle);
                break;
            case R.id.start_btn :
                fragment = new SwingPracticeFragment();
                fragment.onFragmentSelected(((BaseActivity) getActivity()));
                ArrayList<CircleProgress> steps = new ArrayList<>();
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
                bundle = new Bundle();
                bundle.putSerializable("test", steps);
                fragment.setArguments(bundle);
                break;
        }
        ft.replace(R.id.swing_root, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onFragmentSelected(BaseActivity activity) {
        activity.setTitle(title);
        chageState = true;
    }
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
