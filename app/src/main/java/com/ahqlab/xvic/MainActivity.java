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
import android.view.MenuItem;

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
    private BackPressedListener mBackListener;


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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

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
                getFragments()[tab.getPosition()].onFragmentSelected(MainActivity.this);
                getFragments()[tab.getPosition()].setBackButton();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.mainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
//                finish();
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
