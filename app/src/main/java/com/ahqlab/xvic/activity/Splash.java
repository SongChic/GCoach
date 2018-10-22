package com.ahqlab.xvic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.ahqlab.xvic.MainActivity;
import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseActivity;

public class Splash extends BaseActivity<Splash> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.noActionBar();
        setContentView(R.layout.activity_splash);

        /* 5초 뒤에 메인으로 이동
         * 실제 코드 작성 시 데이터를 받아와서 이동 */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                move();
            }
        }, 1000);
    }
    private void move() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
