package com.handgold.pjdc.ui.Movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handgold.pjdc.R;
import com.handgold.pjdc.entitiy.MovieInfo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2015/10/29.
 */
public class MovieAdapter extends BaseAdapter {

    private ArrayList<MovieInfo> mDataList = new ArrayList<MovieInfo>();
    private Context mContext;
    private LayoutInflater mInflater = null;


    public MovieAdapter(ArrayList<MovieInfo> dataList, Context mContext) {
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
            convertView = mInflater.inflate(R.layout.movie_item_detail, null);
            viewholder = new ViewHolder();
            viewholder.itemImg = (ImageView) convertView.findViewById(R.id.item_pic_iv);
            viewholder.itemName = (TextView) convertView.findViewById(R.id.item_name_text);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        final MovieInfo movieInfo = (MovieInfo) getItem(position);


        Glide.with(mContext)
                .load(movieInfo.imgUrl)
                .placeholder(R.drawable.fijitribe)
                .into(viewholder.itemImg);

        viewholder.itemName.setText(movieInfo.getName());
        return convertView;
    }


    private class ViewHolder {
        ImageView itemImg;
        TextView itemName;
    }

}
