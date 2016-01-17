package com.handgold.pjdc.ui.Movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.entitiy.MovieInfo;
import com.handgold.pjdc.ui.widget.RoundedImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2015/10/29.
 */
public class MovieRightAdapter extends BaseAdapter {

    private ArrayList<MovieInfo> mDataList = new ArrayList<MovieInfo>();
    private Context mContext;
    private LayoutInflater mInflater = null;


    public MovieRightAdapter(ArrayList<MovieInfo> dataList, Context mContext) {
        this.mDataList = dataList;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setData(ArrayList<MovieInfo> dataList) {
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.game_item_detail, null);
            viewholder = new ViewHolder();
            viewholder.gameImg = (RoundedImageView) convertView.findViewById(R.id.game_iv);
            viewholder.gameName = (TextView) convertView.findViewById(R.id.game_name_text);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        final MovieInfo movieInfo = (MovieInfo) getItem(position);

        int[] picIds = new int[]{R.drawable.meishi1, R.drawable.meishi2, R.drawable.meishi3, R.drawable.meishi4,
                R.drawable.meishi5, R.drawable.meishi6, R.drawable.meishi7};
        int id = new Random().nextInt(7);
        viewholder.gameImg.setImageResource(picIds[id]);
        viewholder.gameName.setBackgroundResource(R.drawable.movie_name1);
        viewholder.gameName.setText(movieInfo.getName());
        return convertView;
    }


    private class ViewHolder {
        RoundedImageView gameImg;
        TextView gameName;
    }

}
