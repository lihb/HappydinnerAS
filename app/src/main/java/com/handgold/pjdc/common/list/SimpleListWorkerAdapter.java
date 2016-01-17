package com.handgold.pjdc.common.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SimpleListWorkerAdapter extends BaseAdapter {

    protected ListWorker listWorker = null;

    public SimpleListWorkerAdapter(ListWorker listWorker){
        this.listWorker = listWorker;
    }

    @Override
    public int getCount() {
        return listWorker.getCount();
    }

    @Override
    public Object getItem(int position) {
        return listWorker.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return listWorker.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (convertView == null) {
            view = listWorker.newItemView(position, parent);
        } else {
            view = convertView;
        }

        listWorker.updateItem(position, view, parent);

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return listWorker.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return listWorker.getItemViewType(position);
    }

}
