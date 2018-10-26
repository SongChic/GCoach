package com.ahqlab.xvic.fragment.pose;

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
import android.widget.Toast;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseActivity;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.databinding.FragmentPoseCorrectBinding;
import com.ahqlab.xvic.view.CustomCheckBox;

public class PoseCorrectFragment extends BaseFragment<PoseCorrectFragment> {
    private FragmentPoseCorrectBinding binding;
    int position = -1;
    String title = "자세교정";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pose_correct, container, false);
        ((BaseActivity) getActivity()).getFragments()[1] = this;
        binding.setFragment(this);
        for ( int i = 0; i < binding.checkboxWrap.getChildCount(); i++ ) {
            final int finalI = i;
            binding.checkboxWrap.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initCheckState();
                    position = finalI;
                    ((CustomCheckBox)v).onClick(v);
                }
            });
            ((CustomCheckBox) binding.checkboxWrap.getChildAt(i)).setOnChecked(new CustomCheckBox.OnCheckCallback() {
                @Override
                public void OnCheck(View v, boolean checked) {
                    initCheckState();
                    position = finalI;
                    ((CustomCheckBox)v).setCheckedForCheck(checked);
                }
            });
        }
        position = -1;
        return binding.getRoot();
    }
    private void initCheckState () {
        for ( int i = 0; i < binding.checkboxWrap.getChildCount(); i++ )
            ((CustomCheckBox) binding.checkboxWrap.getChildAt(i)).setChecked(false);
    }
    public void onClick( View v ) {
        FragmentManager fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if ( getActivity() == null )
                    return;
                BaseFragment fragment = ((BaseActivity) getActivity()).getFragments()[1];
                fragment.setTitle(fragment.getTitle());
            }
        });
        FragmentTransaction ft = fm.beginTransaction();
        BaseFragment fragment = null;

        switch (v.getId()) {
            case R.id.setting_btn :
                if ( position == -1 ) {
                    Toast.makeText(getContext(), "교정할 자세를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                fragment = new PoseSettingFragment();
                break;
            case R.id.start_btn :
                if ( position == -1 ) {
                    Toast.makeText(getContext(), "교정할 자세를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                fragment = new PosePracticeFragment();
                break;
        }
        fragment.onFragmentSelected(((BaseActivity) getActivity()));
        ft.replace(R.id.pose_fragment_wrap, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onFragmentSelected(BaseActivity activity) {
        activity.setTitle(title);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if ( isVisibleToUser )
            ((BaseActivity) getActivity()).setTitle(title);
        super.setUserVisibleHint(isVisibleToUser);
    }
}
