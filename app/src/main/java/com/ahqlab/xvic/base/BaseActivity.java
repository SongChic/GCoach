package com.ahqlab.xvic.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahqlab.xvic.R;

public abstract class BaseActivity<D extends Activity> extends AppCompatActivity  {
    protected String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void noActionBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @SuppressLint("ResourceAsColor")
    public void setTitle(String titleStr ) {
        if ( toolbar == null ) {
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
        TextView title = findViewById(R.id.title);
        Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(R.font.raleway_semibold);
        }
//            title.setTextColor(R.color.xvic_title_color);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(typeface);
        title.setText(titleStr);

    }
}
