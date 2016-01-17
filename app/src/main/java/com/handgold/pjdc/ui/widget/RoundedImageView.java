package com.handgold.pjdc.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.handgold.pjdc.R;

/**
 * Created by Administrator on 2015/9/30.
 */
public class RoundedImageView extends ImageView {

    public static final int CORNER_NONE = 0;               // 0
    public static final int CORNER_TOP_LEFT = 1;           // 1
    public static final int CORNER_TOP_RIGHT = 1 << 1;     // 2
    public static final int CORNER_BOTTOM_LEFT = 1 << 2;   // 4
    public static final int CORNER_BOTTOM_RIGHT = 1 << 3;  // 8
    public static final int CORNER_ALL = CORNER_TOP_LEFT | CORNER_TOP_RIGHT | CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT; // 15

    private int mCornerLoc = CORNER_NONE;
    private int mCornerRadius = 0;

    public RoundedImageView(Context context) {
        super(context);
        setImage(mCornerRadius, mCornerLoc);
    }

    // 要使用自定义属性，以下2个构造函数都必须覆写
    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);
        mCornerLoc = a.getInt(R.styleable.RoundedImageView_corner_loc, CORNER_NONE);
        mCornerRadius = a.getInt(R.styleable.RoundedImageView_corner_radius, 0);
        a.recycle();
        setImage(mCornerRadius, mCornerLoc);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setImage(mCornerRadius,mCornerLoc);
    }

    public void setImage(int radius, int corners) {
        Drawable drawable = getDrawable();
        Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
        setImageDrawable(new RoundedDrawable(bmp, radius, corners));
    }

    public static class RoundedDrawable extends Drawable {

        protected final float cornerRadius;

        protected final RectF mRect = new RectF(), mBitmapRect;
        protected final BitmapShader bitmapShader;
        protected final Paint paint;
        private int corners;

        public RoundedDrawable(Bitmap bitmap, int cornerRadius, int corners) {
            this.cornerRadius = cornerRadius;
            this.corners = corners;
            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(bitmapShader);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRect.set(0, 0, bounds.width(), bounds.height());

            // Resize the original bitmap to fit the new bound
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
            bitmapShader.setLocalMatrix(shaderMatrix);

        }

        @Override
        public void draw(Canvas canvas) {
            Log.i("RoundedImageView", "lihongbing test in RoundedImageView-->draw()");
            canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, paint);
            int notRoundedCorners = corners ^ CORNER_ALL;
            if ((notRoundedCorners & CORNER_TOP_LEFT) != 0) {
                canvas.drawRect(0, 0, cornerRadius, cornerRadius, paint);
            }
            if ((notRoundedCorners & CORNER_TOP_RIGHT) != 0) {
                canvas.drawRect(mRect.right - cornerRadius, 0, mRect.right, cornerRadius, paint);
            }
            if ((notRoundedCorners & CORNER_BOTTOM_LEFT) != 0) {
                canvas.drawRect(0, mRect.bottom - cornerRadius, cornerRadius, mRect.bottom, paint);
            }
            if ((notRoundedCorners & CORNER_BOTTOM_RIGHT) != 0) {
                canvas.drawRect(mRect.right - cornerRadius, mRect.bottom - cornerRadius, mRect.right, mRect.bottom, paint);
            }
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            paint.setColorFilter(cf);
        }
    }
}