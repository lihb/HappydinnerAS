package com.handgold.pjdc.ui.Game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.entitiy.GameInfo;
import com.handgold.pjdc.ui.widget.RoundedImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2015/10/29.
 */
public class GameRightAdapter extends BaseAdapter {

    private ArrayList<GameInfo> mGameList = new ArrayList<GameInfo>();
    private Context mContext;
    private LayoutInflater mInflater = null;


    public GameRightAdapter(ArrayList<GameInfo> mGameList, Context mContext) {
        this.mGameList = mGameList;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setData(ArrayList<GameInfo> menuList) {
        mGameList = menuList;
    }

    @Override
    public int getCount() {
        return mGameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGameList.get(position);
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

        final GameInfo gameInfo = (GameInfo) getItem(position);

        int[] picIds = new int[]{R.drawable.meishi1, R.drawable.meishi2, R.drawable.meishi3, R.drawable.meishi4,
                R.drawable.meishi5, R.drawable.meishi6, R.drawable.meishi7};
        int id = new Random().nextInt(7);
        viewholder.gameImg.setImageResource(picIds[id]);
        if (position % 2 == 0) {
            viewholder.gameName.setBackgroundResource(R.drawable.home_game_name1);
        }else {
            viewholder.gameName.setBackgroundResource(R.drawable.home_game_name2);
        }
        if (position == 0) {
            viewholder.gameName.setText("保卫萝卜");
        }
        else if (position == 1) {
            viewholder.gameName.setText("开心消消乐");
        }
        else if (position == 2) {
            viewholder.gameName.setText("五子棋大师");
        }
        else {
            viewholder.gameName.setText(gameInfo.getName());
        }
        return convertView;
    }


    private class ViewHolder {
        RoundedImageView gameImg;
        TextView gameName;

    }

}
