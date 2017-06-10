package com.vitoksmile.library.view.controlview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

/*
Author: Victor Mykhailiv (vitoksmile)

https://www.facebook.com/mykhailiv.victor
https://t.me/vitoksmile
https://github.com/vitoksmile

30.05.2017
v.1.0
 */
public class ControlView extends View {

    public interface OnControlValueChangeListener {
        void onControlValueChanged(float value);
    }

    private RectF mArcRect;
    private Paint mArcPaint;

    private float mTouchRadius;
    private Paint mTouchPaint;
    private Paint mTouchTextPaint;
    private Rect mTouchTextBounds;
    private Paint mTouchNeedPaint;
    private Paint mTouchNeedTextPaint;
    private Rect mTouchNeedTextBounds;

    private Paint mValuePaint;
    private Rect mValueBounds;
    private Paint mValueNeedPaint;
    private Rect mValueNeedBounds;
    private Paint mRangePaint;
    private Rect mRangeBounds;

    private float mMinValue;
    private float mMaxValue;
    private float mValue;
    private float mValueNeed;
    private String mText;
    private int mColor;
    private int mColorArc;
    private int mColorValueNeed;

    private boolean mIsTouching;
    private boolean mIsDisable;
    private boolean mIsNotifyImmediately;

    private OnControlValueChangeListener mOnControlValueChangeListener;

    public ControlView(Context context) {
        super(context);
        init(null);
    }

    public ControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);

        mTouchPaint = new Paint();
        mTouchPaint.setAntiAlias(true);
        mTouchPaint.setStyle(Paint.Style.STROKE);
        mTouchPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
        mTouchTextPaint = new Paint();
        mTouchTextPaint.setTextSize(0);
        mTouchTextPaint.setTextAlign(Paint.Align.CENTER);
        mTouchTextBounds = new Rect();

        mTouchNeedPaint = new Paint();
        mTouchNeedPaint.setAntiAlias(true);
        mTouchNeedPaint.setStyle(Paint.Style.FILL);
        mTouchNeedTextPaint = new Paint();
        mTouchNeedTextPaint.setTextSize(0);
        mTouchNeedTextPaint.setTextAlign(Paint.Align.CENTER);
        mTouchNeedTextBounds = new Rect();

        mValuePaint = new Paint();
        mValuePaint.setTextSize(0);
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValueBounds = new Rect();

        mValueNeedPaint = new Paint();

        mValueNeedPaint.setTextSize(0);
        mValueNeedPaint.setTextAlign(Paint.Align.CENTER);
        mValueNeedBounds = new Rect();
        mRangePaint = new Paint();
        mRangePaint.setTextSize(0);
        mRangePaint.setTextAlign(Paint.Align.CENTER);
        mRangeBounds = new Rect();

        final TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.ControlView, 0, 0);
        try {
            setColor(a.getInteger(R.styleable.ControlView_colorBase, Color.parseColor("#607D8B")));
            setColorArc(a.getInteger(R.styleable.ControlView_colorArc, Color.GRAY));
            setColorValueNeed(a.getInteger(R.styleable.ControlView_colorValueNeed, Color.WHITE));
            setRange(a.getFloat(R.styleable.ControlView_rangeMin, 0),
                    a.getFloat(R.styleable.ControlView_rangeMax, 100));
            setValue(a.getFloat(R.styleable.ControlView_value, 25));
            setValueNeed(a.getFloat(R.styleable.ControlView_valueNeed, 25));

            final String text = a.getString(R.styleable.ControlView_text);
            if (text != null) {
                setText(text);
            } else {
                setText(Html.fromHtml("&#x2103").toString());
            }
        } finally {
            a.recycle();
        }
    }

    public void setColor(@ColorInt int color) {
        mColor = color;

        mArcPaint.setColor(Color.GRAY);
        mTouchPaint.setColor(color);
        mTouchTextPaint.setColor(color);
        mTouchNeedPaint.setColor(color);
        mTouchNeedTextPaint.setColor(Color.WHITE);
        mValuePaint.setColor(color);
        mValueNeedPaint.setColor(color);
        mRangePaint.setColor(color);

        invalidate();
    }

    public void setColorRes(@ColorRes int colorId) {
        setColor(ContextCompat.getColor(getContext(), colorId));
    }

    public void setColorArc(@ColorInt int color) {
        mColorArc = color;

        mArcPaint.setColor(color);
        invalidate();
    }

    public void setColorArcRes(@ColorRes int colorId) {
        setColorArc(ContextCompat.getColor(getContext(), colorId));
    }

    public void setColorValueNeed(@ColorInt int color) {
        mColorValueNeed = color;

        mTouchNeedTextPaint.setColor(color);
        invalidate();
    }

    public void setColorValueNeedRes(@ColorRes int colorId) {
        setColorValueNeed(ContextCompat.getColor(getContext(), colorId));
    }

    public void setRange(float minValue, float maxValue) {
        if (minValue >= maxValue) {
            return;
        }
        mMinValue = minValue;
        mMaxValue = maxValue;
        invalidate();
    }

    public void setValue(float value) {
        if (value < mMinValue) {
            return;
        }
        if (value > mMaxValue) {
            return;
        }
        mValue = value;
        invalidate();
    }

    public float getValue() {
        return mValue;
    }

    public void setValueNeed(float valueNeed) {
        setValueNeed(valueNeed, true);
    }

    private void setValueNeed(float valueNeed, boolean fromUser) {
        if (valueNeed < mMinValue) {
            return;
        }
        if (valueNeed > mMaxValue) {
            return;
        }
        mValueNeed = valueNeed;
        invalidate();

        if (!fromUser && mOnControlValueChangeListener != null) {
            mOnControlValueChangeListener.onControlValueChanged(valueNeed);
        }
    }

    public float getValueNeed() {
        return mValueNeed;
    }

    public void setText(String text) {
        if (text == null) {
            return;
        }
        mText = text;
        invalidate();
    }

    public void setOnControlValueChangeListener(OnControlValueChangeListener listener,
                                                boolean notifyImmediately) {
        mOnControlValueChangeListener = listener;
        mIsNotifyImmediately = notifyImmediately;
    }

    public void disable() {
        mIsDisable = true;
    }

    protected void onDraw(Canvas canvas) {
        drawArc(canvas);
        drawRange(canvas);

        if (!mIsTouching) {
            if (Math.round(mValue) == Math.round(mValueNeed)) {
                mValue = Math.round(mValue);
                mValueNeed = Math.round(mValueNeed);
            }
        }

        if (getValue() != getValueNeed()) {
            drawValue(canvas, mValue, false);
        }
        drawValue(canvas, mValueNeed, true);

        drawText(canvas, mValue, 2.2f, mValuePaint, mValueBounds);
        if (getValue() != getValueNeed()) {
            drawText(canvas, mValueNeed, 4f, mValueNeedPaint, mValueNeedBounds);
        }
    }

    private void drawArc(Canvas canvas) {
        if (mArcRect == null) {
            mArcRect = new RectF(getPadding(), getPadding(), getRadius() * 2 - getPadding(),
                    getRadius() * 2 - getPadding());
        }
        canvas.drawArc(mArcRect, 0, 45, false, mArcPaint);
        canvas.drawArc(mArcRect, 135, 225, false, mArcPaint);
    }

    private void drawRange(Canvas canvas) {
        final String min = String.valueOf(Math.round(mMinValue));
        final String max = String.valueOf(Math.round(mMaxValue));

        if (mRangePaint.getTextSize() == 0) {
            mRangePaint.setTextSize(getRadius() / 14f);
        }

        mRangePaint.getTextBounds(min, 0, min.length(), mRangeBounds);
        canvas.drawText(min, getX(mMinValue), getY(mMinValue), mRangePaint);

        mRangePaint.getTextBounds(max, 0, max.length(), mRangeBounds);
        canvas.drawText(max, getX(mMaxValue), getY(mMaxValue), mRangePaint);
    }

    private void drawValue(Canvas canvas, float value, boolean isNeedValue) {
        final Paint paint;
        final Paint textPaint;
        final Rect textBounds;
        final String valueString = String.valueOf(Math.round(value));

        if (isNeedValue) {
            paint = mTouchNeedPaint;
            textPaint = mTouchNeedTextPaint;
            textBounds = mTouchNeedTextBounds;
        } else {
            paint = mTouchPaint;
            textPaint = mTouchTextPaint;
            textBounds = mTouchTextBounds;
        }
        if (textPaint.getTextSize() == 0) {
            textPaint.setTextSize(mTouchRadius);
        }

        canvas.drawCircle(getX(value), getY(value), mTouchRadius, paint);
        textPaint.getTextBounds(valueString, 0, valueString.length(), textBounds);
        canvas.drawText(valueString, getX(value), getY(value) + textBounds.height() / 2, textPaint);
    }

    private void drawText(@NonNull Canvas canvas, float value, float textSize,
                          @NonNull Paint paint, @NonNull Rect bounds) {
        final String text = String.format(Locale.getDefault(), "%d %s", Math.round(value), mText);
        if (paint.getTextSize() == 0) {
            paint.setTextSize(getRadius() / textSize);
        }
        paint.getTextBounds(text, 0, text.length(), bounds);
        if (value == mValue) {
            canvas.drawText(text, getRadius(), getRadius() + bounds.height() / 2, paint);
        } else {
            canvas.drawText(text, getRadius(), getRadius() * 5 / 3, paint);
        }
    }

    private double getCos(float value) {
        return Math.cos(Math.toRadians(getDegree(value)));
    }

    private float getX(float value) {
        return (float) (getRadius() + getCos(value) * (getRadius() - mTouchRadius));
    }

    private double getSin(float value) {
        return Math.sin(Math.toRadians(getDegree(value)));
    }

    private float getY(float value) {
        return (float) (getRadius() + getSin(value) * (getRadius() - mTouchRadius));
    }

    private float getRadius() {
        return Math.min(getWidth(), getHeight()) / 2;
    }

    private float getPadding() {
        if (mTouchRadius == 0) {
            mTouchRadius = getRadius() / 6;
        }
        return mArcPaint.getStrokeWidth() + mTouchRadius;
    }

    private float getDegree(float value) {
        return (float) (((double) (value - mMinValue)) / ((double) (mMaxValue - mMinValue))) * 270
                + 135;
    }

    private float getValue(float degree) {
        return ((degree - 135) / 270) * (mMaxValue - mMinValue) + mMinValue;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsDisable) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsTouching = true;
                getParent().requestDisallowInterceptTouchEvent(isPointSelected(event));
                break;

            case MotionEvent.ACTION_MOVE:
                if (isPointSelected(event)) {
                    drag(event);

                    if (mOnControlValueChangeListener != null && mIsNotifyImmediately) {
                        mOnControlValueChangeListener.onControlValueChanged(getValueNeed());
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                mIsTouching = false;
                setValueNeed(getValueNeed(), false);
                break;

            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                mIsTouching = false;
                setValueNeed(getValueNeed(), false);
                break;
        }
        return true;
    }

    private void drag(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final float r = getRadius();
        float degree = (float) Math.abs(Math.toDegrees(Math.atan((r - y) / (r - x))));

        if (x < r) {
            if (y < r) {
                degree += 180;
            } else {
                degree = 90 - degree + 90;
            }
        } else {
            if (y < r) {
                degree = 90 - degree + 270;
            } else {
                degree = 360 + degree;
            }
        }
        final float value = getValue(degree);
        if (value >= mMinValue && value <= mMaxValue) {
            mValueNeed = getValue(degree);
        }
        invalidate();
    }

    private boolean isPointSelected(MotionEvent event) {
        final float r = getRadius();
        final float c = (float) Math.sqrt(Math.pow(r - event.getX(), 2) +
                Math.pow(r - event.getY(), 2)) - mTouchRadius;

        final float x = event.getX();
        final float y = event.getY();
        float degree = (float) Math.abs(Math.toDegrees(Math.atan((r - y) / (r - x))));
        if (x < r) {
            if (y < r) {
                degree += 180;
            } else {
                degree = 90 - degree + 90;
            }
        } else {
            if (y < r) {
                degree = 90 - degree + 270;
            } else {
                degree = 360 + degree;
            }
        }
        final float value = getValue(degree);

        return value >= mMinValue && value <= mMaxValue &&
                c >= r - getPadding() - mTouchRadius * 2 && c <= r - getPadding();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final SavedState state = new SavedState(super.onSaveInstanceState());
        state.minValue = mMinValue;
        state.maxValue = mMaxValue;
        state.value = mValue;
        state.valueNeed = mValueNeed;
        state.text = mText;
        state.color = mColor;
        state.colorArc = mColorArc;
        state.colorValueNeed = mColorValueNeed;
        return state;
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }

        final SavedState state = (SavedState) parcelable;
        super.onRestoreInstanceState(state.getSuperState());
        setRange(state.minValue, state.maxValue);
        setValue(state.value);
        setValueNeed(state.valueNeed, false);
        setText(state.text);
        setColor(state.color);
        setColorArc(state.colorArc);
        setColorValueNeed(state.colorValueNeed);
    }

    private static class SavedState extends BaseSavedState {

        float minValue, maxValue, value, valueNeed;
        String text;
        int color, colorArc, colorValueNeed;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            minValue = in.readFloat();
            maxValue = in.readFloat();
            value = in.readFloat();
            valueNeed = in.readFloat();
            text = in.readString();
            color = in.readInt();
            colorArc = in.readInt();
            colorValueNeed = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(minValue);
            out.writeFloat(maxValue);
            out.writeFloat(value);
            out.writeFloat(valueNeed);
            out.writeString(text);
            out.writeInt(color);
            out.writeInt(colorArc);
            out.writeInt(colorValueNeed);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}