package com.ahqlab.xvic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.Log;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.util.XvicUtil;

public class CustomSeekbar extends AppCompatSeekBar {
    private final String TAG = CustomSeekbar.class.getSimpleName();
    public CustomSeekbar(Context context) {
        this(context, null);
    }

    public CustomSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.seekBarStyle);
    }

    public CustomSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init () {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seek_bar_thumb);
        Bitmap thumb = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        this.getProgressDrawable().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            this.getThumb().setColorFilter(Color.parseColor("#b6faa1"), PorterDuff.Mode.MULTIPLY);
//        }
        Drawable thumbDrawable = new BitmapDrawable(getResources(), bitmap);
        setThumb(thumbDrawable);
        this.setMinimumHeight(XvicUtil.dpToPx(12));

    }

}
