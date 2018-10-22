package com.ahqlab.xvic.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahqlab.xvic.R;

public class CustomCheckBox extends RelativeLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, Checkable {
    private static final int[] STATE_CHECKABLE = {android.R.attr.state_checked};

    private String mText;
    private int textSize;
    private Drawable bg;
    private boolean isChecked = false;

    private TextView btnText;
    private CheckBox checkBox;
    private OnClickCallback mClickCallback;
    private OnCheckCallback mCheckCallback;

    public interface OnClickCallback {
        void OnClick( View v, boolean checked );
    }
    public interface OnCheckCallback {
        void OnCheck( View v, boolean checked );
    }

    public CustomCheckBox(Context context) {
        this(context, null);
    }

    public CustomCheckBox(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttrs(attrs);
        init();
    }
    private void init () {
        btnText = new TextView(getContext());
        btnText.setText(mText);
        btnText.setTextColor(ContextCompat.getColor(getContext(), R.color.xvic_default_text_color));
        if ( textSize != -1 )
            btnText.setTextSize(textSize);
        Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(R.font.arial_bold_mt);
            btnText.setTypeface(typeface);
            btnText.setTextScaleX(0.9f);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(bg);
        }
        checkBox = new CheckBox(getContext());
        checkBox.setOnCheckedChangeListener(this);
        setButtonTint(checkBox, R.color.xvic_default_checkbox_color);
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        btnText.setLayoutParams(params);

        params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        checkBox.setLayoutParams(params);

        this.addView(checkBox);
        this.addView(btnText);

        this.setOnClickListener(this);
    }
    private void setAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, new int[]{android.R.attr.background, android.R.attr.text, android.R.attr.textSize});
        bg = getBackground();
        mText = array.getText(1).toString();
        textSize = array.getInt(2, -1);
    }
    @Override
    protected int[] onCreateDrawableState(int extraSpace)
    {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked) mergeDrawableStates(drawableState, STATE_CHECKABLE);
        return drawableState;
    }

    @Override
    public void onClick(View v) {
        setChecked(!isChecked);
        checkBox.setChecked(isChecked);
    }

    @SuppressLint("ResourceType")
    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        if ( mClickCallback != null )
            mClickCallback.OnClick(this, isChecked);
        if (isChecked) {
            btnText.setTextColor(ContextCompat.getColor(getContext(), R.color.xvic_white_color));
            checkBox.setChecked(isChecked);
            setButtonTint(checkBox, R.color.xvic_white_color);
        } else {
            btnText.setTextColor(ContextCompat.getColor(getContext(), R.color.xvic_default_text_color));
            checkBox.setChecked(isChecked);
            setButtonTint(checkBox, R.color.xvic_default_checkbox_color);
        }
        refreshDrawableState();
    }
    @SuppressLint("ResourceType")
    public void setCheckedForCheck(boolean checked) {
        isChecked = checked;
        if (isChecked) {
            btnText.setTextColor(ContextCompat.getColor(getContext(), R.color.xvic_white_color));
            setButtonTint(checkBox, R.color.xvic_white_color);
            checkBox.setChecked(isChecked);
        } else {
            btnText.setTextColor(ContextCompat.getColor(getContext(), R.color.xvic_default_text_color));
            setButtonTint(checkBox, R.color.xvic_default_checkbox_color);
            checkBox.setChecked(isChecked);
        }
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        setCheckedForCheck(isChecked);
        if ( mCheckCallback != null ) mCheckCallback.OnCheck(this, isChecked);

    }
    public static void setButtonTint(CheckBox view, int resId) {
        int tintColor = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            tintColor = view.getContext().getColor(resId);
        }
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{new int[]{}},
                new int[]{
                        tintColor
                }
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setButtonTintList(colorStateList);
        }
    }
    public boolean getIsChecked() {
        return isChecked;
    }
    public void setOnClickCallback ( OnClickCallback callback ) {
        mClickCallback = callback;
    }
    public void setOnChecked( OnCheckCallback callback ) {
        mCheckCallback = callback;
    }

}
