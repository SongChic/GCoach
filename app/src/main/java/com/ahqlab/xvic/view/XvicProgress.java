package com.ahqlab.xvic.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.util.XvicUtil;

public class XvicProgress extends RelativeLayout {

    public interface ClickListner {
        void OnClickListener(int position);
    }

    private final String TAG = XvicProgress.class.getSimpleName();
    private int viewWidth = 0;
    private ImageView selectStepView;
    private LinearLayout iconWrap;
    private ClickListner mListner;
    private ImageView[] iconViews;


    public XvicProgress(Context context) {
        this(context, null);
    }

    public XvicProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XvicProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init () {
    }
    public void setIcon (final int[] icons ) {
        iconViews = new ImageView[icons.length];
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_HORIZONTAL);
        iconWrap = new LinearLayout(getContext());
        iconWrap.setOrientation(LinearLayout.HORIZONTAL);
        for ( int i = 0; i < icons.length; i++ ) {
            final ImageView icon = new ImageView(getContext());
            icon.setImageResource(icons[i]);
            iconViews[i] = icon;
            if ( i == 0 ) {
                icon.setColorFilter(ContextCompat.getColor(getContext(), R.color.xvic_primary_color));
                selectStepView = icon;
            }
            iconWrap.post(new Runnable() {
                @Override
                public void run() {
                    int totalWidth = 0;
                    for ( int i = 0; i < iconWrap.getChildCount(); i++ )
                        if ( iconWrap.getChildAt(i) instanceof ImageView ) {
                            totalWidth = iconWrap.getChildAt(i).getWidth();
                            if ( totalWidth != 0 )
                                break;
                        }
                    totalWidth = totalWidth * icons.length;

                    viewWidth = getMeasuredWidth();

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((viewWidth - totalWidth) / (icons.length), XvicUtil.dpToPx(1));
                    params.setMargins(0, iconWrap.getChildAt(0).getHeight() / 3, 0, 0);
                    for ( int i = 0; i < iconWrap.getChildCount(); i++ ) {
                        if ( iconWrap.getChildAt(i) instanceof View && !(iconWrap.getChildAt(i) instanceof ImageView) ) {
                            iconWrap.getChildAt(i).setLayoutParams(params);
                            iconWrap.getChildAt(i).postInvalidate();
                        }
                    }


                }
            });
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    for ( int i = 0; i < iconViews.length; i++ ) {
                        if ( iconViews[i].equals(v) ) {
                            selectedIcon(i);
                            if ( mListner != null )
                                mListner.OnClickListener(i);
                            break;
                        }
                    }
                }
            });
            View line = new View(getContext());
            line.setBackgroundResource(R.color.xvic_step_default_color);
            iconWrap.addView(icon);
            if ( i != icons.length - 1 )
                iconWrap.addView(line);
        }
        iconWrap.setLayoutParams(params);
        this.addView(iconWrap);
    }
    public void selectedIcon ( int position ) {
        if ( selectStepView != null )
            selectStepView.setColorFilter(ContextCompat.getColor(getContext(), R.color.xvic_step_default_color));

        iconViews[position].setColorFilter(ContextCompat.getColor(getContext(), R.color.xvic_primary_color));
        selectStepView = iconViews[position];
    }
    public void setOnClickListner ( ClickListner listner ) {
        mListner = listner;
    }
}
