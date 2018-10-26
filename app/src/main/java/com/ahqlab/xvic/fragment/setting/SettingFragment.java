package com.ahqlab.xvic.fragment.setting;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.ahqlab.xvic.databinding.FragmentSettingBinding;
import com.ahqlab.xvic.domain.CircleProgress;
import com.ahqlab.xvic.fragment.pose.PoseCorrectFragment;
import com.ahqlab.xvic.util.XvicUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends BaseFragment<SettingFragment> implements View.OnClickListener {
    private FragmentSettingBinding binding;
    private long mLastClickTime;
    String title = "설정";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        ((BaseActivity) getActivity()).getFragments()[2] = this;
        List<CircleProgress> steps = new ArrayList<>();
        CircleProgress step = CircleProgress.builder().imageResorce(R.drawable.bluetooth_icon).startColor(Color.parseColor("#bdc0f8")).endColor(Color.parseColor("#64d9fc")).type(CircleProgress.NORMAL_TYPE).size(CircleProgress.SMALL_SIZE).padding(XvicUtil.dpToPx(100)).build();
        steps.add(step);
        binding.bluetoothBtn.setStep(steps);
        binding.bluetoothBtn.setOnClickListener(this);

        steps = new ArrayList<>();
        step = CircleProgress.builder().imageResorce(R.drawable.setting_icon).startColor(Color.parseColor("#c7faa0")).endColor(Color.parseColor("#21faa3")).type(CircleProgress.NORMAL_TYPE).size(CircleProgress.MIDDLE_SIZE).padding(XvicUtil.dpToPx(100)).build();
        steps.add(step);
        //level_setting_btn
        binding.levelSettingBtn.setStep(steps);
        binding.levelSettingBtn.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if ( getActivity() == null )
                    return;
                BaseFragment fragment = ((BaseActivity) getActivity()).getFragments()[2];
                fragment.setTitle(fragment.getTitle());
            }
        });
        BaseFragment fragment = null;
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){ return; }
        switch (v.getId()) {
            case R.id.bluetooth_btn :
                fragment = new BluetoothSettingFragment();
                break;
            case R.id.level_setting_btn :
                fragment = new LevelSettingFragment();
                break;
        }
        fragment.onFragmentSelected(((BaseActivity) getActivity()));
        transaction.replace(R.id.setting_fragment_wrap, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
        mLastClickTime = SystemClock.elapsedRealtime();

    }
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void onFragmentSelected() {
        super.onFragmentSelected();

    }

    @Override
    public void onFragmentSelected(BaseActivity activity) {
        setTitleStr(title);
        super.onFragmentSelected(activity);
//        activity.setTitle(title);
    }
}
