package com.ahqlab.xvic;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.ahqlab.xvic.adapter.MainPagerAdapter;
import com.ahqlab.xvic.base.BaseActivity;
import com.ahqlab.xvic.databinding.ActivityMainBinding;
import com.ahqlab.xvic.impl.BackPressedListener;
import com.ahqlab.xvic.view.FadeSlider;

public class MainActivity extends BaseActivity<MainActivity> {
    private ActivityMainBinding binding;
    private FadeSlider slider;

    private TabLayout tabLayout;

    private BackPressedListener mBackListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTitle("스윙 구간 선택");


        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("스윙교정"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("자세교정"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("설정"));
        binding.tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.mainViewPager.setAdapter(adapter);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.mainViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setOnBackPressedListener ( BackPressedListener listener ) {
        mBackListener = listener;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if ( mBackListener != null ) {
//            mBackListener.onBackKey();
//        }
    }
}
