package com.handgold.pjdc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.GameTypeEnum;
import com.handgold.pjdc.base.MovieTypeEnum;
import com.handgold.pjdc.entitiy.GameInfo;
import com.handgold.pjdc.ui.Game.GameFragment;
import com.handgold.pjdc.ui.widget.HeadView;
import com.handgold.pjdc.ui.widget.PopupGameVideoView;
import com.handgold.pjdc.util.DeviceUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.*;

/**
 * Created by Administrator on 2015/11/3.
 */
public class GameShowActivityNew extends FragmentActivity {

    private HeadView headView;

    private SortedMap<Integer, List<GameInfo>> sortedGameDataMap;

    private RelativeLayout mPopupGameVideoRelativeLayout;

    private PopupGameVideoView mPopupGameVideoView;

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

    private PopupGameVideoView.OnPopGameVideoViewListener mListener = new PopupGameVideoView.OnPopGameVideoViewListener(){

        @Override
        public void onCloseBtnEvent(Map<String, String> map_value, int curPos) {
            MobclickAgent.onEventValue(GameShowActivityNew.this, "game_video_close_event", map_value, curPos);
            Log.i("GameShowActivity", "onCloseBtnEvent");
        }

        @Override
        public void onVideoCompletion(Map<String, String> map) {
            MobclickAgent.onEvent(GameShowActivityNew.this, "game_video_completion_event", map);
            Log.i("GameShowActivity", "onVideoCompletion");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_moive_game);

        // 初始化界面
        initView();


        // 获取游戏数据
        sortedGameDataMap = (SortedMap) ((ApplicationEx) getApplication()).receiveInternalActivityParam("allGameList");
        if (sortedGameDataMap == null) {
            sortedGameDataMap = new TreeMap<Integer, List<GameInfo>>();
            initData();
        }

        initFragmentAndDot(MovieTypeEnum.HOT.ordinal());


    }

    private void initView() {
        headView = new HeadView(this);
        headView.h_left_tv.setText("返回");
        headView.h_title.setVisibility(View.GONE);
        headView.h_title_img.setVisibility(View.VISIBLE);
        headView.h_title_img.setBackgroundResource(R.drawable.top_game);
        headView.h_right_tv_llyt.setVisibility(View.VISIBLE);
        headView.h_right_tv.setText("当前位置：68桌");
        headView.h_right_tv.setTextColor(0x33ffffff);
        headView.h_left.setOnClickListener(mOnClickListener);
        headView.h_left_rlyt.setOnClickListener(mOnClickListener);
//        headView.h_right_tv.setOnClickListener(mOnClickListener);

        //换背景图片
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        rootView.setBackgroundResource(R.drawable.game_bg);

        //viewpager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (LinearLayout) findViewById(R.id.content_title_ll);

        tabItem1 = (TextView) tabLayout.findViewById(R.id.content_title_item1);
        tabItem2 = (TextView) tabLayout.findViewById(R.id.content_title_item2);
        tabItem3 = (TextView) tabLayout.findViewById(R.id.content_title_item3);
        tabItem4 = (TextView) tabLayout.findViewById(R.id.content_title_item4);
        tabItem5 = (TextView) tabLayout.findViewById(R.id.content_title_item5);

        tabItem1.setText(getString(R.string.recomend_game));
        tabItem2.setText(getString(R.string.relax_game));
        tabItem3.setText(getString(R.string.card_game));
        tabItem4.setText(getString(R.string.shoot_game));
        tabItem5.setText(getString(R.string.puzzle_game));

        textArray = new TextView[]{null, tabItem1, tabItem2, tabItem3, tabItem4, tabItem5};
        for (int i = 1; i < textArray.length; i++) {
            textArray[i].setBackgroundResource(R.drawable.game_tab_selector);
        }

        tabItem1.setSelected(true);

        tabItem1.setOnClickListener(mOnClickListener);
        tabItem2.setOnClickListener(mOnClickListener);
        tabItem3.setOnClickListener(mOnClickListener);
        tabItem4.setOnClickListener(mOnClickListener);
        tabItem5.setOnClickListener(mOnClickListener);

        dotLayout = (LinearLayout) findViewById(R.id.dot_ll);

        callServiceImg = (ImageView) findViewById(R.id.image_call_service);
        callServiceImg.setImageResource(R.drawable.game_call_service_selector);
        callServiceImg.setOnClickListener(mOnClickListener);

        // 广告视频(暂时屏蔽 2015-12-12)
//        mPopupGameVideoRelativeLayout = (RelativeLayout) findViewById(R.id.popup_game_video_relativeLayout);
//        mPopupGameVideoRelativeLayout.setOnTouchListener(mOnTouchListener);
//
//        mPopupGameVideoView = (PopupGameVideoView) findViewById(R.id.popup_game_video_view);
//        int width = DeviceUtils.getScreenWidth(this) / 2;
//        int height = DeviceUtils.getScreenHeight(this) / 2;
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        mPopupGameVideoView.setLayoutParams(params);
//        mPopupGameVideoView.setOnPopGameVideoViewListener(mListener);
//        mPopupGameVideoRelativeLayout.setVisibility(View.VISIBLE);
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // 屏蔽在该页面之下的页面的点击事件
            return true;
        }
    };

    private void initData() {
        List<GameInfo> movieInfoList = new ArrayList<GameInfo>();
        for (int i = 0; i < 60; i++) {
            GameInfo gameInfo = new GameInfo("游戏" + (i+1), "http://www.dddd.com", (i / 12 + 1));
            movieInfoList.add(gameInfo);

        }
        /**
         * 比较器：给menu按照type排序用
         */
        Comparator<GameInfo> comparator = new Comparator<GameInfo>() {
            @Override
            public int compare(GameInfo lhs, GameInfo rhs) {

                return lhs.getType() - rhs.getType();
            }
        };
        Collections.sort(movieInfoList, comparator);

        List<GameInfo> tmpList = new ArrayList<GameInfo>();

        int oldKey = movieInfoList.get(0).getType();

        for (int i = 0; i < movieInfoList.size(); i++) {
            GameInfo movieItemData = movieInfoList.get(i);
            int newKey = movieItemData.getType();
            if (newKey == oldKey) {
                tmpList.add(movieItemData);
            } else {
                sortedGameDataMap.put(oldKey, tmpList);
                tmpList = new ArrayList<GameInfo>();
                tmpList.add(movieItemData);
                oldKey = newKey;
            }
        }
        sortedGameDataMap.put(oldKey, tmpList); // 处理最后一组数据

        ((ApplicationEx) getApplication()).setInternalActivityParam("allGameList", sortedGameDataMap);
    }

    private void initFragmentAndDot(int type) {
        //构造前，先清除原来的圆点
        dotLayout.removeAllViews();

        fragmentList = new ArrayList<>();
        ArrayList<GameInfo> dataList = new ArrayList<GameInfo>();
        //初始化右边fragment的数据
        dataList.addAll(sortedGameDataMap.get(type));
        int count = dataList.size() / MAX_ITE_IN_PER_FRAGEMNT;
        if (dataList.size() % MAX_ITE_IN_PER_FRAGEMNT != 0) {
            count++;
        }
        for (int i = 0; i < count; i++) {
            Bundle bundle = new Bundle();
            ArrayList<GameInfo> subDataList = new ArrayList<GameInfo>();
            for (int j = 0, size = dataList.size();j < MAX_ITE_IN_PER_FRAGEMNT && (j + i * MAX_ITE_IN_PER_FRAGEMNT < size); j++) {
                GameInfo gameInfo = dataList.get(j + i * MAX_ITE_IN_PER_FRAGEMNT);
                subDataList.add(gameInfo);
            }
            bundle.putParcelableArrayList("dataList", subDataList);
            GameFragment gameFragment = new GameFragment();
            gameFragment.setArguments(bundle);
            fragmentList.add(gameFragment);

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
                    initFragmentAndDot(GameTypeEnum.RECOMMEND.ordinal());
                    setSelectType(GameTypeEnum.RECOMMEND.ordinal());
                    break;
                case R.id.content_title_item2:
                    initFragmentAndDot(GameTypeEnum.RELAX.ordinal());
                    setSelectType(GameTypeEnum.RELAX.ordinal());
                    break;
                case R.id.content_title_item3:
                    initFragmentAndDot(GameTypeEnum.CARD.ordinal());
                    setSelectType(GameTypeEnum.CARD.ordinal());
                    break;
                case R.id.content_title_item4:
                    initFragmentAndDot(GameTypeEnum.SHOOT.ordinal());
                    setSelectType(GameTypeEnum.SHOOT.ordinal());
                    break;
                case R.id.content_title_item5:
                    initFragmentAndDot(GameTypeEnum.PUZZLE.ordinal());
                    setSelectType(GameTypeEnum.PUZZLE.ordinal());
                    break;
                case R.id.image_call_service:
                    Toast.makeText(GameShowActivityNew.this, "服务员正赶过来，请稍后。", Toast.LENGTH_SHORT).show();
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
