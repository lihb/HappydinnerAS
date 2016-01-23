package com.handgold.pjdc.ui.Menu;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.animation.*;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.DataManager;
import com.handgold.pjdc.entitiy.MenuItemInfo;
import com.handgold.pjdc.entitiy.Order;
import com.handgold.pjdc.ui.widget.AddToShopCartAnimation;
import com.handgold.pjdc.ui.widget.OrderShowView;
import com.handgold.pjdc.ui.widget.PopupMenuDetailView;
import com.handgold.pjdc.ui.widget.ShoppingCartView;
import com.handgold.pjdc.util.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 类说明：
 *
 * @author Administrator
 * @version 1.0
 * @date 2015/6/16
 */

public class FoodRightFragment extends Fragment {

    private GridView mGridView;

    private ShoppingCartView mShoppingCardView;

    private OrderShowView mOrderShowView;

    private RelativeLayout mOrderShowViewRelativeLayout;

    private PopupMenuDetailView mPopupMenuDetailView;

    private RelativeLayout mPopupMenuDetailViewRelativeLayout;

    private ArrayList<MenuItemInfo> mDataList;

    private FoodRightAdapter mAdapter;

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int[] location = new int[2];
                View subView = ((ViewGroup) v).getChildAt(0);
                subView.getLocationInWindow(location);
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (x < location[0] || x > location[0] + subView.getWidth() ||
                        y < location[1] || y > location[1] + subView.getHeight()) {
                    if (subView instanceof OrderShowView) {
                        OrderShowView orderShowView = (OrderShowView) subView;
                        if (orderShowView.getCurState() == OrderShowView.SUBMIT_STATE) { // 未提交订单，才能关闭页面，提交后，不能关闭页面。
                            orderShowView.exitView();
                        }
                    }/*else if(subView instanceof PopupMenuDetailView){//菜品介绍页面，点击遮罩不关闭页面
                        ((PopupMenuDetailView)subView).exitView();
                    }*/
                }
            }
            return true;
        }
    };

    private FrameLayout animation_viewGroup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.food_right_fragment, null);
        mGridView = (GridView) view.findViewById(R.id.food_right_lv);
        setGridViewAnimation(mGridView);
        mShoppingCardView = (ShoppingCartView) getActivity().findViewById(R.id.shopping_cart_view);
        mShoppingCardView.setVisibility(View.VISIBLE);
        mOrderShowView = (OrderShowView) getActivity().findViewById(R.id.order_show_view);
        mOrderShowViewRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.order_show_view_relativeLayout);
        mOrderShowView.setActivity(getActivity());
        mOrderShowView.initData();

        mPopupMenuDetailView = (PopupMenuDetailView) getActivity().findViewById(R.id.popup_detail_view);
        mPopupMenuDetailViewRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.popup_view_relativeLayout);

        mShoppingCardView.setOnClickListener(mOnClickListener);
        mOrderShowViewRelativeLayout.setOnTouchListener(mOnTouchListener);
        mPopupMenuDetailViewRelativeLayout.setOnTouchListener(mOnTouchListener);

        mOrderShowView.setOnOrderViewListener(new OrderShowView.OnOrderViewListener() {
            @Override
            public void onBackToFoodFrag(Order order) {
                if (mAdapter != null) {
                    mAdapter.setData(mDataList);
                    mAdapter.notifyDataSetChanged();
                }
                mShoppingCardView.setTextCount(order.getSize());
                mShoppingCardView.setTextPrice(CommonUtils.round(order.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));
            }
        });
        animation_viewGroup = createAnimLayout();
        return view;
    }

    /**
     * 设置菜品进入动画
     * @param gridView
     */
    private void setGridViewAnimation(GridView gridView) {
        TranslateAnimation transAnim = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 1.0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
        transAnim.setDuration(600);
        LayoutAnimationController lac = new LayoutAnimationController(transAnim, 0.5f);
        gridView.setLayoutAnimation(lac);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        mDataList = bundle.getParcelableArrayList("dataList");
        // 配置listworker
        if (mAdapter == null) {
            mAdapter = new FoodRightAdapter(mDataList, getActivity(), new FoodRightAdapterCallBack());
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(mDataList);
            mAdapter.notifyDataSetChanged();
        }

        updateView();
    }


   private View.OnClickListener mOnClickListener = new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           if (v == mShoppingCardView) {
               if (DataManager.order.getSize() <= 0) {
                   ObjectAnimator animator = ObjectAnimator.ofFloat(mShoppingCardView, "translationX", 0, 30, -30, 30, -30,20, -20, 10, -10, 0);
                   animator.setDuration(500);
                   animator.start();
                   CommonUtils.toastText(getActivity(), "购物车是空的哦~");
               }else {
                   if (mOrderShowViewRelativeLayout.getVisibility() == View.VISIBLE) {
                       return;
                   }
                   mOrderShowViewRelativeLayout.setVisibility(View.VISIBLE);
                   mOrderShowView.setData(DataManager.order.getMenuList());
                   mOrderShowView.startAnimation(orderShowViewAnimation());
               }
           }
       }
   };

    private Animation orderShowViewAnimation() {

        AnimationSet animationSet = new AnimationSet(false);
        TranslateAnimation translateAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 1.0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 1.0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.0f);
//        translateAnimation.setInterpolator(new OvershootInterpolator(1.5f));
        animationSet.addAnimation(translateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        animationSet.addAnimation(alphaAnimation);
//        animationSet.setFillAfter(true);
        animationSet.setDuration(400);

        return animationSet;
    }
    /**
     * FoodRightAdapter回调类
     */
    class FoodRightAdapterCallBack implements FoodRightAdapter.OnFoodRightAdapterListener {

        @Override
        public void onItemClicked(Object itemData, int index) {

        }

        @Override
        public void onAddMenuClicked(Object itemData, View v) {
            MenuItemInfo menu = (MenuItemInfo) itemData;
            DataManager.order.addMenu(menu);
            int[] startLocation = new int[2];
            v.getLocationInWindow(startLocation);
            doAnim(startLocation);
            updateView();
        }

        @Override
        public void onSubMenuClicked(Object itemData) {
            MenuItemInfo menu = (MenuItemInfo) itemData;
            DataManager.order.delMenu(menu);
            updateView();
        }

        @Override
        public void onAddToOrderClicked(Object itemData) {

        }

        @Override
        public void onGotoLookDesc(View view, Object itemData) {
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(300);
            mPopupMenuDetailView.startAnimation(scaleAnimation);
            mPopupMenuDetailViewRelativeLayout.setVisibility(View.VISIBLE);
            MenuItemInfo menu = (MenuItemInfo) itemData;
            mPopupMenuDetailView.initData(menu);
            mPopupMenuDetailView.setOnPopDetailViewListener(new PopupMenuDetailView.OnPopDetailViewListener() {
                @Override
                public void onDataChanged(MenuItemInfo menu, int operation) {
                    if (operation == PopupMenuDetailView.OPERATION_ADD) {
                        DataManager.order.addMenu(menu);
                    }else if (operation == PopupMenuDetailView.OPERATION_SUB){
                        DataManager.order.delMenu(menu);
                    }
                    updateView();
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * 更新购物车和订单页面的菜品个数和总价数据
     */
    private void updateView() {
        mShoppingCardView.setTextCount(DataManager.order.getSize());
        mShoppingCardView.setTextPrice(CommonUtils.round(DataManager.order.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));
        mOrderShowView.setTextOrderCount(DataManager.order.getSize());
        mOrderShowView.setTextOrderPrice(CommonUtils.round(DataManager.order.getTotalPrice(), 1, BigDecimal.ROUND_HALF_UP));
    }

    public void emptyShopCart() {
        mShoppingCardView.setTextCount(0);
        mShoppingCardView.setTextPrice(0.0f);
    }

    /************************添加到购物车动画*******************************************/

    private void doAnim(int[] start_location){
        setAnim(start_location);
    }

    /**
     * @Description: 创建动画层
     * @param
     */
    private FrameLayout createAnimLayout(){
        ViewGroup rootView = (ViewGroup)getActivity().getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(getActivity());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;

    }

    /**
     * @deprecated 将要执行动画的view 添加到动画层
     * @param vg
     *        动画运行的层 这里是frameLayout
     * @param view
     *        要运行动画的View
     * @param location
     *        动画的起始位置
     * @return
     */
    private View addViewToAnimLayout(ViewGroup vg,View view,int[] location){
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);

        return view;
    }


    /**
     * 动画效果设置
     *
     * @param start_location
     *        起始位置
     */
    private void setAnim(int[] start_location){

        final TextView textView = new TextView(getActivity());
        textView.setText("1");
        textView.setTextColor(0xffffffff);
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.addto_cart_text_bg);
        final View view = addViewToAnimLayout(animation_viewGroup, textView,start_location);
        view.setAlpha(0.6f);

        int[] end_location = new int[2];
        mShoppingCardView.getLocationInWindow(end_location);
        int endX = -start_location[0] + mShoppingCardView.getWidth() / 2 + 40;
        int endY = end_location[1]-start_location[1];

//        Animation mTranslateAnimationX = new TranslateAnimation(0, endX, 0, 0);
//        mTranslateAnimationX.setInterpolator(new LinearInterpolator());
//
//        Animation mTranslateAnimationY = new TranslateAnimation(0, 0, 0, endY);
//        mTranslateAnimationY.setInterpolator(new BounceInterpolator());
        AddToShopCartAnimation customAnimation = new AddToShopCartAnimation(0, endX, 0, endY);
        AnimationSet mAnimationSet = new AnimationSet(false);

        mAnimationSet.setFillAfter(true);
//        mAnimationSet.addAnimation(mTranslateAnimationX);
//        mAnimationSet.addAnimation(mTranslateAnimationY);
        mAnimationSet.addAnimation(customAnimation);
        mAnimationSet.setDuration(600);
        mAnimationSet.setAnimationListener(new MyAnimationListener(view));
        view.startAnimation(mAnimationSet);
    }

    private class MyAnimationListener implements Animation.AnimationListener{

        View mAnimateView;

        public MyAnimationListener(View view) {
            this.mAnimateView = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            final ViewGroup group = (ViewGroup) mAnimateView.getParent();
            if (group != null) {
                group.post(new Runnable() {
                    @Override
                    public void run() {
                        group.removeView(mAnimateView);
                    }
                });
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FoodRightFragment"); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FoodRightFragment");
    }
}
