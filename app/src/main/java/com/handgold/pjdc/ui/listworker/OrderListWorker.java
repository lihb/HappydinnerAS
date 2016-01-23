package com.handgold.pjdc.ui.listworker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.common.list.AbstractListWorker;
import com.handgold.pjdc.entitiy.MenuItemInfo;
import com.handgold.pjdc.ui.widget.OrderShowView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类说明：
 *
 * @author Administrator
 * @version 1.0
 * @date 2015/3/19
 */

public class OrderListWorker extends AbstractListWorker {

    private Context mContext;

    private List<MenuItemInfo> mDataList;
    private OnListWorkerListener mOnListWorkerListener;

    public static final int SUBMIT_STATE = 1;
    public static final int CONFIRM_STATE = 2;

    private int mCurState = SUBMIT_STATE;

    public int getCurState() {
        return mCurState;
    }

    public void setCurState(int curState) {
        this.mCurState = curState;
    }

    public OrderListWorker(Context mContext, List<MenuItemInfo> dataList, OnListWorkerListener mOnListWorkerListener) {
        this.mContext = mContext;
        this.mDataList = dataList;
        this.mOnListWorkerListener = mOnListWorkerListener;
        rebuildDataList();
        rebuildWorkerMap();
    }

    public void setData(List<MenuItemInfo> dataList) {
        mDataList = dataList;
        rebuildDataList();

    }

    @Override
    protected Map<Integer, ItemWorker> constructItemWorkerMap() {
        Map<Integer, ItemWorker> map = new HashMap<Integer, ItemWorker>();
        map.put(DataType.MENU.ordinal(), new MenuItemWorker(mOnListWorkerListener));
        return map;
    }

    @Override
    protected List<ListData> constructItemDataList() {
        List<ListData> datas = new ArrayList<ListData>();

        for (int i = 0; i < mDataList.size(); ++i) {
            ListData itemData = new ListData();
            itemData.type = DataType.MENU.ordinal();
            itemData.obj = mDataList.get(i);
            datas.add(itemData);
        }

        return datas;
    }


    public enum DataType {
        MENU
    }

    public interface OnListWorkerListener {

        void onItemClick(int index);

        /**
         * 加菜
         */
        void onAddMenuClicked(Object itemData);

        /**
         * 减菜
         */
        void onSubMenuClicked(Object itemData);
    }

    class MenuItemWorker implements ItemWorker {
        OnListWorkerListener mOnListWorkerListener;

        public MenuItemWorker(OnListWorkerListener onListWorkerListener) {
            mOnListWorkerListener = onListWorkerListener;
        }

        @Override
        public View newView(int position, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View convertView = inflater.inflate(R.layout.order_show_item, null, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            return convertView;
        }

        @Override
        public void updateItem(View convertView, Object itemData, ViewGroup parent, int position) {
            final MenuItemInfo menu = (MenuItemInfo) itemData;
            final ViewHolder holder = (ViewHolder) convertView.getTag();

            holder.order_item_name.setText(menu.getName());
            holder.order_item_price.setText("¥" + menu.getPrice());
            holder.order_item_count.setText("" + menu.getCount());

            if (OrderShowView.SUBMIT_STATE == mCurState) {
                holder.order_item_add.setVisibility(View.VISIBLE);
                holder.order_item_sub.setVisibility(View.VISIBLE);
            } else {
                holder.order_item_add.setVisibility(View.INVISIBLE);
                holder.order_item_sub.setVisibility(View.INVISIBLE);
            }

            holder.order_item_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnListWorkerListener != null) {
                        mOnListWorkerListener.onAddMenuClicked(menu);
                        holder.order_item_count.setText("" + menu.getCount());
                    }
                }
            });

            holder.order_item_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnListWorkerListener != null) {
                        mOnListWorkerListener.onSubMenuClicked(menu);
                        holder.order_item_count.setText("" + menu.getCount());
                    }
                }
            });

        }

        @Override
        public void onItemClicked(int position, View itemView, ViewGroup parent, Object itemData) {
            if (mOnListWorkerListener != null) {
                mOnListWorkerListener.onItemClick(position);
            }

        }

        class ViewHolder {
            TextView order_item_name;
            TextView order_item_price;
            TextView order_item_count;
            ImageView order_item_add;
            ImageView order_item_sub;

            ViewHolder(View view) {
                order_item_name = (TextView) view.findViewById(R.id.order_item_name);
                order_item_price = (TextView) view.findViewById(R.id.order_item_price);
                order_item_count = (TextView) view.findViewById(R.id.order_item_count);
                order_item_add = (ImageView) view.findViewById(R.id.order_item_add);
                order_item_sub = (ImageView) view.findViewById(R.id.order_item_sub);
            }
        }
    }
}
