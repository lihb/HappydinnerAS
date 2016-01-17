package com.handgold.pjdc.common.list;

import java.util.List;

public interface Selector {
    /**
     * 是否可以被选择
     * 
     * @param index
     * @return
     */
    boolean isSelectable(int index);

    /**
     * 是否在全选状态
     * 
     * @return true全选状态
     */
    boolean isAllselected();

    /**
     * 选中某个条目
     * 
     * @param index
     * @param isSelected
     */
    void selectItem(int index, boolean isSelected);

    /**
     * 所有条目是否选中
     * 
     * @param isSelected true 选中所有条目；false 不选中所有条目
     */
    void selectAllItem(boolean isSelected);

    /**
     * 获取已经选中条目
     * 
     * @return
     */
    List<Integer> getSelectedIndexs();

    /**
     * 某个条目是否被选中
     * 
     * @param index item position
     * @return true 选中；false 未选中
     */
    boolean isItemSelected(int index);

    /**
     * 是否在选择的状态
     * 
     * @return true，选择状态；false 非选中状态
     */
    boolean isSelectedState();

    /**
     * 设置选择状态
     * 
     * @param selectState true表示选择状态；false 表示非选择状态
     */
    void setSelectedState(boolean selectState);
}
