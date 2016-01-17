package com.handgold.pjdc.common.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

public interface ListWorker extends OnItemClickListener {

    /**
     * 构造新的itemView
     * 
     * @param index item 位置position
     * @param ViewGroup  itemview的父view，如listview
     * @return 新的itemView
     */
    public View newItemView(int position, ViewGroup parent);

    /**
     * 更新item显示
     * 
     * @param index item的位置 position
     * @param convertView item对应的view
     */
    public void updateItem(int index, View convertView, ViewGroup parent);

    /**
     * 获取item的数量
     * 
     * @return
     */
    public int getCount();

    /**
     * 获取对应item的信息（一般是该item的数据对象）
     * 
     * @param index
     * @return
     */
    public Object getItem(int index);

    /**
     * 获取Item的id
     * 
     * @param index
     * @return
     */
    public int getItemId(int index);

    /**
     * 获取item的类型数量
     * 
     * @return
     */
    int getViewTypeCount();

    /**
     * 获取某个item的类型
     * 
     * @param position item的index
     * @return
     */
    int getItemViewType(int position);
}
