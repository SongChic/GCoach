package com.ahqlab.xvic.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.util.XvicUtil;

@SuppressLint("AppCompatCustomView")
public class CircleTextView extends TextView {
    private int mViewWidth = XvicUtil.dpToPx(20), mViewHeight = XvicUtil.dpToPx(55);
    private String circleColor;
    public CircleTextView(Context context) {
        this(context, null);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs) {
        //com.android.internal.R.attr.textViewStyle
        this(context, attrs, 0);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttr(attrs, defStyleAttr);
        init();
    }
    private void init () {
        this.setPadding(0, 0, 0, XvicUtil.dpToPx(30));
    }
    private void setAttr( AttributeSet attrs, int defStyleAttr ) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.CircleTextView, defStyleAttr, 0);
        circleColor = arr.getString(R.styleable.CircleTextView_circleColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        if ( circleColor == null || circleColor.equals("") )
            circleColor = "#02faa4";
        paint.setColor(Color.parseColor(circleColor));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mViewWidth / 2, XvicUtil.dpToPx(20) + mViewWidth, mViewWidth / 2, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mViewWidth, mViewHeight);
    }
}
