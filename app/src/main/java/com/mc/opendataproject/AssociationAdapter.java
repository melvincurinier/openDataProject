package com.mc.opendataproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
            holder.city = (TextView) layoutItem.findViewById(R.id.textViewCity);
            holder.address = (TextView) layoutItem.findViewById(R.id.textViewAddress);
            holder.title = (TextView) layoutItem.findViewById(R.id.textViewTitle);
            /*
            holder.description = (TextView) layoutItem.findViewById(R.id.);
            holder.postal_code = (TextView) layoutItem.findViewById(R.id.);
            holder.region = (TextView) layoutItem.findViewById(R.id.);
             */
            layoutItem.setTag(holder);
        }
        holder.city.setText(list.get(i).getCity());
        holder.address.setText(list.get(i).getAddress());
        holder.title.setText(list.get(i).getTitle());
        /*
        holder.description.setText(list.get(i).getDescription());
        holder.postal_code.setText(list.get(i).getPostal_code());
        holder.region.setText(list.get(i).getRegion());
        */

        return layoutItem;
    }

    @Override
    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();
        try
        {
            OutputStream out = context.openFileOutput(MainActivity.FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(list);
            oout.flush();
            oout.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private class ViewHolder{
        public TextView city;
        public TextView address;
        public TextView title;
        public TextView description;
        public TextView postal_code;
        public TextView region;

    }
}
