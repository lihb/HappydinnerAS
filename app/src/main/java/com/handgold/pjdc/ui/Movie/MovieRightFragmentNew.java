package com.handgold.pjdc.ui.Movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.activity.VideoPlayer2Activity;
import com.handgold.pjdc.entitiy.MovieInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 类说明：
 *
 * @author Administrator
 * @version 1.0
 * @date 2015/6/16
 */

public class MovieRightFragmentNew extends Fragment {

    private GridView mGridView;

    private ArrayList<MovieInfo> mDataList;

    private MovieRightAdapter mAdapter;
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            MovieInfo movie = (MovieInfo) parent.getAdapter().getItem(position);
            Intent intent = new Intent(getActivity(), VideoPlayer2Activity.class);
            movie.setMovieUrl("http://download.cloud.189.cn/v5/downloadFile.action?downloadRequest=1_266BEB5F2F53474145C6EBE33E9A75D592251F2581CFE66ED934BC80674F070BA6790DA91C37DD2867779B6A435B6E040ED7928D6EFEB456A463C8E6238E8DA431473E7443FCC8025B64223A6700BF64EDD9FFDFEEA7447A59FC024F4CE7979319CFCCF6F79641E0E10945F7D23B60F7557901BF94E0BF88DFACD44EF40A4A4D0E77B882");
            intent.putExtra("videoUrl", movie.getMovieUrl());
            intent.putExtra("fileName", movie.getName());
            getActivity().startActivity(intent);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.food_right_fragment, null);
        mGridView = (GridView) view.findViewById(R.id.food_right_lv);
        mGridView.setOnItemClickListener(mOnItemClickListener);
        setGridViewAnimation(mGridView);

        return view;
    }

    /**
     * 设置电影进入动画
     *
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
            mAdapter = new MovieRightAdapter(mDataList, getActivity());
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(mDataList);
            mAdapter.notifyDataSetChanged();
        }

    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MovieRightFragmentNew"); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MovieRightFragmentNew");
    }

}
