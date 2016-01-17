package com.handgold.pjdc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.MenuTypeEnum;
import com.handgold.pjdc.entitiy.MenuItemInfo;
import com.handgold.pjdc.ui.Menu.FoodLeftFragment;
import com.handgold.pjdc.ui.Menu.FoodRightFragment;
import com.handgold.pjdc.ui.VideoPlayerFragment;
import com.handgold.pjdc.ui.widget.HeadView;
import com.handgold.pjdc.ui.widget.OrderShowView;
import com.handgold.pjdc.ui.widget.PopupMenuDetailView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 * Created by lihb on 15/5/16.
 */
public class FoodShowActivity extends FragmentActivity {

    private SortedMap<Integer, List<MenuItemInfo>> sortedMap;

    private HeadView headView;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private VideoPlayerFragment videoPlayerFragment;

    private FoodLeftFragment foodLeftFragment = null;

    private FoodRightFragment foodRightFragment = null;

    private OrderShowView mOrderShowView = null;

    private PopupMenuDetailView mPopupMenuDetailView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 去除title
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去掉Activity上面的状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //去除title和状态栏的操作必须在 super.onCreate()方法之前
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_pay);

        mFragmentManager = getSupportFragmentManager();
        // 初始化界面
        initView();

        // 获取菜品数据
        sortedMap = (SortedMap) ((ApplicationEx) getApplication()).receiveInternalActivityParam("allMenuList");

        initFragment();


    }

    private void initView() {
        headView = new HeadView(this);
        headView.h_left_tv.setText("返回");
        headView.h_title.setVisibility(View.GONE);
        headView.h_title_img.setVisibility(View.VISIBLE);
        headView.h_right_tv_llyt.setVisibility(View.VISIBLE);
        headView.h_right_tv.setText("当前位置：68桌");
        headView.h_right_tv.setTextColor(0x33ffffff);
        headView.h_left.setOnClickListener(mOnClickListener);
        headView.h_left_rlyt.setOnClickListener(mOnClickListener);
//        headView.h_right_tv.setOnClickListener(mOnClickListener);

        mOrderShowView = (OrderShowView) findViewById(R.id.order_show_view);
        mPopupMenuDetailView = (PopupMenuDetailView) findViewById(R.id.popup_detail_view);
    }

    private void initFragment() {
        mTransaction = mFragmentManager.beginTransaction();
        if (foodLeftFragment == null) {
            foodLeftFragment = new FoodLeftFragment();
            mTransaction.replace(R.id.left_frag, foodLeftFragment, "food_left_fragment");

        }

        if (foodRightFragment == null) {
            Bundle bundle = new Bundle();
            ArrayList<MenuItemInfo> dataList = new ArrayList<MenuItemInfo>();
            //初始化右边fragment的数据
            dataList.addAll(sortedMap.get(MenuTypeEnum.RECOMMEND.ordinal()));
            bundle.putParcelableArrayList("dataList", dataList);
            foodRightFragment = new FoodRightFragment();
            foodRightFragment.setArguments(bundle);
            mTransaction.replace(R.id.right_frag, foodRightFragment, "food_right_fragment");
        }

        mTransaction.commit();

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){
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

    @Override
    public void onBackPressed() {
        if (mOrderShowView.isVisible() || mPopupMenuDetailView.isVisible()) {
            mOrderShowView.exitView();
            mPopupMenuDetailView.exitView();
        } else {
            super.onBackPressed();
        }
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
