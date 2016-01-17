package com.handgold.pjdc.ui.Movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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

        int[] picIds = new int[]{
                R.drawable.pic_moive_dashengguilai, R.drawable.pic_moive_diezhongdie5,
                R.drawable.pic_moive_gangjiong, R.drawable.pic_moive_gundanba_zhongniujun,
                R.drawable.pic_moive_jianbingxia, R.drawable.pic_moive_padingdunxiong,
                R.drawable.pic_moive_suduyujiqing7, R.drawable.pic_moive_toulaotegongdui,
                R.drawable.pic_moive_xialuote, R.drawable.pic_moive_xiaowangzi,
                R.drawable.pic_moive_xinniangzuozhan, R.drawable.pic_moive_zhuoyaoji};
        int id = new Random().nextInt(picIds.length);
        viewholder.itemImg.setImageResource(picIds[id]);
        viewholder.itemName.setText(movieInfo.getName());
        return convertView;
    }


    private class ViewHolder {
        ImageView itemImg;
        TextView itemName;
    }

}
