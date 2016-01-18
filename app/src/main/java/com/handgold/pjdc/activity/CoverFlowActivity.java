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
import com.handgold.pjdc.entitiy.MenuType;
import com.handgold.pjdc.entitiy.MovieInfo;
import com.handgold.pjdc.entitiy.RoomTableInfo;
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
    private ArrayList<MenuType> allMenuList;
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
                } else if (position == 0) {
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
        allMenuList = (ArrayList) ((ApplicationEx) getApplication()).receiveInternalActivityParam("allMenuList");
        if (allMenuList == null) {
            allMenuList = new ArrayList<>();
            initMenuData();
        }
        Configuration config = getResources().getConfiguration();
        int smallestScreenWidth = config.smallestScreenWidthDp;
        int screenWidthDp = config.screenWidthDp;
        Log.i("smallest width = ", "" + smallestScreenWidth);
        Log.i("screenWidthDp width = ", "" + screenWidthDp);
    }

    private void initMenuData() {

//
        ServiceGenerator.createService(ApiManager.class)
                .getMenuList(((ApplicationEx) getApplication()).deviceid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MenuListEntity>() {
                    @Override
                    public void onCompleted() {
                        Log.i("lihb test getMenuList", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("lihb test getMenuList", "onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MenuListEntity menuListEntity) {
                        Log.i("lihb test getMenuList", "onNext");
                        if (menuListEntity.result_code == 0) {

                            ((ApplicationEx) getApplication()).setInternalActivityParam("allMenuList", menuListEntity.menulist);
                            allMenuList.addAll(menuListEntity.menulist);
                        }
                    }
                });
        ServiceGenerator.createService(ApiManager.class)
                .getOrderInfo(((ApplicationEx) getApplication()).deviceid)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<RoomTableInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.i("lihb test getOrderInfo", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("lihb test getOrderInfo", "onError");
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(RoomTableInfo roomTableInfo) {
                        Log.i("lihb test getOrderInfo", "onNext");
                        if (roomTableInfo.result_code == 0) {
                            Log.i("lihb test", "roomTableInfo.table_number = " + roomTableInfo.table_number);
                            ((ApplicationEx) getApplication()).setInternalActivityParam("table_number", roomTableInfo.table_number);

                        }

                    }
                });

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁首页时，清除全局变量
        ((ApplicationEx) getApplication()).clearInternalActivityParam();
    }
}
