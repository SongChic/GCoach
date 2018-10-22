package com.ahqlab.xvic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseView;
import com.ahqlab.xvic.domain.CircleProgress;
import com.ahqlab.xvic.util.XvicUtil;

import java.util.List;

public class CircleProgressView extends BaseView<CircleProgressView> implements Runnable {

    private int mDeviceWidth = 0, mViewRadius = 0, mPadding = 86;
    private int mStartColor, mEndColor;
    private int mImage, mImgStartColor, mImgEndColor = -1, mAlpha = 100, mImgWidth, mMode = 0, mStepCount = 0, w, h;
    private int[] mButtonImage;
    private float outInteractBase = 0, inInteractBase = 0;

    private List<CircleProgress> mSteps;

    private boolean mBtnState = false, interactState = false;
    private CircleProgress.ImageOnClick mCallback;
    private Bitmap result;
    private Thread interaction;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if ( result != null ) {
            float x = mPadding + (mViewRadius / 2) - (result.getWidth() / 3) + result.getWidth();
            float y = mPadding + (mViewRadius / 2) - (result.getHeight() / 2) + result.getHeight();
            float eventX = event.getX();
            float eventY = event.getY();
            if (
                    (eventX >= mPadding + (mViewRadius / 2) - (result.getWidth() / 3) && eventX <= x) && (eventY >= mPadding + (mViewRadius / 2) - (result.getHeight() / 2) && eventY <= y)
                ) {
                if ( mCallback != null ) {
                    mBtnState = mBtnState ? false : true;
                    if ( mBtnState ) {
                        if ( mButtonImage != null ) setImage(mButtonImage[1]);
                        start();
                    } else {
                        if ( mButtonImage != null ) setImage(mButtonImage[0]);
                        stop();
                    }
                    invalidate();
                    mCallback.onClick(mBtnState);
                }


            }
        }
        return super.onTouchEvent(event);
    }

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        setTypedArray(array);
        init();
    }
    private void init () {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        mDeviceWidth = metrics.widthPixels;
        mPadding = XvicUtil.dpToPx(mPadding);
        mViewRadius = mImgWidth = mDeviceWidth - (mPadding * 2);
//        this.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//        int viewW = this.getMeasuredWidth(), viewHeight = this.getMeasuredHeight();

        if ( this.getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            this.setLayoutParams(params);
        }
    }
    public void setImage( int imageResource ) {
        mImage = imageResource;
    }
    public void setTypedArray(TypedArray array) {
        mStartColor = Color.parseColor(array.getString(R.styleable.CircleProgressView_startColor));
        mEndColor = Color.parseColor(array.getString(R.styleable.CircleProgressView_endColor));
        array.recycle();
    }
    public void start () {
        interaction = new Thread(this);
        interactState = true;
        interaction.start();
    }
    public void stop () {
        interactState = false;
        interaction.interrupt();
        interaction = null;
        outInteractBase = inInteractBase = (float) mViewRadius;
        outInteractBase = outInteractBase / 2;
        inInteractBase = (inInteractBase - XvicUtil.dpToPx(30)) / 2;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if ( mSteps != null && mSteps.size() > 0 ) {
            makeCircle(canvas);
            makeImg(canvas);
//            if ( mSteps.get(mStepCount).getType() == CircleProgress.PROGRESS_TYPE ) {
//
//            } else if ( mSteps.get(mStepCount).getType() == CircleProgress.ANIMATION_TYPE ) {
//
//            }
        }

    }
    private void makeCircle( Canvas canvas ) {
        if ( mMode == CircleProgress.NORMAL_TYPE || mMode == CircleProgress.PROGRESS_TYPE ) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth( XvicUtil.dpToPx(18) );

            paint.setShader(new LinearGradient(mViewRadius, mViewRadius - (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mStartColor, mEndColor, Shader.TileMode.CLAMP));
            canvas.drawCircle(mViewRadius / 2 + XvicUtil.dpToPx(18) / 2, mViewRadius / 2 + XvicUtil.dpToPx(18) / 2, mViewRadius / 2, paint);
            if ( mMode == CircleProgress.PROGRESS_TYPE ) {
                paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth( XvicUtil.dpToPx(18) );
                paint.setShadowLayer(10.0f, 0.0f, 2.0f, 0xFF000000);
                paint.setShader(new LinearGradient(mViewRadius, mViewRadius - (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mStartColor, mEndColor, Shader.TileMode.CLAMP));
                setLayerType(LAYER_TYPE_SOFTWARE, paint);

                float x = (float) (mViewRadius * Math.sin( Math.toRadians(0) ));
                float y = (float) (mViewRadius * Math.sin( Math.toRadians(0) ));

                canvas.drawCircle(x, y, XvicUtil.dpToPx(18) / 2, paint);
            }
        } else if ( mMode == CircleProgress.ANIMATION_TYPE ) {
            Paint outCirclePaint = new Paint();
            outCirclePaint.setStyle(Paint.Style.FILL);
            outCirclePaint.setShader(new LinearGradient(mViewRadius, mViewRadius - (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mViewRadius + (mViewRadius / 2), Color.parseColor("#ecad8d"), Color.parseColor("#ef8f91"), Shader.TileMode.CLAMP));
            outCirclePaint.setAlpha(255 * 22 / 100);

            Paint inCirclePaint = new Paint();
            inCirclePaint.setStyle(Paint.Style.FILL);
            inCirclePaint.setShader(new LinearGradient(mViewRadius, mViewRadius - (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mViewRadius + (mViewRadius / 2), Color.parseColor("#ecad8d"), Color.parseColor("#ef8f91"), Shader.TileMode.CLAMP));
            inCirclePaint.setAlpha(255 * 40 / 100);

            Paint circlePaint = new Paint();
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setShader(new LinearGradient(mViewRadius, mViewRadius - (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mViewRadius + (mViewRadius / 2), Color.parseColor("#ecad8d"), Color.parseColor("#ef8f91"), Shader.TileMode.CLAMP));
            circlePaint.setAlpha(255 * 100 / 100);

            if ( interaction != null ) {
                if ( outInteractBase >= mViewRadius / 2 ) {
                    outInteractBase = (mViewRadius - XvicUtil.dpToPx(60)) / 2;
                    inInteractBase = (mViewRadius - XvicUtil.dpToPx(60)) / 2;
                } else {
                    outInteractBase += 7;
                    if ( !(inInteractBase >= (mViewRadius - XvicUtil.dpToPx(30)) / 2) )
                        inInteractBase += 7;
                }
            }

            canvas.drawCircle(mViewRadius / 2, mViewRadius / 2, outInteractBase, outCirclePaint);
            canvas.drawCircle(mViewRadius / 2, mViewRadius / 2, inInteractBase, inCirclePaint);

            canvas.drawCircle(mViewRadius / 2, mViewRadius / 2, (mViewRadius - XvicUtil.dpToPx(60)) / 2, circlePaint);
        }

    }
    private void makeImg ( Canvas canvas ) {
            if ( mImage != -1 ) {
                Bitmap img = BitmapFactory.decodeResource(getResources(), mImage);
                if ( img == null )
                    return;

                double aspectRatio = (double) img.getHeight() / (double) img.getWidth();
                int targetHeight = (int) (mImgWidth / 2 * aspectRatio);
                result = Bitmap.createScaledBitmap(img, mImgWidth / 2, targetHeight, false);
                if (result != img) {
                    img.recycle();
                }
                Paint imgPaint = new Paint();
                if ( mAlpha != 100 )
                    imgPaint.setAlpha(255 * mAlpha / 100);
                if ( mImgStartColor != -1 && mImgEndColor != -1 ) {
                    Canvas imgCanvas = new Canvas(result);
                    Paint innerPaint = new Paint();
                    innerPaint.setShader(new LinearGradient(0, 0, 0, mPadding + (mViewRadius / 2) - (result.getHeight()), mImgStartColor, mImgEndColor, Shader.TileMode.CLAMP));
                    innerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    imgCanvas.drawRect(0, 0, result.getWidth(), result.getHeight(), innerPaint);
                    imgPaint.setShader(new LinearGradient(0, 0, 0, mPadding + (mViewRadius / 2) - (result.getHeight()), mImgStartColor, mImgEndColor, Shader.TileMode.CLAMP));
                }
                float imgX = mMode == CircleProgress.PROGRESS_TYPE || mMode == CircleProgress.NORMAL_TYPE ? (mViewRadius / 2 + XvicUtil.dpToPx(18) / 2) : mViewRadius / 2, imgY = mMode == CircleProgress.PROGRESS_TYPE || mMode == CircleProgress.NORMAL_TYPE ? (mViewRadius / 2 + XvicUtil.dpToPx(18) / 2) : mViewRadius / 2;

                canvas.drawBitmap(result, imgX - result.getWidth() / 2, imgY  - result.getHeight() / 2, imgPaint);
            }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mViewRadius + XvicUtil.dpToPx(18), mViewRadius + XvicUtil.dpToPx(18));
    }
    @Override
    public void run() {
        while (interactState) {
            postInvalidate();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* new code (s) */
    public void setStep ( List<CircleProgress> steps ) {
        mSteps = steps;
        setValues();
        invalidate();
    }
    public void changeStep() {
        if (mStepCount < mSteps.size())
            mStepCount++;
        if ( mMode != mSteps.get(mStepCount).getType() )
            mMode = mSteps.get(mStepCount).getType();
        if ( interaction != null )
            stop();
        setValues();
        invalidate();
    }
    private void setValues () {
        /* color settings (s) */
        if ( mSteps.get(mStepCount).getStartColor() != -1 ) mStartColor = mSteps.get(mStepCount).getStartColor();
        if ( mSteps.get(mStepCount).getEndColor() != -1 ) mEndColor = mSteps.get(mStepCount).getEndColor();

        if ( mSteps.get(mStepCount).getStartImgColor() != 0 ) mImgStartColor = mSteps.get(mStepCount).getStartImgColor();
        if ( mSteps.get(mStepCount).getEndImgColor() != 0 ) mImgEndColor = mSteps.get(mStepCount).getEndImgColor();
        /* color settings (e) */
        mMode = mSteps.get(mStepCount).getType();

        if ( mSteps.get(mStepCount).getImageResorce() != 0 )
            mImage = mSteps.get(mStepCount).getImageResorce();
        if ( mSteps.get(mStepCount).getBtnImageResource() != null && mSteps.get(mStepCount).getBtnImageResource().length != 0 ) {
            mImage = mSteps.get(mStepCount).getBtnImageResource()[0];
            mButtonImage = mSteps.get(mStepCount).getBtnImageResource();
        }
        mCallback = mSteps.get(mStepCount).getOnClick() != null ? mSteps.get(mStepCount).getOnClick() : null;
//        mImage = mImage == 0 ? R.drawable.address : mImage;
        if ( mMode == CircleProgress.ANIMATION_TYPE ) {
            mPadding = 66;
            mPadding = XvicUtil.dpToPx(mPadding);
            mViewRadius = mImgWidth = mDeviceWidth - (mPadding * 2);
            outInteractBase = inInteractBase = (float) mViewRadius;
            outInteractBase = outInteractBase / 2;
            inInteractBase = (inInteractBase - XvicUtil.dpToPx(30)) / 2;
        }
        if ( mSteps.get(mStepCount).getSize() != 0 )
            mImgWidth = mImgWidth / mSteps.get(mStepCount).getSize();
    }
    /* new code (e) */

}
