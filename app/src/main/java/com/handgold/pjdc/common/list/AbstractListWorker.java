package com.handgold.pjdc.common.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;

import java.util.List;
import java.util.Map;

public abstract class AbstractListWorker implements ListWorker {

    public AbstractListWorker(){

    }

    public interface ItemWorker {
        /**
         * 新建一个itemView
         * 
         * @param position item的index
         * @param parent itemView的父view，如listView
         * @return
         */
        View newView(int position, ViewGroup parent);

        /**
         * 更新itemView的显示
         * 
         * @param convertView itemView
         * @param itemData 对应的数据
         * @param parent itemView的父view，如listView
         * @param positon item的index
         */
        void updateItem(View convertView, Object itemData, ViewGroup parent, int positon);

        /**
         * 该条目被点击
         * 
         * @param position
         * @param itemView
         * @param parent
         * @param itemData
         */
        void onItemClicked(int position, View itemView, ViewGroup parent, Object itemData);
    }

    public class ListData {
        public int type;

        public Object obj;
    }

    private List<ListData> mDataList;

    private Map<Integer, ItemWorker> mItemWorkerMap;

    protected abstract Map<Integer, ItemWorker> constructItemWorkerMap();

    protected abstract List<ListData> constructItemDataList();

    public void rebuildDataList() {
        mDataList = constructItemDataList();
    }

    public void rebuildWorkerMap() {
        mItemWorkerMap = constructItemWorkerMap();
    }

    @Override
    public View newItemView(int index, ViewGroup parent) {
        if (mDataList == null || mItemWorkerMap == null) {
            return null;
        }
        ListData data = mDataList.get(index);

        ItemWorker itemWorker = mItemWorkerMap.get(data.type);

        View view = itemWorker.newView(index, parent);
        return view;
    }

    @Override
    public void updateItem(int index, View convertView, ViewGroup parent) {
        if (mDataList == null || mItemWorkerMap == null) {
            return;
        }
        ListData data = mDataList.get(index);

        ItemWorker itemWorker = mItemWorkerMap.get(data.type);

        itemWorker.updateItem(convertView, data.obj, parent, index);
    }

    @Override
    public int getCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    @Override
    public Object getItem(int index) {
        if (mDataList == null) {
            return null;
        }
        ListData data = mDataList.get(index);
        return data.obj;
    }

    @Override
    public int getItemId(int index) {
        return index;
    }

    @Override
    public int getViewTypeCount() {
        if (mItemWorkerMap == null) {
            return 0;
        }
        return mItemWorkerMap.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.get(position).type;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Adapter adapter = parent.getAdapter();

        if (adapter instanceof HeaderViewListAdapter) {
            position = position - ((HeaderViewListAdapter) adapter).getHeadersCount();
        }

        if (mDataList == null || mItemWorkerMap == null) {
            return;
        }

        ListData data = mDataList.get(position);

        ItemWorker itemWorker = mItemWorkerMap.get(data.type);

        itemWorker.onItemClicked(position, view, parent, data.obj);
    }
}
