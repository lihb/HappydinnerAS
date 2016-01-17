package com.handgold.pjdc.common.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class AbstractMultiSelectListWorker implements ListWorker {

    protected ListWorker mListWorker;

    protected Selector mSelectable;

    public AbstractMultiSelectListWorker(ListWorker wrappedListWorker, Selector selectable){
        this.mListWorker = wrappedListWorker;
        this.mSelectable = selectable;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListWorker.onItemClick(parent, view, position, id);
    }

    @Override
    public View newItemView(int position, ViewGroup parent) {
        return mListWorker.newItemView(position, parent);
    }

    @Override
    public void updateItem(int index, View convertView, ViewGroup parent) {
        mListWorker.updateItem(index, convertView, parent);
    }

    @Override
    public int getCount() {
        return mListWorker.getCount();
    }

    @Override
    public Object getItem(int index) {
        return mListWorker.getItem(index);
    }

    @Override
    public int getItemId(int index) {
        return mListWorker.getItemId(index);
    }

    @Override
    public int getViewTypeCount() {
        return mListWorker.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mListWorker.getItemViewType(position);
    }
}
