package com.ahqlab.xvic.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.ahqlab.xvic.MainActivity;
import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseActivity;

public class Splash extends BaseActivity<Splash> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.noActionBar();
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2000);
            return;
        } else {
            /* 5초 뒤에 메인으로 이동
             * 실제 코드 작성 시 데이터를 받아와서 이동 */
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    move();
                }
            }, 5000);
        }

    }
    private void move() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];

            if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    /* 5초 뒤에 메인으로 이동
                     * 실제 코드 작성 시 데이터를 받아와서 이동 */
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            move();
                        }
                    }, 5000);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2000);
                }
            }
        }
    }


}
