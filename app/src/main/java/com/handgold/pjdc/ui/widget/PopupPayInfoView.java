package com.handgold.pjdc.ui.widget;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.handgold.pjdc.R;

/**
 * Created by Administrator on 2015/10/26.
 */
public class PopupPayInfoView extends RelativeLayout {

    private ImageView mPayStatusImg = null;

    private TextView mPayStatusText = null;

    private TextView mPayDescText = null;

    public static final int DURATION = 5;

    /**
     * 付款是否成功标志
     */
    private boolean isSuccess = true;
    private boolean isRuuning = true;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public PopupPayInfoView(Context context) {
        super(context);
    }

    public PopupPayInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupPayInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        mPayStatusImg = (ImageView) findViewById(R.id.imageview_pay_status);
        mPayStatusText = (TextView) findViewById(R.id.text_pay_status);
        mPayDescText = (TextView) findViewById(R.id.text_pay_desc);
    }

    public void updateUI(boolean successed) {
        if (successed) {
            mPayStatusImg.setImageResource(R.drawable.icon_success);
            mPayStatusText.setText("付款成功！");
            mPayStatusText.setTextColor(0xff000000);
            mPayDescText.setText("请耐心等候您的餐点\n\n     " + DURATION + "S后自动返回");
            isRuuning = true;
//            new Thread(new MyRunnable()).start(); // 法1：使用线程+handler实现倒计时
//            new CountDownTimer(6000, 1000){       // 法2：使用CountDownTimer实现倒计时
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    mPayDescText.setText("请耐心等候您的餐点\n\n     "+ millisUntilFinished / 1000 +"S后自动返回");
//                }
//
//                @Override
//                public void onFinish() {
//                    exitView();
//                }
//            }.start();
//            mHandler.sendEmptyMessageDelayed(DURATION - 1, 1000); // 法3：单独使用handler实现倒计时
            postDelayed(mRunnable, 1000); // // 法4：不使用handler，使用view里面的postDelay方法
        } else {
            mPayStatusImg.setImageResource(R.drawable.icon_error);
            mPayStatusText.setTextColor(0xffff0000);
            mPayStatusText.setText("付款失败！");
            mPayDescText.setText("请重新执行付款操作.");
        }
    }

    private Runnable mRunnable = new Runnable() {
        private int count = DURATION;
        @Override
        public void run() {
            if (count > 1) {
                mPayDescText.setText("请耐心等候您的餐点\n\n     " + (--count) + "S后自动返回");
            }else {
                exitView();
            }
            postDelayed(this, 1000);
        }
    };

    public void exitView() {
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.setVisibility(GONE);
        }
    }

    public android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what > 0) {
                mPayDescText.setText("请耐心等候您的餐点\n\n     " + msg.what + "S后自动返回");
                sendEmptyMessageDelayed(--msg.what, 1000);
            } else {
                isRuuning = false;
                exitView();
            }
        }
    };

//    private class MyRunnable implements Runnable {
//        int duration = 4;
//
//        @Override
//        public void run() {
//            while (isRuuning) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                mHandler.sendEmptyMessage(duration > 0 ? duration : -1);
//                mHandler.sendEmptyMessageDelayed(duration, 1000);
////                duration--;
//            }
//
//        }
//    }
}
