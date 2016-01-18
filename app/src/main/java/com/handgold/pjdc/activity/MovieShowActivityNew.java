package com.handgold.pjdc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.MovieTypeEnum;
import com.handgold.pjdc.entitiy.MovieInfo;
import com.handgold.pjdc.ui.Movie.MovieFragment;
import com.handgold.pjdc.ui.widget.HeadView;
import com.handgold.pjdc.util.DeviceUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.*;

/**
 * Created by Administrator on 2015/11/3.
 */
public class MovieShowActivityNew extends FragmentActivity {

    private HeadView headView;

    private SortedMap<Integer, List<MovieInfo>> sortedMovieDataMap;



    private RelativeLayout rootView = null;

    private ViewPager mViewPager = null;

    private MyFragmentpagerAdapter mStatePagerAdapter = null;

    private LinearLayout tabLayout = null;

    private TextView tabItem1, tabItem2, tabItem3, tabItem4, tabItem5;

    private LinearLayout dotLayout = null;

    private ImageView callServiceImg= null;

    private List<Fragment> fragmentList;

    /**
     * 每个页面显示的item的个数
     */
    public static final int MAX_ITE_IN_PER_FRAGEMNT = 8;

    private TextView[] textArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_moive_game);

        // 初始化界面
        initView();


        // 获取游戏数据
        sortedMovieDataMap = (SortedMap) ((ApplicationEx) getApplication()).receiveInternalActivityParam("allMovieList");
        if (sortedMovieDataMap == null) {
            sortedMovieDataMap = new TreeMap<Integer, List<MovieInfo>>();
            initData();
        }

        initFragmentAndDot(MovieTypeEnum.HOT.ordinal());
    }

    private void initView() {
        headView = new HeadView(this);
        headView.h_left_tv.setText("返回");
        headView.h_title.setVisibility(View.GONE);
        headView.h_title_img.setVisibility(View.VISIBLE);
        headView.h_title_img.setBackgroundResource(R.drawable.newest_moive);
        headView.h_right_tv_llyt.setVisibility(View.VISIBLE);
        headView.h_right_tv.setText("当前位置：68桌");
        headView.h_right_tv.setTextColor(0x33ffffff);
        headView.h_left.setOnClickListener(mOnClickListener);
        headView.h_left_rlyt.setOnClickListener(mOnClickListener);
//        headView.h_right_tv.setOnClickListener(mOnClickListener);

        //换背景图片
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        rootView.setBackgroundResource(R.drawable.moive_bg);

        //viewpager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (LinearLayout) findViewById(R.id.content_title_ll);

        tabItem1 = (TextView) tabLayout.findViewById(R.id.content_title_item1);
        tabItem2 = (TextView) tabLayout.findViewById(R.id.content_title_item2);
        tabItem3 = (TextView) tabLayout.findViewById(R.id.content_title_item3);
        tabItem4 = (TextView) tabLayout.findViewById(R.id.content_title_item4);
        tabItem5 = (TextView) tabLayout.findViewById(R.id.content_title_item5);

        tabItem1.setText(getString(R.string.hot_moive));
        tabItem2.setText(getString(R.string.love_moive));
        tabItem3.setText(getString(R.string.comedy_moive));
        tabItem4.setText(getString(R.string.cartoon_movie));
        tabItem5.setText(getString(R.string.fiction_movie));

        textArray = new TextView[]{null, tabItem1, tabItem2, tabItem3, tabItem4, tabItem5};

        tabItem1.setSelected(true);

        tabItem1.setOnClickListener(mOnClickListener);
        tabItem2.setOnClickListener(mOnClickListener);
        tabItem3.setOnClickListener(mOnClickListener);
        tabItem4.setOnClickListener(mOnClickListener);
        tabItem5.setOnClickListener(mOnClickListener);

        dotLayout = (LinearLayout) findViewById(R.id.dot_ll);

        callServiceImg = (ImageView) findViewById(R.id.image_call_service);
        callServiceImg.setOnClickListener(mOnClickListener);
    }



    private void initData() {
        List<MovieInfo> movieInfoList = new ArrayList<MovieInfo>();
        for (int i = 0; i < 60; i++) {
            MovieInfo movieInfo = new MovieInfo("电影" + (i+1), "http://www.dddd.com", (i / 12 + 1));
            movieInfoList.add(movieInfo);

        }
        /**
         * 比较器：给menu按照type排序用
         */
        Comparator<MovieInfo> comparator = new Comparator<MovieInfo>() {
            @Override
            public int compare(MovieInfo lhs, MovieInfo rhs) {

                return lhs.getType() - rhs.getType();
            }
        };
        Collections.sort(movieInfoList, comparator);

        List<MovieInfo> tmpList = new ArrayList<MovieInfo>();

        int oldKey = movieInfoList.get(0).getType();

        for (int i = 0; i < movieInfoList.size(); i++) {
            MovieInfo movieItemData = movieInfoList.get(i);
            int newKey = movieItemData.getType();
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

    private void initFragmentAndDot(int type) {
        //构造前，先清除原来的圆点
        dotLayout.removeAllViews();

        fragmentList = new ArrayList<>();
        ArrayList<MovieInfo> dataList = new ArrayList<MovieInfo>();
        //初始化右边fragment的数据
        dataList.addAll(sortedMovieDataMap.get(type));
        int count = dataList.size() / MAX_ITE_IN_PER_FRAGEMNT;
        if (dataList.size() % MAX_ITE_IN_PER_FRAGEMNT != 0) {
            count++;
        }
        for (int i = 0; i < count; i++) {
            Bundle bundle = new Bundle();
            ArrayList<MovieInfo> subDataList = new ArrayList<MovieInfo>();
            for (int j = 0, size = dataList.size();j < MAX_ITE_IN_PER_FRAGEMNT && (j + i * MAX_ITE_IN_PER_FRAGEMNT < size); j++) {
                MovieInfo movieInfo = dataList.get(j + i * MAX_ITE_IN_PER_FRAGEMNT);
                subDataList.add(movieInfo);
            }
            bundle.putParcelableArrayList("dataList", subDataList);
            MovieFragment movieFragment = new MovieFragment();
            movieFragment.setArguments(bundle);
            fragmentList.add(movieFragment);

            //构造圆点
            ImageView dot = new ImageView(this);
            dot.setImageResource(R.drawable.dot_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            params.rightMargin = DeviceUtils.dip2px(this, 15);
            dotLayout.addView(dot, params);
        }
        dotLayout.getChildAt(0).setSelected(true);

        mStatePagerAdapter = new MyFragmentpagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mStatePagerAdapter);
        mStatePagerAdapter.notifyDataSetChanged();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int pos) {
                for (int i = 0; i < dotLayout.getChildCount(); i++) {
                    if (pos == i) {
                        dotLayout.getChildAt(i).setSelected(true);
                    }else {
                        dotLayout.getChildAt(i).setSelected(false);
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.head_left:
                case R.id.head_left_rlyt:
                    finish();
                    break;
                case R.id.content_title_item1:
                    initFragmentAndDot(MovieTypeEnum.HOT.ordinal());
                    setSelectType(MovieTypeEnum.HOT.ordinal());
                    break;
                case R.id.content_title_item2:
                    initFragmentAndDot(MovieTypeEnum.LOVE.ordinal());
                    setSelectType(MovieTypeEnum.LOVE.ordinal());
                    break;
                case R.id.content_title_item3:
                    initFragmentAndDot(MovieTypeEnum.COMEDY.ordinal());
                    setSelectType(MovieTypeEnum.COMEDY.ordinal());
                    break;
                case R.id.content_title_item4:
                    initFragmentAndDot(MovieTypeEnum.CARTOON.ordinal());
                    setSelectType(MovieTypeEnum.CARTOON.ordinal());
                    break;
                case R.id.content_title_item5:
                    initFragmentAndDot(MovieTypeEnum.SCIENCE_FICTION.ordinal());
                    setSelectType(MovieTypeEnum.SCIENCE_FICTION.ordinal());
                    break;
                case R.id.image_call_service:
                    Toast.makeText(MovieShowActivityNew.this, "服务员正赶过来，请稍后。", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 切换tab选中状态
     * @param whichType
     */
    private void setSelectType(int whichType) {
        for (int i = 1; i < textArray.length; i++) {
            if (whichType == i) {
                textArray[i].setSelected(true);
            }else {
                textArray[i].setSelected(false);
            }
        }
    }

    /**
     * viewpager的adapter
     */
    private class MyFragmentpagerAdapter extends FragmentStatePagerAdapter{

        public MyFragmentpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
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
