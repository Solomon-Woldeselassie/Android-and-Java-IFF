package com.miasmesh.meqdim.africaniffstracker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by meqdim on 1/7/2015.
 */
public class IffDataAdapter extends ArrayAdapter<IffData>{
    Context context;
    int layoutResourceId;
    IffData data[] = null;

    //    public IffDataAdapter(Context context, int layoutResourceId, IffData[] data) {
    public IffDataAdapter(Context context, int layoutResourceId, IffData[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        IffDataHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new IffDataHolder();
//            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.list_item_years_textView = (TextView)row.findViewById(R.id.list_item_years_textView);
            holder.list_item_amount_textView = (TextView)row.findViewById(R.id.list_item_amount_textView);

            row.setTag(holder);
        }
        else
        {
            holder = (IffDataHolder)row.getTag();
        }

        IffData iffData = data[position];
        holder.list_item_years_textView.setText(iffData.year.toUpperCase());
        holder.list_item_amount_textView.setText(iffData.amount);

        return row;
    }

    static class IffDataHolder
    {
        TextView list_item_years_textView;
        TextView list_item_amount_textView;
    }
}
