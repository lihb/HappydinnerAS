package com.handgold.pjdc.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Administrator on 2015/9/30.
 */
public class PopupVideoView extends VideoView {
    public PopupVideoView(Context context) {
        super(context);
    }

    public PopupVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(wSize,hSize);
    }
}
