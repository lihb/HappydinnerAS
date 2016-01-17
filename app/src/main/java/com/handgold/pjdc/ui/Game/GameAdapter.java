package com.handgold.pjdc.ui.Game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.entitiy.GameInfo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2015/10/29.
 */
public class GameAdapter extends BaseAdapter {

    private ArrayList<GameInfo> mGameList = new ArrayList<GameInfo>();
    private Context mContext;
    private LayoutInflater mInflater = null;


    public GameAdapter(ArrayList<GameInfo> mGameList, Context mContext) {
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
            convertView = mInflater.inflate(R.layout.game_item_detail_new, null);
            viewholder = new ViewHolder();
            viewholder.itemImg = (ImageView) convertView.findViewById(R.id.item_pic_iv);
            viewholder.itemName = (TextView) convertView.findViewById(R.id.item_name_text);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        final GameInfo gameInfo = (GameInfo) getItem(position);

        int[] picIds = new int[]{
                R.drawable.pic_game_baoweiluobo, R.drawable.pic_game_buyudaren,
                R.drawable.pic_game_ditiepaoku, R.drawable.pic_game_doudizhu,
                R.drawable.pic_game_kaixinxiaoxiaole, R.drawable.pic_game_majiang,
                R.drawable.pic_game_shenmiaotaowang,R.drawable.pic_game_shuiguorenzhe,};
        int id = new Random().nextInt(picIds.length);
        viewholder.itemImg.setImageResource(picIds[id]);

        if (position == 0) {
            viewholder.itemName.setText("保卫萝卜");
        }
        else if (position == 1) {
            viewholder.itemName.setText("开心消消乐");
        }
        else if (position == 2) {
            viewholder.itemName.setText("五子棋大师");
        }
        else {
            viewholder.itemName.setText(gameInfo.getName());
        }
        return convertView;
    }


    private class ViewHolder {
        ImageView itemImg;
        TextView itemName;

    }

}
