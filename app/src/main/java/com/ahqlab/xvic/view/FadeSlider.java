package com.ahqlab.xvic.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ahqlab.xvic.R;

public class FadeSlider extends RelativeLayout implements View.OnClickListener {
    public interface IndexCallback {
        void IndexCallbackListener(int position);
    }
    private ImageView leftArrow, rightArrow;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout itemWrap;
    private int mCount = 0, level = 0;
    private IndexCallback mListner;

    public FadeSlider(Context context) {
        this(context, null);
    }

    public FadeSlider(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FadeSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init () {
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        leftArrow = new ImageView(getContext());
        leftArrow.setImageResource(R.drawable.left_arrow);
        leftArrow.setPadding(50, 50, 50, 50);
        leftArrow.setLayoutParams(params);
        leftArrow.setOnClickListener(this);

        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        rightArrow = new ImageView(getContext());
        rightArrow.setImageResource(R.drawable.right_arrow);
        rightArrow.setPadding(50, 50, 50, 50);
        rightArrow.setLayoutParams(params);
        rightArrow.setOnClickListener(this);

        this.addView(leftArrow);
        this.addView(rightArrow);
    }
    public void setItem ( int[] items ) {
        mCount = items.length;
        itemWrap = new RelativeLayout(getContext());
        params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        for ( int i = 0; i < items.length; i++ ) {
            ImageView itemView = new ImageView(getContext());
            if ( i != 0 )
                itemView.animate().alpha(0).setDuration(0).withLayer();
            itemView.setImageResource(items[i]);
            itemWrap.addView(itemView);
        }
        itemWrap.setLayoutParams(params);
        this.addView(itemWrap);
    }
    public void fadeAnim ( int position ) {
        if ( position < 0 || position == mCount )
            return;
        for ( int i = 0; i < itemWrap.getChildCount(); i++ ) {
            if ( itemWrap.getChildAt(i).getAlpha() > 0 ) {
                itemWrap.getChildAt(i).animate().setDuration(500).alpha(0).withLayer();
            }
        }
        itemWrap.getChildAt(position).animate().alpha(1).setDuration(1000).withLayer();
        level = position;
    }

    @Override
    public void onClick(View v) {
        if ( v.equals(leftArrow) ) {
            if ( level == 0 )
                return;
            level--;
        } else if ( v.equals(rightArrow) ) {
            if ( level == mCount - 1 )
                return;
            level++;
        }
        if ( mListner != null )
            mListner.IndexCallbackListener(level);
    }
    public void setOnClickListner ( IndexCallback listner ) {
        mListner = listner;
    }
}
