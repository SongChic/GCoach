package com.ahqlab.xvic.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.TextView;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.util.XvicUtil;

public abstract class BaseActivity<D extends Activity> extends AppCompatActivity  {
    protected String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    public BaseFragment[] mFragments;
    public BaseFragment mFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void noActionBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public void setTitle(String titleStr ) {
        if ( toolbar == null ) {
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("");

            Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.left_arrow);
            double aspectRatio = (double) img.getHeight() / (double) img.getWidth();

            int targetHeight = (int) (XvicUtil.dpToPx(100) / 2 * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(img, XvicUtil.dpToPx(100) / 2, targetHeight, false);


            Drawable drawable = new BitmapDrawable(result);
            drawable.mutate();
            DrawableCompat.setTint(drawable, Color.parseColor("#80aaaaaa"));

            toolbar.setNavigationIcon(drawable);
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
    public void setFragment( BaseFragment fragment ) {
        mFragment = fragment;
    }
    public void setFragments( BaseFragment[] fragments ) {
        mFragments = fragments;
    }
    public BaseFragment[] getFragments(){
        return mFragments;
    }

}
