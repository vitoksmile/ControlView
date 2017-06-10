package com.vitoksmile.library.builder.controlview;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.vitoksmile.library.view.controlview.ControlView;

public class ControlViewBuilder {

    private float mMinValue;
    private float mMaxValue;
    private float mValue;
    private float mValueNeed;
    private String mText;
    private int mColor;
    private int mColorArc;
    private int mColorValueNeed;

    public ControlViewBuilder() {
    }

    public ControlViewBuilder range(float minValue, float maxValue) {
        mMinValue = minValue;
        mMaxValue = maxValue;
        return this;
    }

    public ControlViewBuilder value(float value) {
        mValue = value;
        return this;
    }

    public ControlViewBuilder valueNeed(float valueNeed) {
        mValueNeed = valueNeed;
        return this;
    }

    public ControlViewBuilder value(float value, float valueNeed) {
        mValue = value;
        mValueNeed = valueNeed;
        return this;
    }

    public ControlViewBuilder text(@NonNull String text) {
        mText = text;
        return this;
    }

    public ControlViewBuilder color(@ColorInt int color) {
        mColor = color;
        return this;
    }

    public ControlViewBuilder colorRes(@NonNull Context context, @ColorRes int resourceId) {
        mColor = ContextCompat.getColor(context, resourceId);
        return this;
    }

    public ControlViewBuilder colorArc(@ColorInt int color) {
        mColorArc = color;
        return this;
    }

    public ControlViewBuilder colorArcRes(@NonNull Context context, @ColorRes int colorArcId) {
        mColorArc = ContextCompat.getColor(context, colorArcId);
        return this;
    }

    public ControlViewBuilder colorValueNeed(@ColorInt int color) {
        mColorValueNeed = color;
        return this;
    }

    public ControlViewBuilder colorValueNeedRes(@NonNull Context context,
                                                @ColorRes int colorValueNeedId) {
        mColorValueNeed = ContextCompat.getColor(context, colorValueNeedId);
        return this;
    }

    public ControlViewBuilder color(@ColorInt int color, @ColorInt int colorArc,
                                    @ColorInt int colorValueNeed) {
        mColor = color;
        mColorArc = colorArc;
        mColorValueNeed = colorValueNeed;
        return this;
    }

    public ControlViewBuilder colorRes(@NonNull Context context, @ColorRes int colorId,
                                       @ColorRes int colorArcId, @ColorRes int colorValueNeedId) {
        mColor = ContextCompat.getColor(context, colorId);
        mColorArc = ContextCompat.getColor(context, colorArcId);
        mColorValueNeed = ContextCompat.getColor(context, colorValueNeedId);
        return this;
    }

    public void build(@NonNull ControlView controlView) {
        controlView.setRange(mMinValue, mMaxValue);
        controlView.setValue(mValue);
        controlView.setValueNeed(mValueNeed);
        controlView.setText(mText);
        controlView.setColor(mColor);
        controlView.setColorArc(mColorArc);
        controlView.setColorValueNeed(mColorValueNeed);
    }

}
