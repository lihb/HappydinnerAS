package com.handgold.pjdc.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.handgold.pjdc.R;

/**
 * Created by Administrator on 2015/9/29.
 */
public class ShoppingCartView extends RelativeLayout {

    private TextView mTextCount = null;

    private TextView mTextPrice = null;

    public ShoppingCartView(Context context) {
        super(context);
    }

    public ShoppingCartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShoppingCartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextCount = (TextView) findViewById(R.id.text_cart_count);
        mTextPrice = (TextView) findViewById(R.id.text_cart_price);
    }

    public void setTextCount(int count) {
        mTextCount.setText("" + count + "份");
    }

    public void setTextPrice(float price) {
        mTextPrice.setText("¥" + price);
    }
}
