package com.handgold.pjdc.ui.Menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.MenuTypeEnum;
import com.handgold.pjdc.entitiy.MenuItemInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 * 类说明：
 *
 * @author Administrator
 * @version 1.0
 * @date 2015/6/16
 */

public class FoodLeftFragment extends Fragment {

    @InjectView(R.id.linearlayout_drink)
    LinearLayout linearlayoutDrink;
    @InjectView(R.id.linearlayout_snack)
    LinearLayout linearlayoutSnack;
    @InjectView(R.id.linearlayout_food)
    LinearLayout linearlayoutFood;
    @InjectView(R.id.linearlayout_setmeal)
    LinearLayout linearlayoutSetmeal;
    @InjectView(R.id.relative_food_left)
    RelativeLayout relativeFoodLeft;
    @InjectView(R.id.linearlayout_recommend)
    LinearLayout linearlayoutRecommend;
    private SortedMap<Integer, List<MenuItemInfo>> sortedMap;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private FoodRightFragment mFoodRightFragment;
    private LinearLayout[] mLinearLayoutArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.food_left_fragment, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentManager = getFragmentManager();
        sortedMap = (SortedMap) ((ApplicationEx) getActivity().getApplication()).receiveInternalActivityParam("allMenuList");
        linearlayoutDrink.setOnClickListener(mOnclickListener);
        linearlayoutSnack.setOnClickListener(mOnclickListener);
        linearlayoutFood.setOnClickListener(mOnclickListener);
        linearlayoutSetmeal.setOnClickListener(mOnclickListener);
        linearlayoutRecommend.setOnClickListener(mOnclickListener);
        linearlayoutRecommend.setSelected(true);

        TranslateAnimation transAnim = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, -1.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
        transAnim.setDuration(400);

        // 去掉弹回动画
       /* AddToShopCartAnimation customAnimation = new AddToShopCartAnimation();
        customAnimation.setDuration(400);
        customAnimation.setStartOffset(400);*/

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(transAnim);
//        set.addAnimation(customAnimation);
//        set.setInterpolator(new OvershootInterpolator());
        LayoutAnimationController lac = new LayoutAnimationController(set, 0.5f);
        relativeFoodLeft.setLayoutAnimation(lac);
        //需和TypeEnum的顺序保持一致
        mLinearLayoutArray = new LinearLayout[]{null, linearlayoutDrink,linearlayoutSnack, linearlayoutFood, linearlayoutSetmeal,linearlayoutRecommend,};
    }

    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTransaction = mFragmentManager.beginTransaction();
            ArrayList<MenuItemInfo> dataList = new ArrayList<>();
            if (v == linearlayoutDrink) {
                dataList.clear();
                List<MenuItemInfo> collection = sortedMap.get(MenuTypeEnum.DRINK.ordinal());
                dataList.addAll(collection);
                setSelectType(MenuTypeEnum.DRINK.ordinal());

            } else if (v == linearlayoutSnack) {
                dataList.clear();
                List<MenuItemInfo> collection = sortedMap.get(MenuTypeEnum.SNACK.ordinal());
                dataList.addAll(collection);
                setSelectType(MenuTypeEnum.SNACK.ordinal());
            } else if (v == linearlayoutFood) {
                dataList.clear();
                List<MenuItemInfo> collection = sortedMap.get(MenuTypeEnum.PRI_FOOD.ordinal());
                dataList.addAll(collection);
                setSelectType(MenuTypeEnum.PRI_FOOD.ordinal());
            } else if (v == linearlayoutSetmeal) {
                dataList.clear();
                List<MenuItemInfo> collection = sortedMap.get(MenuTypeEnum.MEALSET.ordinal());
                dataList.addAll(collection);
                setSelectType(MenuTypeEnum.MEALSET.ordinal());
            }
            else if (v == linearlayoutRecommend) {
                dataList.clear();
                List<MenuItemInfo> collection = sortedMap.get(MenuTypeEnum.RECOMMEND.ordinal());
                dataList.addAll(collection);
                setSelectType(MenuTypeEnum.RECOMMEND.ordinal());
            }
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("dataList", dataList);
            mFoodRightFragment = new FoodRightFragment();
            mFoodRightFragment.setArguments(bundle);
            mTransaction.replace(R.id.right_frag, mFoodRightFragment, "food_right_frag");
            mTransaction.commit();

        }
    };

    /**
     * 设置选择的菜品类别
     * @param whichType
     */
    private void setSelectType(int whichType) {
        for (int i = 1; i < mLinearLayoutArray.length; i++) {
            if (whichType == i) {
                mLinearLayoutArray[i].setSelected(true);
            }else {
                mLinearLayoutArray[i].setSelected(false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FoodLeftFragment"); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FoodLeftFragment");
    }
}
