package com.handgold.pjdc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.handgold.pjdc.R;
import com.handgold.pjdc.entitiy.Order;
import com.handgold.pjdc.ui.Order.OrderRightFragment;
import com.handgold.pjdc.ui.widget.HeadView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by lihb on 15/7/9.
 */
public class VideoListActivity extends FragmentActivity implements OrderRightFragment.RefreshLeftFragListener {

    private HeadView headView;

    private OrderRightFragment mOrderRightFragment;

    private FragmentManager fragmentManager;

    public static final int VIDEOLISTACTIVITY = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 去除title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        initView();
        fragmentManager = getSupportFragmentManager();
        initFragment();

    }

    private void initView() {
        headView = new HeadView(this);
        headView.h_left_tv.setText("返回");
        headView.h_title.setText("菜品视频列表");
        headView.h_right_tv_llyt.setVisibility(View.VISIBLE);
        headView.h_right_tv.setText("");
        headView.h_left.setOnClickListener(mOnClickListener);
        headView.h_left_rlyt.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.head_left:
                case R.id.head_left_rlyt:
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    private void initFragment() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        boolean rightneedAdd = false;

        String rightTag = createFragmentTag(R.id.video_list_frag);
        mOrderRightFragment = (OrderRightFragment) fragmentManager.findFragmentByTag(rightTag);
        if (mOrderRightFragment == null) {
            rightneedAdd = true;
            mOrderRightFragment = new OrderRightFragment();
            Bundle args = new Bundle();
            args.putInt(OrderRightFragment.ORDERRIGHTFRAGMENT, VIDEOLISTACTIVITY);
            mOrderRightFragment.setArguments(args);
        }

        if (rightneedAdd) {
            fragmentTransaction.replace(R.id.video_list_frag, mOrderRightFragment, rightTag);
        }

        if (rightneedAdd) {
            fragmentTransaction.commit();
        }

    }

    private String createFragmentTag(int id) {
        return this.getClass().getSimpleName() + "_" + id;
    }

    @Override
    public void changeDataToLeft(Order order) {

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
}
