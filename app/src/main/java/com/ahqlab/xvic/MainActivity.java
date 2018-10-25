package com.ahqlab.xvic;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.ahqlab.xvic.adapter.MainPagerAdapter;
import com.ahqlab.xvic.base.BaseActivity;
import com.ahqlab.xvic.base.BaseFragment;
import com.ahqlab.xvic.constant.XvicConstant;
import com.ahqlab.xvic.databinding.ActivityMainBinding;
import com.ahqlab.xvic.fragment.pose.PoseCorrectFragment;
import com.ahqlab.xvic.fragment.setting.SettingFragment;
import com.ahqlab.xvic.fragment.swing.SwingSelectFragment;
import com.ahqlab.xvic.impl.BackPressedListener;
import com.ahqlab.xvic.view.FadeSlider;

public class MainActivity extends BaseActivity<MainActivity> {
    private ActivityMainBinding binding;
    private FadeSlider slider;
    private TabLayout tabLayout;
    private BackPressedListener mBackListener;
    private String[] titles = {
            "스윙 구간 선택",
            "자세 교정",
            "설정"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTitle("스윙 구간 선택");
        setFragments( new BaseFragment[]{
                new SwingSelectFragment(),
                new PoseCorrectFragment(),
                new SettingFragment()
        });

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if ( btAdapter == null ) {
            //블루투스 권한 사용 X
        } else {
            if ( !btAdapter.isEnabled() ) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, XvicConstant.BT_REQUEST_CODE);
            }
        }

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
        binding.mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                getFragments()[i].onFragmentSelected(MainActivity.this);
//                String frgmentTitle = MainActivity.this.mFragment.getTitle();
//                if ( MainActivity.this.mFragment instanceof SwingSelectFragment || MainActivity.this.mFragment instanceof PoseCorrectFragment || MainActivity.this.mFragment instanceof SettingFragment) {
//                    if ( frgmentTitle != null && !frgmentTitle.equals("") ) {
//                        setTitle(frgmentTitle);
//                    }
//                } else
//                setTitle(titles[i]);

            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        binding.mainViewPager.setCurrentItem(0);
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
    public void setTitleStr(String titleStr) {
        setTitle(titleStr);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == XvicConstant.BT_REQUEST_CODE ) {
            if ( resultCode == RESULT_OK ) {
                Log.e(TAG, "bluetooth on");
            } else {
                Log.e(TAG, "bluetooth off");
            }
        }
    }
    public void fragmentSetTitle ( String title ) {
        this.setTitle(title);
    }

}
