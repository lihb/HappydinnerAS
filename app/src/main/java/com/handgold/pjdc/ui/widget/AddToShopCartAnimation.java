package com.handgold.pjdc.ui.widget;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by lihb on 15/10/14.
 */
public class AddToShopCartAnimation extends Animation {

    private float mFromXValue = 0.0f;
    private float mToXValue = 0.0f;

    private float mFromYValue = 0.0f;
    private float mToYValue = 0.0f;

    public AddToShopCartAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        mFromXValue = fromXDelta;
        mToXValue = toXDelta;
        mFromYValue = fromYDelta;
        mToYValue = toYDelta;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setFillAfter(true);
        Log.i("AddToShopCartAnimation", "mToXValue = " + mToXValue + ", topHeight = " + mToYValue+
                "mFromXValue = " + mFromXValue + ", mFromYValue = " + mFromYValue);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float tempXValue = mToXValue * 0.6f;
        int topHeight = -110;
        if (interpolatedTime < 0.6) {
            float dx = tempXValue * interpolatedTime * 5 / 3;
            float dy = (float) (Math.sin(interpolatedTime * Math.PI * 5 / 6) * topHeight);
            t.getMatrix().setTranslate(dx, dy);
        }else {
            float dx = tempXValue;
            float dy = topHeight;
            if (mToXValue != tempXValue) {
                dx = tempXValue + ((mToXValue - tempXValue) * (interpolatedTime - 0.6f) *2.5f);
            }
            if (topHeight != mToYValue) {
//                dy = topHeight + ((mToYValue - topHeight) * (interpolatedTime - 0.6f)*2.5f);
                dy = (float)(Math.cos(Math.PI / 2 * ((interpolatedTime - 0.6f)*2.5f))) * (topHeight - mToYValue) +mToYValue ;
            }
            t.getMatrix().setTranslate(dx, dy);
        }
        setFillAfter(true);

    }
}
