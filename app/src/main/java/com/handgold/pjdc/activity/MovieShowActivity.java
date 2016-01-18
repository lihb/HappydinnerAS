package com.handgold.pjdc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.MovieTypeEnum;
import com.handgold.pjdc.entitiy.MovieInfo;
import com.handgold.pjdc.ui.Movie.MovieLeftFragment;
import com.handgold.pjdc.ui.Movie.MovieRightFragmentNew;
import com.handgold.pjdc.ui.widget.HeadView;
import com.umeng.analytics.MobclickAgent;

import java.util.*;

/**
 * Created by Administrator on 2015/11/3.
 */
public class MovieShowActivity extends FragmentActivity {

    private HeadView headView;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private MovieLeftFragment movieLeftFragment = null;

    private MovieRightFragmentNew movieRightFragment = null;

    private SortedMap<Integer, List<MovieInfo>> sortedMovieDataMap;

    private RelativeLayout rootView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_pay);

        mFragmentManager = getSupportFragmentManager();
        // 初始化界面
        initView();

        // 获取游戏数据
        sortedMovieDataMap = (SortedMap) ((ApplicationEx) getApplication()).receiveInternalActivityParam("allMovieList");
        if (sortedMovieDataMap == null) {
            sortedMovieDataMap = new TreeMap<Integer, List<MovieInfo>>();
            initData();
        }
        initFragment();


    }



    private void initView() {
        headView = new HeadView(this);
        headView.h_left_tv.setText("返回");
        headView.h_title.setVisibility(View.GONE);
        headView.h_title_img.setVisibility(View.VISIBLE);
        headView.h_right_tv_llyt.setVisibility(View.GONE);
        headView.h_right_tv.setText("");
        headView.h_right_tv.setTextColor(0x33ffffff);
        headView.h_left.setOnClickListener(mOnClickListener);
        headView.h_left_rlyt.setOnClickListener(mOnClickListener);
//        headView.h_right_tv.setOnClickListener(mOnClickListener);

        //换背景图片
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        rootView.setBackgroundResource(R.drawable.moive_bg);
    }

    private void initData() {


        List<MovieInfo> movieInfoList = new ArrayList<MovieInfo>();
        for (int i = 0; i < 30; i++) {
            MovieInfo movieInfo = new MovieInfo("电影" + (i + 1), "http://www.dddd.com", (i / 6 + 1), "");
            movieInfoList.add(movieInfo);

        }
        /**
         * 比较器：给menu按照type排序用
         */
        Comparator<MovieInfo> comparator = new Comparator<MovieInfo>() {
            @Override
            public int compare(MovieInfo lhs, MovieInfo rhs) {

                return lhs.getPlaytype() - rhs.getPlaytype();
            }
        };
        Collections.sort(movieInfoList, comparator);

        List<MovieInfo> tmpList = new ArrayList<MovieInfo>();

        int oldKey = movieInfoList.get(0).getPlaytype();

        for (int i = 0; i < movieInfoList.size(); i++) {
            MovieInfo movieItemData = movieInfoList.get(i);
            int newKey = movieItemData.getPlaytype();
            if (newKey == oldKey) {
                tmpList.add(movieItemData);
            } else {
                sortedMovieDataMap.put(oldKey, tmpList);
                tmpList = new ArrayList<MovieInfo>();
                tmpList.add(movieItemData);
                oldKey = newKey;
            }
        }
        sortedMovieDataMap.put(oldKey, tmpList); // 处理最后一组数据

        ((ApplicationEx) getApplication()).setInternalActivityParam("allMovieList", sortedMovieDataMap);
    }

    private void initFragment() {
        mTransaction = mFragmentManager.beginTransaction();
        if (movieLeftFragment == null) {
            movieLeftFragment = new MovieLeftFragment();
            mTransaction.replace(R.id.left_frag, movieLeftFragment, "movie_left_fragment");

        }

        if (movieRightFragment == null) {
            Bundle bundle = new Bundle();
            ArrayList<MovieInfo> dataList = new ArrayList<MovieInfo>();
            //初始化右边fragment的数据
            dataList.addAll(sortedMovieDataMap.get(MovieTypeEnum.HOT.ordinal()));
            bundle.putParcelableArrayList("dataList", dataList);
            movieRightFragment = new MovieRightFragmentNew();
            movieRightFragment.setArguments(bundle);
            mTransaction.replace(R.id.right_frag, movieRightFragment, "movie_right_fragment");
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
