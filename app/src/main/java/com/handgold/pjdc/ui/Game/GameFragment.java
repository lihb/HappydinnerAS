package com.handgold.pjdc.ui.Game;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.entitiy.GameInfo;
import com.handgold.pjdc.util.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 类说明：
 *
 * @author Administrator
 * @version 1.0
 * @date 2015/6/16
 */

public class GameFragment extends Fragment {

    private GridView mGridView;

    private ArrayList<GameInfo> mDataList;

    private GameAdapter mAdapter;
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    startGameApp("com.carrot.iceworld", getActivity());
                    break;
                case 1:
                    startGameApp("com.happyelements.AndroidAnimal", getActivity());
                    break;
                case 2:
                    startGameApp("com.cnvcs.gomoku", getActivity());
                    break;
                default:
                    CommonUtils.toastText(getActivity(), "您没有安装该游戏！");
            }

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_game_fragment, null);
        mGridView = (GridView) view.findViewById(R.id.movie_game_gv);
        mGridView.setOnItemClickListener(mOnItemClickListener);
//        mGridView.setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.horizontal_spacing));
//        mGridView.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        mDataList = bundle.getParcelableArrayList("dataList");
        // 配置listworker
        if (mAdapter == null) {
            mAdapter = new GameAdapter(mDataList, getActivity());
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(mDataList);
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 打开游戏app
     */
    private void startGameApp(String packageName, Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> list = packageManager.getInstalledApplications(0);
        for (ApplicationInfo info : list) {
            Log.i("wwwww-----------", info.toString());
        }
        PackageInfo pi = null;

        try {
            pi = packageManager.getPackageInfo(packageName, 0);

            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pi.packageName);

            List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                ComponentName cn = new ComponentName(packageName, className);

                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            CommonUtils.toastText(getActivity(), "您没有安装该游戏！");
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("GameRightFragment"); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("GameRightFragment");
    }

}
