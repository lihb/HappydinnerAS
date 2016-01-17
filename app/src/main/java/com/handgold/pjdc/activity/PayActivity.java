package com.handgold.pjdc.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.BaseActivity;
import com.handgold.pjdc.ui.Pay.PayLeftFragment;
import com.handgold.pjdc.ui.Pay.PayRightZhiFuBaoFragment;
import com.handgold.pjdc.ui.widget.HeadView;
import com.handgold.pjdc.ui.widget.PopupPayInfoView;
import com.umeng.analytics.MobclickAgent;

/**
 * 类说明：
 *
 * @author Administrator
 * @version 1.0
 * @date 2015/6/16
 */

public class PayActivity extends BaseActivity {

    private HeadView headView;

    private LinearLayout mLeftFragLayout;
    private LinearLayout mRightFragLayout;

    private FragmentManager mFragmentManager;

    private FragmentTransaction mTransaction;

    private PayRightZhiFuBaoFragment payRightZhiFuBaoFragment;

    private PayLeftFragment payLeftFragment;

    private RelativeLayout mPayInfoRelativeLayout;

    private PopupPayInfoView mPopupPayInfoView;

    private BitMatrix mBitMatrix = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_pay);
        initView();
        mFragmentManager = getFragmentManager();
        initFragment();

    }

    private void initView() {
        headView = new HeadView(this);
        headView.h_left_tv.setText("返回");
        headView.h_title.setText("选择支付方式付款");
        headView.h_right_tv_llyt.setVisibility(View.VISIBLE);
        headView.h_right_tv.setText("");
        headView.h_left.setOnClickListener(mOnClickListener);
        headView.h_left_rlyt.setOnClickListener(mOnClickListener);

        mPayInfoRelativeLayout = (RelativeLayout) findViewById(R.id.popup_pay_info_relativeLayout);
        mPopupPayInfoView = (PopupPayInfoView) findViewById(R.id.popup_pay_info_view);

        mLeftFragLayout = (LinearLayout) findViewById(R.id.left_frag);
        mRightFragLayout = (LinearLayout) findViewById(R.id.right_frag);

        mLeftFragLayout.setBackgroundResource(R.drawable.paying_left);
        mRightFragLayout.setBackgroundResource(R.drawable.paying_right);

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.head_left:
                case R.id.head_left_rlyt:
                    finish();
//                    mPayInfoRelativeLayout.setVisibility(View.VISIBLE);
//                    mPopupPayInfoView.updateUI(true);
                    break;

                default:
                    break;
            }
        }
    };

    private void initFragment() {
        mTransaction = mFragmentManager.beginTransaction();

        if (payLeftFragment == null) {
            payLeftFragment = new PayLeftFragment();
            mTransaction.replace(R.id.left_frag, payLeftFragment, "pay_left_frag");
        }

        if (payRightZhiFuBaoFragment == null) {
            payRightZhiFuBaoFragment = new PayRightZhiFuBaoFragment();
            Intent intent = getIntent();
            float price = intent.getFloatExtra("totalPrice", 0f);
            Bundle bundle = new Bundle();
            bundle.putFloat("price", price);
            payRightZhiFuBaoFragment.setArguments(bundle);
            mTransaction.replace(R.id.right_frag, payRightZhiFuBaoFragment, "pay_right_zhifubao_frag");
        }
        mTransaction.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /**
     * 生成二维码图片
     * @param imageView
     * @param url
     */
    public void generateQrImg(ImageView imageView, String url) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            mBitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, imageView.getLayoutParams().height, imageView.getLayoutParams().width);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //show the QRCode image
        imageView.setImageBitmap(toBitmap(mBitMatrix));
    }

    /**
     * Writes the given Matrix on a new Bitmap object.
     *
     * @param matrix the matrix to write.
     * @return the new {@link Bitmap}-object.
     */
    public static Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

}
