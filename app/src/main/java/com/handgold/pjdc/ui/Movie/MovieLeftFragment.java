package com.handgold.pjdc.ui.Movie;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.MovieTypeEnum;
import com.handgold.pjdc.entitiy.MovieInfo;
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

public class MovieLeftFragment extends Fragment {

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

    @InjectView(R.id.image_drink)
    ImageView image_drink;
    @InjectView(R.id.text_drink)
    TextView text_drink;

    @InjectView(R.id.image_snack)
    ImageView image_snack;
    @InjectView(R.id.text_snack)
    TextView text_snack;

    @InjectView(R.id.image_food)
    ImageView image_food;
    @InjectView(R.id.text_food)
    TextView text_food;

    @InjectView(R.id.image_setmeal)
    ImageView image_setmeal;
    @InjectView(R.id.text_setmeal)
    TextView text_setmeal;

    @InjectView(R.id.image_recommend)
    ImageView image_recommend;
    @InjectView(R.id.text_recommend)
    TextView text_recommend;

    private SortedMap<Integer, List<MovieInfo>> sortedMovieMap;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private MovieRightFragmentNew movieRightFragment;
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
        text_drink.setText("恐怖电影");
        text_setmeal.setText("动作电影");
        text_snack.setText("喜剧电影");
        text_food.setText("科幻电影");
        text_recommend.setText("推荐电影");
        mFragmentManager = getFragmentManager();
        sortedMovieMap = (SortedMap) ((ApplicationEx) getActivity().getApplication()).receiveInternalActivityParam("allMovieList");
        linearlayoutDrink.setOnClickListener(mOnclickListener);
        linearlayoutSnack.setOnClickListener(mOnclickListener);
        linearlayoutFood.setOnClickListener(mOnclickListener);
        linearlayoutSetmeal.setOnClickListener(mOnclickListener);
        linearlayoutRecommend.setOnClickListener(mOnclickListener);
        linearlayoutRecommend.setSelected(true);

        TranslateAnimation transAnim = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, -1.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
        transAnim.setDuration(400);


        AnimationSet set = new AnimationSet(false);
        set.addAnimation(transAnim);
        LayoutAnimationController lac = new LayoutAnimationController(set, 0.5f);
        relativeFoodLeft.setLayoutAnimation(lac);
        //需和TypeEnum的顺序保持一致
        mLinearLayoutArray = new LinearLayout[]{null, linearlayoutRecommend, linearlayoutDrink,linearlayoutSnack, linearlayoutFood, linearlayoutSetmeal};
    }

    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTransaction = mFragmentManager.beginTransaction();
            ArrayList<MovieInfo> dataList = new ArrayList<>();
            if (v == linearlayoutDrink) {
                dataList.clear();
                List<MovieInfo> collection = sortedMovieMap.get(MovieTypeEnum.LOVE.ordinal());
                dataList.addAll(collection);
                setSelectType(MovieTypeEnum.LOVE.ordinal());

            } else if (v == linearlayoutSnack) {
                dataList.clear();
                List<MovieInfo> collection = sortedMovieMap.get(MovieTypeEnum.COMEDY.ordinal());
                dataList.addAll(collection);
                setSelectType(MovieTypeEnum.COMEDY.ordinal());
            } else if (v == linearlayoutFood) {
                dataList.clear();
                List<MovieInfo> collection = sortedMovieMap.get(MovieTypeEnum.CARTOON.ordinal());
                dataList.addAll(collection);
                setSelectType(MovieTypeEnum.CARTOON.ordinal());
            } else if (v == linearlayoutSetmeal) {
                dataList.clear();
                List<MovieInfo> collection = sortedMovieMap.get(MovieTypeEnum.SCIENCE_FICTION.ordinal());
                dataList.addAll(collection);
                setSelectType(MovieTypeEnum.SCIENCE_FICTION.ordinal());
            }
            else if (v == linearlayoutRecommend) {
                dataList.clear();
                List<MovieInfo> collection = sortedMovieMap.get(MovieTypeEnum.HOT.ordinal());
                dataList.addAll(collection);
                setSelectType(MovieTypeEnum.HOT.ordinal());
            }
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("dataList", dataList);
            movieRightFragment = new MovieRightFragmentNew();
            movieRightFragment.setArguments(bundle);
            mTransaction.replace(R.id.right_frag, movieRightFragment, "movie_right_frag");
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
        MobclickAgent.onPageStart("MovieLeftFragment"); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MovieLeftFragment");
    }
}
