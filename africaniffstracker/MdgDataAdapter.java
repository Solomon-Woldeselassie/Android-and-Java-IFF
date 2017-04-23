package com.miasmesh.meqdim.africaniffstracker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by meqdim on 2/7/2015.
 */
public class MdgDataAdapter extends ArrayAdapter<MdgData> {
    Context context;
    int layoutResourceId;
    MdgData data[] = null;

    //    public MdgDataAdapter(Context context, int layoutResourceId, MdgData[] data) {
    public MdgDataAdapter(Context context, int layoutResourceId, MdgData[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MdgDataHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MdgDataHolder();
//            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.list_item_mdgs_textView = (TextView)row.findViewById(R.id.list_item_goal_textView);
            holder.list_item_mdg_data_textView = (TextView)row.findViewById(R.id.list_item_goal_amount_textView);

            row.setTag(holder);
        }
        else
        {
            holder = (MdgDataHolder)row.getTag();
        }

        MdgData mdgData = data[position];
        holder.list_item_mdgs_textView.setText(mdgData.mdg.toUpperCase());
        holder.list_item_mdg_data_textView.setText(mdgData.data);

        return row;
    }

    static class MdgDataHolder
    {
        TextView list_item_mdgs_textView;
        TextView list_item_mdg_data_textView;
    }
}
