package com.handgold.pjdc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handgold.pjdc.R;
import com.handgold.pjdc.action.ApiManager;
import com.handgold.pjdc.action.ServiceGenerator;
import com.handgold.pjdc.base.Constant;
import com.handgold.pjdc.base.DataManager;
import com.handgold.pjdc.base.MovieTypeEnum;
import com.handgold.pjdc.entitiy.MovieInfo;
import com.handgold.pjdc.entitiy.MovieListEntity;
import com.handgold.pjdc.ui.Movie.MovieFragment;
import com.handgold.pjdc.ui.widget.HeadView;
import com.handgold.pjdc.util.DeviceUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2015/11/3.
 */
public class MovieShowActivity extends FragmentActivity {

    private static final String TAG = "MovieShowActivity";
    private HeadView headView;

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

        if (DataManager.movieTypeList.isEmpty()) {
            initData();
        } else {
            initFragmentAndDot(MovieTypeEnum.HOT.ordinal());
        }

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

        textArray = new TextView[]{tabItem1, tabItem2, tabItem3, tabItem4, tabItem5};

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
        ServiceGenerator.createService(ApiManager.class)
                .getMovieList(Constant.deviceid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieListEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if(DataManager.movieTypeList.isEmpty()) {
                            Log.i(TAG, "没有电影数据");
                            Toast.makeText(MovieShowActivity.this, R.string.no_movie_data,Toast.LENGTH_SHORT).show();
                            DataManager.movieTypeList.clear();
                        }
                    }

                    @Override
                    public void onNext(MovieListEntity movieListEntity) {
                        DataManager.movieTypeList.addAll(movieListEntity.movielist);
                        initFragmentAndDot(MovieTypeEnum.HOT.ordinal());

                    }
                });
    }

    private void initFragmentAndDot(int type) {
        //构造前，先清除原来的圆点
        dotLayout.removeAllViews();
        if(DataManager.movieTypeList.isEmpty()) {
            return;
        }
        fragmentList = new ArrayList<>();
        ArrayList<MovieInfo> dataList = new ArrayList<MovieInfo>();
        //初始化fragment的数据
        dataList.addAll(DataManager.movieTypeList.get(type).items);
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
                    Toast.makeText(MovieShowActivity.this, "服务员正赶过来，请稍后。", Toast.LENGTH_SHORT).show();
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
        for (int i = 0; i < textArray.length; i++) {
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
