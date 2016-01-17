package com.handgold.pjdc.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.activity.PayActivity;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.common.list.SimpleListWorkerAdapter;
import com.handgold.pjdc.entitiy.MenuItemInfo;
import com.handgold.pjdc.entitiy.Order;
import com.handgold.pjdc.ui.listworker.OrderListWorker;
import com.handgold.pjdc.util.CommonUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2015/9/29.
 */
public class OrderShowView extends RelativeLayout {

    private TextView mTextOrderCount = null;

    private TextView mTextDelAll = null;

    private TextView mTextCancel = null;

    private TextView mTextOrderPrice = null;

    private TextView mTextOrderNow = null;

    private ListView mListView = null;

    private Order mOrder = null;

    private Activity mActivity = null;

    private OrderListWorker mListWorker;

    private SimpleListWorkerAdapter mListAdapter;

    private OnOrderViewListener mOnOrderViewListener;

    public OrderShowView(Context context) {
        super(context);
    }

    public OrderShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    private static final int SUBMIT_STATE = 1;
    private static final int CONFIRM_STATE = 2;

    private int mCurState = 1;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        mTextOrderCount = (TextView) findViewById(R.id.text_order_count);
        mTextDelAll = (TextView) findViewById(R.id.text_order_del_all);
        mTextCancel = (TextView) findViewById(R.id.text_order_cancel);
        mTextOrderPrice = (TextView) findViewById(R.id.text_order_price);
        mTextOrderNow = (TextView) findViewById(R.id.text_order_now);
        mListView = (ListView) findViewById(R.id.order_show_lv);

        mTextDelAll.setOnClickListener(mOnclickListener);
        mTextCancel.setOnClickListener(mOnclickListener);
        mTextOrderNow.setOnClickListener(mOnclickListener);
    }

    public void initData() {
        mCurState = SUBMIT_STATE;
        //获取订单数据
        mOrder = (Order) ((ApplicationEx)(mActivity).getApplication()).receiveInternalActivityParam("order");
        if (mOrder == null) {
            mOrder = new Order();
            mOrder.setOrderId(UUID.randomUUID().toString());
            mOrder.setMenuList(new ArrayList<MenuItemInfo>());
            mOrder.setStatus(Order.OrderStatus.NOTSUBMIT);
            ((ApplicationEx) (mActivity).getApplication()).setInternalActivityParam("order", mOrder);
        }

        setTextOrderCount(mOrder.getSize());
        setTextOrderPrice(CommonUtils.round(mOrder.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));

        // 配置listworker
        if (mListWorker == null) {
            mListWorker = new OrderListWorker(mActivity, mOrder.getMenuList(), new OrderListWorkerCallBack());
            mListAdapter = new SimpleListWorkerAdapter(mListWorker);
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(mListWorker);
        } else {
            mListWorker.setData(mOrder.getMenuList());
            mListAdapter.notifyDataSetChanged();
        }
    }

    public void setData(List<MenuItemInfo> dataList) {
        if (mListWorker != null) {
            mListWorker.setData(dataList);
            mListAdapter.notifyDataSetChanged();
        }
    }

    private View.OnClickListener mOnclickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (v == mTextDelAll) {
                mOrder.clear();
                setTextOrderCount(mOrder.getSize());
                setTextOrderPrice(CommonUtils.round(mOrder.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));
                setData(mOrder.getMenuList());
                exitView();
            }else  if (v == mTextCancel) {
                exitView();
            }else {
                if (mCurState == SUBMIT_STATE) {
                    mTextOrderNow.setText("确认订单");
                    mOrder.setStatus(Order.OrderStatus.SUBMITED);
                    mCurState = CONFIRM_STATE;
                }else {
                    mTextOrderNow.setText("立刻下单");
                    mCurState = SUBMIT_STATE;
                    //todo 上传参数
                    Intent intent = new Intent(mActivity, PayActivity.class);
                    intent.putExtra("totalPrice", mOrder.getTotalPrice());
                    mActivity.startActivity(intent);
                }
            }
        }
    };

    public void setTextOrderCount(int count) {
        mTextOrderCount.setText("" + count);
        if (count > 0) {
            mTextOrderNow.setTextColor(Color.WHITE);
            mTextOrderNow.setBackgroundResource(R.color.button_settlement);
        }else{
            mTextOrderNow.setTextColor(Color.BLACK);
            mTextOrderNow.setBackgroundResource(R.color.transparent);
        }
    }

    public void setTextOrderPrice(float price) {
        mTextOrderPrice.setText("总计¥" + price);
    }

    public void exitView() {
        if (getAnimation() != null) {
            return;
        }
        startAnimation(orderShowViewHideAnimation());
        if (mOnOrderViewListener != null) {
            mOnOrderViewListener.onBackToFoodFrag(mOrder);
        }
    }

    private Animation orderShowViewHideAnimation() {

        AnimationSet animationSet = new AnimationSet(false);
        TranslateAnimation translateAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0.0f, TranslateAnimation.RELATIVE_TO_PARENT, 1.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.0f, TranslateAnimation.RELATIVE_TO_PARENT, 1.0f);
//        translateAnimation.setInterpolator(new OvershootInterpolator(1.5f));
        animationSet.addAnimation(translateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        animationSet.addAnimation(alphaAnimation);
//        animationSet.setFillAfter(true);
        animationSet.setDuration(400);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((ViewGroup) getParent()).setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return animationSet;
    }

    public boolean isVisible() {
        return (((ViewGroup)getParent()).getVisibility() == VISIBLE);
    }

    /**
     * OrderListWorker回调类
     */
    class OrderListWorkerCallBack implements OrderListWorker.OnListWorkerListener {


        @Override
        public void onItemClick(int index) {

        }

        @Override
        public void onAddMenuClicked(Object itemData) {
            MenuItemInfo menu = (MenuItemInfo) itemData;
            mOrder.addMenu(menu);
            setTextOrderCount(mOrder.getSize());
            setTextOrderPrice(CommonUtils.round(mOrder.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));
            mListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSubMenuClicked(Object itemData) {
            MenuItemInfo menu = (MenuItemInfo) itemData;
            mOrder.delMenu(menu);
            setTextOrderCount(mOrder.getSize());
            setTextOrderPrice(CommonUtils.round(mOrder.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));
            mListAdapter.notifyDataSetChanged();
        }

    }

    public void setOnOrderViewListener(OnOrderViewListener listener) {
        this.mOnOrderViewListener = listener;
    }

    public interface OnOrderViewListener{
        void onBackToFoodFrag(Order order);
    }
}
