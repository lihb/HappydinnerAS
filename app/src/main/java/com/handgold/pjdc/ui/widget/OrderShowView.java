package com.handgold.pjdc.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handgold.pjdc.R;
import com.handgold.pjdc.action.ApiManager;
import com.handgold.pjdc.action.ServiceGenerator;
import com.handgold.pjdc.activity.PayActivity;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.Constant;
import com.handgold.pjdc.base.DataManager;
import com.handgold.pjdc.common.list.SimpleListWorkerAdapter;
import com.handgold.pjdc.entitiy.MenuItemInfo;
import com.handgold.pjdc.entitiy.Order;
import com.handgold.pjdc.entitiy.OrderPayInfo;
import com.handgold.pjdc.entitiy.PlaceOrderInfo;
import com.handgold.pjdc.ui.listworker.OrderListWorker;
import com.handgold.pjdc.util.CommonUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        setTextOrderCount(DataManager.order.getSize());
        setTextOrderPrice(CommonUtils.round(DataManager.order.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));

        // 配置listworker
        if (mListWorker == null) {
            mListWorker = new OrderListWorker(mActivity, DataManager.order.getMenuList(), new OrderListWorkerCallBack());
            mListAdapter = new SimpleListWorkerAdapter(mListWorker);
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(mListWorker);
        } else {
            mListWorker.setData(DataManager.order.getMenuList());
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
                DataManager.order.clear();
                setTextOrderCount(DataManager.order.getSize());
                setTextOrderPrice(CommonUtils.round(DataManager.order.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));
                setData(DataManager.order.getMenuList());
                exitView();
            }else  if (v == mTextCancel) {
                exitView();
            }else {
                if (mCurState == SUBMIT_STATE) {
                    mTextOrderNow.setText("结算");
                    DataManager.order.setStatus(Order.OrderStatus.SUBMITED);
                    mCurState = CONFIRM_STATE;
                    String deviceid = (Constant.deviceid);
                    ArrayList<MenuItemInfo> dataList = (ArrayList<MenuItemInfo>) DataManager.order.getMenuList();
                    Gson gson = new Gson();
                    String ss = gson.toJson(dataList);
                    Log.i("----------", ss);
                    ApiManager.PlaceOrderParas paras = new ApiManager.PlaceOrderParas();
                    paras.deviceid = deviceid;
                    paras.menuListJson = ss;
                    ServiceGenerator.createService(ApiManager.class)
                            .getPlaceOrder(paras)
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<PlaceOrderInfo>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(PlaceOrderInfo placeOrderInfo) {
                                    if (placeOrderInfo.result_code == 0) {
                                        Log.i("timestamp", placeOrderInfo.timestamp);
                                        DataManager.timestamp = placeOrderInfo.timestamp;
                                        CommonUtils.toastText(mActivity, mActivity.getString(R.string.order_success_info));
                                    }
                                }
                            });
                }else {
//                    mTextOrderNow.setText("");
//                    mCurState = SUBMIT_STATE;
                    ServiceGenerator.createService(ApiManager.class)
                            .getBuyOrder(Constant.deviceid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<OrderPayInfo>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();

                                }

                                @Override
                                public void onNext(OrderPayInfo orderPayInfo) {
                                    if (orderPayInfo.result_code == 0) {
                                        Log.i("order_pay_id", orderPayInfo.order_pay_id);
                                        DataManager.order_pay_id = orderPayInfo.order_pay_id;
                                        Intent intent = new Intent(mActivity, PayActivity.class);
                                        intent.putExtra("totalPrice", DataManager.order.getTotalPrice());
                                        mActivity.startActivity(intent);
                                    }
                                }
                            });
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
            mOnOrderViewListener.onBackToFoodFrag(DataManager.order);
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
            DataManager.order.addMenu(menu);
            setTextOrderCount(DataManager.order.getSize());
            setTextOrderPrice(CommonUtils.round(DataManager.order.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));
            mListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSubMenuClicked(Object itemData) {
            MenuItemInfo menu = (MenuItemInfo) itemData;
            DataManager.order.delMenu(menu);
            setTextOrderCount(DataManager.order.getSize());
            setTextOrderPrice(CommonUtils.round(DataManager.order.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));
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
