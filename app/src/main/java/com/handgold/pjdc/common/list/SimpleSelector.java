package com.handgold.pjdc.common.list;

import java.util.*;

public class SimpleSelector implements Selector {

    /**
     * 保存忽略的indexs
     */
    protected final Set<Integer> mIgnoreIndexs = new HashSet<Integer>(5);

    protected int mIndexFrom;

    protected int mIndexTo;

    /**
     * 保存选中的indexs
     */
    protected final Set<Integer> mSelectedIndexs = new HashSet<Integer>(5);

    /**
     * 是否是选择的状态
     */
    protected boolean isSelectedState;

    public SimpleSelector(int from, int to, Set<Integer> ignoreIndexs){
        setIndexRange(from, to, ignoreIndexs);
    }

    /**
     * 设置index的范围及忽略的indexs,比如【0~100】忽略5,9
     * 
     * @param from 最小的index 如0
     * @param to 最大的index 如100
     * @param mIgnoreIndexs 需要增加（非替換）忽略的indexs 如5,9
     */
    public void setIndexRange(int from, int to, Set<Integer> indexs) {
        if (from > to) {
            throw new IllegalArgumentException("from[" + from + "] index can not more than to[" + to + "]");
        }
        this.mIndexFrom = from;
        this.mIndexTo = to;
        addIgnoreIndexs(indexs);
        escapedInvalidIndexs();
    }

    /**
     * 删除无效的indexs，比如【0~100】，102为无效
     */
    private void escapedInvalidIndexs() {
        Iterator<Integer> itr = mIgnoreIndexs.iterator();
        while (itr.hasNext()) {
            int index = itr.next();
            if (!validate(index)) {
                itr.remove();
            }
        }
    }

    /**
     * 校验index的合法性
     * 
     * @param index
     * @return
     */
    private boolean validate(int index) {
        if (index >= mIndexFrom && index <= mIndexTo) {
            return true;
        }
        return false;
    }

    /**
     * 设置index的范围最大值
     * 
     * @param maxIndex
     */
    public void setIndexTo(int maxIndex) {
        if (maxIndex < mIndexFrom) {
            throw new IllegalArgumentException("to[" + maxIndex + "] index can not less than from[" + mIndexFrom + "]");
        }
        mIndexTo = maxIndex;
        escapedInvalidIndexs();
    }

    /**
     * 设置index的范围最小值
     * 
     * @param minIndex
     */
    public void setIndexFrom(int minIndex) {
        if (minIndex > mIndexTo) {
            throw new IllegalArgumentException("from[" + minIndex + "] index can not more than to[" + mIndexTo + "]");
        }
        mIndexFrom = minIndex;
        escapedInvalidIndexs();
    }

    /**
     * 增加忽略的index
     * 
     * @param index
     */
    public void addIgnoreIndex(int index) {
        if (validate(index)) {
            mIgnoreIndexs.add(index);
        } else {
            throw new IllegalArgumentException("illegal index, expected [" + mIndexFrom + "," + mIndexTo +
                                               "],but current: " + index);
        }
    }

    /**
     * 增加忽略的indexs
     * 
     * @param indexs
     */
    public void addIgnoreIndexs(Set<Integer> indexs) {
        if (indexs == null) {
            return;
        }
        Iterator<Integer> itr = indexs.iterator();
        while (itr.hasNext()) {
            addIgnoreIndex(itr.next());
        }
    }

    @Override
    public boolean isSelectable(int index) {
        return validate(index) && !mIgnoreIndexs.contains(index);
    }

    @Override
    public boolean isAllselected() {
        return (mSelectedIndexs.size() + mIgnoreIndexs.size()) == (mIndexTo - mIndexFrom + 1);
    }

    @Override
    public void selectItem(int index, boolean isSelected) {
        if (validate(index)) {
            if (isSelected) {
                mSelectedIndexs.add(index);
            } else {
                mSelectedIndexs.remove(index);
            }
        }
    }

    @Override
    public void selectAllItem(boolean isSelected) {
        mSelectedIndexs.clear();
        if (isSelected) {
            for (int index = mIndexFrom; index <= mIndexTo; index++) {
                if (!mIgnoreIndexs.contains(index)) {
                    mSelectedIndexs.add(index);
                }
            }
        }
    }

    @Override
    public List<Integer> getSelectedIndexs() {
        ArrayList<Integer> selectedList = new ArrayList<Integer>();
        Iterator<Integer> itr = mSelectedIndexs.iterator();
        while (itr.hasNext()) {
            selectedList.add(itr.next());
        }
        return selectedList;
    }

    @Override
    public boolean isItemSelected(int index) {
        return mSelectedIndexs.contains(index);
    }

    @Override
    public boolean isSelectedState() {
        return isSelectedState;
    }

    @Override
    public void setSelectedState(boolean selectState) {
        isSelectedState = selectState;
    }

}
