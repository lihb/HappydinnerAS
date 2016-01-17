package com.handgold.pjdc.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.action.ApiManager;
import com.handgold.pjdc.action.ServiceGenerator;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.BaseActivity;
import com.handgold.pjdc.entitiy.CoverFlowEntity;
import com.handgold.pjdc.entitiy.GameInfo;
import com.handgold.pjdc.entitiy.MenuItemInfo;
import com.handgold.pjdc.entitiy.MenuListEntity;
import com.handgold.pjdc.entitiy.MovieInfo;
import com.handgold.pjdc.ui.CoverFlowAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.*;


public class CoverFlowActivity extends BaseActivity {

    private FeatureCoverFlow mCoverFlow;
//    private CoverFlowAdapterNew mAdapter;
    private CoverFlowAdapter mAdapter;
//    private HashMap<String, ArrayList> mData = new HashMap<>();
    private ArrayList<CoverFlowEntity> mData = new ArrayList<CoverFlowEntity>(0);
    private SortedMap<Integer, List<MenuItemInfo>> sortedMenuMap;
    private SortedMap<Integer, List<GameInfo>> sortedGameMap;
    private SortedMap<Integer, List<MovieInfo>> sortedMovieMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coverflow);

        mData.add(new CoverFlowEntity(R.drawable.cover_location, R.string.title2));
        mData.add(new CoverFlowEntity(R.drawable.cover_game, R.string.title4));
        mData.add(new CoverFlowEntity(R.drawable.cover_menu, R.string.title1));
        mData.add(new CoverFlowEntity(R.drawable.cover_movie, R.string.title3));

//        mAdapter = new CoverFlowAdapterNew(this);
        mAdapter = new CoverFlowAdapter(this);
        mAdapter.setData(mData);
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
        mCoverFlow.setAdapter(mAdapter);

        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    Intent intent = new Intent(CoverFlowActivity.this, com.handgold.pjdc.activity.GameShowActivityNew.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(CoverFlowActivity.this, com.handgold.pjdc.activity.MovieShowActivityNew.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(CoverFlowActivity.this, com.handgold.pjdc.activity.FoodShowActivity.class);
                    startActivity(intent);
                }else if (position == 0) {
                    Intent intent = new Intent(CoverFlowActivity.this, com.handgold.pjdc.activity.MapShowActivity.class);
                    startActivity(intent);
                }

            }
        });

        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
//                mTitle.setText(getResources().getString(mData.get(position).titleResId));
            }

            @Override
            public void onScrolling() {
//                mTitle.setText("");
            }
        });

        // 获取菜品数据
        sortedMenuMap = (SortedMap) ((ApplicationEx) getApplication()).receiveInternalActivityParam("allMenuList");
        if (sortedMenuMap == null) {
            sortedMenuMap = new TreeMap<Integer, List<MenuItemInfo>>();
            initMenuData();
        }
        Configuration config = getResources().getConfiguration();
        int smallestScreenWidth = config.smallestScreenWidthDp;
        int screenWidthDp = config.screenWidthDp;
        Log.i("smallest width = " ,""+ smallestScreenWidth);
        Log.i("screenWidthDp width = " ,""+ screenWidthDp);
    }

    private void initMenuData() {

//
        ServiceGenerator.createService(ApiManager.class)
                .getMenuList("2222")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MenuListEntity>() {
                    @Override
                    public void onCompleted() {
                        Log.i("lihb test", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("lihb test", "onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MenuListEntity menuListEntity) {
                        Log.i("lihb test", "onNext");
                        if(menuListEntity.result_code == 0) {
                            Log.i("lihb test", "onNext in if()");

                            for (int i = 0; i < menuListEntity.menulist.size(); i++) {
                                ApiManager.MenuType menuType = menuListEntity.menulist.get(i);
                                Log.i("lihb test", menuType.typename);
                                for (int j = 0; j < menuType.items.size(); j++) {
                                    MenuItemInfo info = menuType.items.get(j);
                                    Log.i("lihb test", info.getName());
                                }
                            }


                        }
                    }
                });

        List<MenuItemInfo> menuList = new ArrayList<MenuItemInfo>();
        for (int i = 0; i < 30; i++) {
            MenuItemInfo menu = new MenuItemInfo("菜品" + (i+1), null, null, 15.0f + i, "菜品简介" + (i+1), 1, 0, (i / 6) + 1, "大丰收");
            menuList.add(menu);

        }

        /**
         * 比较器：给menu按照type排序用
         */
        Comparator<MenuItemInfo> comparator = new Comparator<MenuItemInfo>() {
            @Override
            public int compare(MenuItemInfo lhs, MenuItemInfo rhs) {

                return lhs.getType() - rhs.getType();
            }
        };
        Collections.sort(menuList, comparator);

        List<MenuItemInfo> tmpList = new ArrayList<MenuItemInfo>();

        int oldKey = menuList.get(0).getType();

        for (int i = 0; i < menuList.size(); i++) {
            MenuItemInfo menuItemData = menuList.get(i);
            int newKey = menuItemData.getType();
            if (newKey == oldKey) {
                tmpList.add(menuItemData);
            } else {
                sortedMenuMap.put(oldKey, tmpList);
                tmpList = new ArrayList<MenuItemInfo>();
                tmpList.add(menuItemData);
                oldKey = newKey;
            }
        }
        sortedMenuMap.put(oldKey, tmpList); // 处理最后一组数据

        ((ApplicationEx) getApplication()).setInternalActivityParam("allMenuList", sortedMenuMap);
    }

    private void initGameData() {
        List<GameInfo> gameInfoList = new ArrayList<GameInfo>();
        for (int i = 0; i < 30; i++) {
            GameInfo gameInfo = new GameInfo("游戏" + (i+1), "http://www.dddd.com", (i / 6 + 1));
            gameInfoList.add(gameInfo);

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
        Collections.sort(gameInfoList, comparator);

        List<GameInfo> tmpList = new ArrayList<GameInfo>();

        int oldKey = gameInfoList.get(0).getType();

        for (int i = 0; i < gameInfoList.size(); i++) {
            GameInfo gameItemData = gameInfoList.get(i);
            int newKey = gameItemData.getType();
            if (newKey == oldKey) {
                tmpList.add(gameItemData);
            } else {
                sortedGameMap.put(oldKey, tmpList);
                tmpList = new ArrayList<GameInfo>();
                tmpList.add(gameItemData);
                oldKey = newKey;
            }
        }
        sortedGameMap.put(oldKey, tmpList); // 处理最后一组数据

        ((ApplicationEx) getApplication()).setInternalActivityParam("allGameList", sortedGameMap);
    }


    private void initMovieData() {
        List<MovieInfo> movieInfoList = new ArrayList<MovieInfo>();
        for (int i = 0; i < 30; i++) {
            MovieInfo movieInfo = new MovieInfo("电影" + (i+1), "http://www.dddd.com", (i / 6 + 1));
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
                sortedMovieMap.put(oldKey, tmpList);
                tmpList = new ArrayList<MovieInfo>();
                tmpList.add(movieItemData);
                oldKey = newKey;
            }
        }
        sortedMovieMap.put(oldKey, tmpList); // 处理最后一组数据

        ((ApplicationEx) getApplication()).setInternalActivityParam("allMovieList", sortedMovieMap);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁首页时，清除全局变量
        ((ApplicationEx) getApplication()).clearInternalActivityParam();
    }
}
