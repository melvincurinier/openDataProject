package com.mc.opendataproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class AssociationAdapter extends BaseAdapter {
    Context context;
    ArrayList<Association> list;

    public AssociationAdapter(Context context, ArrayList<Association> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ConstraintLayout layoutItem = null;
        LayoutInflater mInflater = LayoutInflater.from(context);

        if (convertView == null) {
            layoutItem = (ConstraintLayout) mInflater.inflate(R.layout.item_layout, parent, false);
        } else { layoutItem = (ConstraintLayout) convertView; }
        ViewHolder holder = (ViewHolder) layoutItem.getTag();
        if(holder == null) {
            holder = new ViewHolder();
            layoutItem.setTag(holder);
        }

        return layoutItem;
    }

    @Override
    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();
    }

    private class ViewHolder{

    }
}
