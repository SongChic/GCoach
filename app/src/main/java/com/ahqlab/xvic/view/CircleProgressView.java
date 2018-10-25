package com.ahqlab.xvic.view;

import android.annotation.SuppressLint;
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
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.base.BaseView;
import com.ahqlab.xvic.domain.CircleProgress;
import com.ahqlab.xvic.util.XvicUtil;

import java.util.ArrayList;
import java.util.List;

public class CircleProgressView extends BaseView<CircleProgressView> implements Runnable {
    private int BASE_PADDING = 86;

    private int mDeviceWidth = 0, mViewRadius = 0, mPadding = 86, mViewPadding = 0;
    private int mStartColor, mEndColor;
    private int mImage, mImgStartColor, mImgEndColor = -1, mAlpha = 100, mImgAlpha = 100, mImgWidth, mMode = 0, mStepCount = 0, progressState = 1;
    private int[] mButtonImage;
    private float outInteractBase = 0, inInteractBase = 0, mMainCenterX, mMainCenterY;
    private int viewWidth, viewHeight;

    /* progress circle */
    float startAngle = 270;
    float mAngle = 0;
    float mAnimAngle = 0;

    private List<CircleProgress> mSteps;

    private boolean mBtnState = false, interactState = false, mSelected = false;
    private CircleProgress.ImageOnClick mCallback;
    private CircleProgress.AngleCallback mAngleCallback;
    private Bitmap result;
    private Thread interaction;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if ( result != null ) {
            float x = mMainCenterX - result.getWidth() / 2;
            float y = mMainCenterY  - result.getHeight() / 2;
            float eventX = event.getX();
            float eventY = event.getY();

            if ( (eventX >= x && eventX <= x + result.getWidth()) && (eventY >= y && eventY <= y + result.getHeight()) ) {
                if ( mCallback != null ) {
                    mBtnState = mBtnState ? false : true;
//                    if ( mBtnState ) {
//                        if ( mButtonImage != null ) setImage(mButtonImage[1]);
//                        start();
//                    } else {
//                        if ( mButtonImage != null ) setImage(mButtonImage[0]);
//                        stop();
//                    }
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
    }
    public void changeColor ( int startColor, int endColor ) {
        mStartColor = startColor;
        mEndColor = endColor;
        invalidate();
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
        if ( progressState < 0 ) progressState = 1;
    }
    public void stop () {
        interactState = false;
        if ( interaction != null )
            interaction.interrupt();
        interaction = null;
        outInteractBase = inInteractBase = (float) mViewRadius;
        outInteractBase = outInteractBase / 2;
        inInteractBase = (inInteractBase - XvicUtil.dpToPx(30)) / 2;
        invalidate();
    }
    public void reset () {
        mAnimAngle = 0;
        progressState = -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if ( mSteps != null && mSteps.size() > 0 ) {
            makeCircle(canvas);
            makeImg(canvas);
        }

    }
    private void makeCircle( Canvas canvas ) {
        if ( mMode == CircleProgress.NORMAL_TYPE || mMode == CircleProgress.PROGRESS_TYPE ) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth( XvicUtil.dpToPx(18) );
            paint.setAlpha(255 * mSteps.get(mStepCount).getAlpha() / 100);

            paint.setShader(new LinearGradient(mViewRadius, mViewRadius - (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mStartColor, mEndColor, Shader.TileMode.CLAMP));
            canvas.drawCircle(mMainCenterX, mMainCenterY, mViewRadius / 2, paint);
            if ( mMode == CircleProgress.PROGRESS_TYPE ) {
                paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
//                    paint.setStrokeWidth( XvicUtil.dpToPx(18) );
                paint.setShadowLayer(10.0f, 0.0f, 2.0f, 0xFF000000);


                setLayerType(LAYER_TYPE_SOFTWARE, paint);

                float radius, x, y, circleRadius, centerX, centerY;

                radius = mViewRadius / 2 ;
                circleRadius = XvicUtil.dpToPx(27) / 2;

                centerX = centerY = radius + circleRadius + ( mViewPadding / 2 ) - XvicUtil.dpToPx(18) / 4;

                /* anim setting (s) */
                if ( mAnimAngle != mAngle ) {
//                    if ( mAnimAngle < mAngle ) {
//                        mAngle = mAnimAngle;
//                        stop();
//                    }
                    mAngle = mAngle + (2 * progressState);
                    if ( mAngleCallback != null )
                        mAngleCallback.onAngleListner((int) (mAngle / 360 * 100));
                }



                /* anim setting (e) */

                x = (float) (centerX + Math.cos( Math.toRadians( mAngle + startAngle ) ) * radius);
                y = (float) (centerY + Math.sin( Math.toRadians( mAngle + startAngle ) ) * radius);
                paint.setShader(new LinearGradient(x, y , x + circleRadius, y + circleRadius, Color.parseColor("#f7b585"), Color.parseColor("#f3887f"), Shader.TileMode.CLAMP));

                canvas.drawCircle(x, y, circleRadius, paint);
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

            canvas.drawCircle(mMainCenterX, mMainCenterY, outInteractBase, outCirclePaint);
            canvas.drawCircle(mMainCenterX, mMainCenterY, inInteractBase, inCirclePaint);

            canvas.drawCircle(mMainCenterX, mMainCenterY, (mViewRadius - XvicUtil.dpToPx(60)) / 2, circlePaint);
        } else if ( mMode == CircleProgress.TEXT_TYPE ) {
            Paint paint = new Paint();
            Paint textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(18 * getResources().getDisplayMetrics().scaledDensity);
            if ( mSelected ) {
                textPaint.setColor(Color.parseColor("#ffffff"));
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


                canvas.drawCircle(mMainCenterX, mMainCenterY, outInteractBase, outCirclePaint);
                canvas.drawCircle(mMainCenterX, mMainCenterY, inInteractBase, inCirclePaint);

                canvas.drawCircle(mMainCenterX, mMainCenterY, mViewRadius / 2, circlePaint);
            } else {
                textPaint.setColor(Color.parseColor("#666666"));
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mMainCenterX, mMainCenterY, mViewRadius / 2, paint);

                paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(XvicUtil.dpToPx(6));
                paint.setAlpha(255 * mSteps.get(mStepCount).getAlpha() / 100);

                if (mStartColor == -1)
                    mStartColor = Color.parseColor("#e99e73");
                if (mEndColor == -1)
                    mEndColor = Color.parseColor("#ec7579");
                paint.setShader(new LinearGradient(mViewRadius, mViewRadius - (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mViewRadius + (mViewRadius / 2), mStartColor, mEndColor, Shader.TileMode.CLAMP));
                canvas.drawCircle(mMainCenterX, mMainCenterY, mViewRadius / 2, paint);
            }
            String text = mSteps.get(mStepCount).getText();


            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);

            canvas.drawText(text, mMainCenterX, mMainCenterY + bounds.height() / 2, textPaint);
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


                if ( mImgStartColor != -1 && mImgEndColor != -1 ) {
                    Canvas imgCanvas = new Canvas(result);
                    Paint innerPaint = new Paint();
                    innerPaint.setShader(new LinearGradient(0, 0, 0, mPadding + (mViewRadius / 2) - (result.getHeight()), mImgStartColor, mImgEndColor, Shader.TileMode.CLAMP));
                    innerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    innerPaint.setAlpha(255 * mImgAlpha / 100);
                    imgCanvas.drawRect(0, 0, result.getWidth(), result.getHeight(), innerPaint);
                    imgPaint.setShader(new LinearGradient(0, 0, 0, mPadding + (mViewRadius / 2) - (result.getHeight()), mImgStartColor, mImgEndColor, Shader.TileMode.CLAMP));
                } else {
                    imgPaint.setAlpha(255 * mImgAlpha / 100);
                }
                canvas.drawBitmap(result, mMainCenterX - result.getWidth() / 2, mMainCenterY  - result.getHeight() / 2, imgPaint);
            }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(viewWidth, viewHeight);
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
    public void setStep (List<CircleProgress> steps ) {
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
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = mViewRadius + XvicUtil.dpToPx(18) + mViewPadding;
        params.height = mViewRadius + XvicUtil.dpToPx(18) + mViewPadding;
        this.setLayoutParams(params);
        invalidate();
    }
    private void setValues () {
        /* color settings (s) */
        if ( mStartColor != -1 ) mStartColor = -1;
        if ( mSteps.get(mStepCount).getStartColor() != -1 ) mStartColor = mSteps.get(mStepCount).getStartColor();
        if ( mSteps.get(mStepCount).getEndColor() != -1 ) mEndColor = mSteps.get(mStepCount).getEndColor();
        if ( mPadding != XvicUtil.dpToPx(BASE_PADDING) )
            mPadding = XvicUtil.dpToPx(BASE_PADDING);
        if ( mSteps.get(mStepCount).getPadding() != -1 ) {
            mPadding = mSteps.get(mStepCount).getPadding();
        }
        mViewRadius = mImgWidth = mDeviceWidth - (mPadding * 2);
        mImgAlpha = mSteps.get(mStepCount).getImgAlpha();
        mViewPadding = XvicUtil.dpToPx(27) / 2;

        mImgStartColor = mSteps.get(mStepCount).getStartImgColor();
        mImgEndColor = mSteps.get(mStepCount).getEndImgColor();

        /* color settings (e) */
        mMode = mSteps.get(mStepCount).getType();

        if ( mSteps.get(mStepCount).getImageResorce() != 0 )
            mImage = mSteps.get(mStepCount).getImageResorce();
        if ( mSteps.get(mStepCount).getBtnImageResource() != null && mSteps.get(mStepCount).getBtnImageResource().length != 0 ) {
            mImage = mSteps.get(mStepCount).getBtnImageResource()[0];
            mButtonImage = mSteps.get(mStepCount).getBtnImageResource();
        }
        mCallback = mSteps.get(mStepCount).getOnClick() != null ? mSteps.get(mStepCount).getOnClick() : null;
        mAngleCallback = mSteps.get(mStepCount).getAngleListner() != null ? mSteps.get(mStepCount).getAngleListner() : null;
//        mImage = mImage == 0 ? R.drawable.address : mImage;
        Log.e(TAG, String.format("view width : %d", this.getWidth()));
        viewWidth = viewHeight = mViewRadius + XvicUtil.dpToPx(18) + mViewPadding;
        if ( mMode == CircleProgress.NORMAL_TYPE || mMode == CircleProgress.PROGRESS_TYPE ) {
            mMainCenterX = mMainCenterY = mViewRadius / 2 + XvicUtil.dpToPx(18) / 2 + mViewPadding / 2;
        }
        else if ( mMode == CircleProgress.ANIMATION_TYPE ) {
            mPadding = 66;
            mPadding = XvicUtil.dpToPx(mPadding);
            mViewRadius = mImgWidth = mDeviceWidth - (mPadding * 2);
            outInteractBase = inInteractBase = (float) mViewRadius;
            outInteractBase = outInteractBase / 2;
            inInteractBase = (inInteractBase - XvicUtil.dpToPx(30)) / 2;
            mMainCenterX = mMainCenterY = outInteractBase + XvicUtil.dpToPx(18) / 2 + mViewPadding / 2;
            viewWidth = viewHeight = mViewRadius + XvicUtil.dpToPx(18) + mViewPadding;
        } else if ( mMode == CircleProgress.TEXT_TYPE ) {
            mMainCenterX = mMainCenterY = mViewRadius / 2 + XvicUtil.dpToPx(18) / 2 + mViewPadding / 2;
            mSelected = mSteps.get(mStepCount).isSelected();
            if ( mSelected ) {
                mMainCenterX = mMainCenterY = mViewRadius / 2 + XvicUtil.dpToPx(48) / 2 + mViewPadding / 2;
                viewWidth = viewHeight = mViewRadius + XvicUtil.dpToPx(48) + mViewPadding;
            }
            outInteractBase = inInteractBase = (float) mViewRadius / 2;
            outInteractBase = outInteractBase + XvicUtil.dpToPx(10);
            inInteractBase = inInteractBase + XvicUtil.dpToPx(20);
        }
        if ( mSteps.get(mStepCount).getSize() != 0 )
            mImgWidth = mImgWidth / mSteps.get(mStepCount).getSize();
    }
    /* new code (e) */
    public void setAngle( float angle ) {
        mAnimAngle = angle;
    }
    public void setAlpha( int alpha ) {
        mSteps.get(mStepCount).setAlpha(alpha);
        mImgAlpha = alpha;
        invalidate();
    }
    public void setStep(CircleProgress step ) {
        List<CircleProgress> steps = new ArrayList<>();
        steps.add(step);
        setStep(steps);
    }
    public void setSelected ( boolean selected ) {
        mSteps.get(mStepCount).setSelected(selected);
        setValues();
        invalidate();
    }
}
