package com.miasmesh.meqdim.africaniffstracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by meqdim on 29/7/2015.
 */
public class PanelListDataAdapter extends ArrayAdapter<PanelListData> {
    Context context;
    int layoutResourceId;
    PanelListData data[] = null;

    public PanelListDataAdapter(Context context, int layoutResourceId, PanelListData[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PLDataHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PLDataHolder();
            holder.member_picture = (ImageView)row.findViewById(R.id.member_picture);
            holder.member_name = (TextView)row.findViewById(R.id.member_name);
            holder.member_title = (TextView)row.findViewById(R.id.member_title);
            holder.member_role = (TextView)row.findViewById(R.id.member_role);
            holder.member_profile = (TextView)row.findViewById(R.id.member_profile);

            row.setTag(holder);
        }
        else{
            holder = (PLDataHolder)row.getTag();
        }

        PanelListData PLData = data[position];
        holder.member_name.setText(PLData.name);
        holder.member_title.setText(PLData.title);
        holder.member_role.setText(PLData.role);
        holder.member_profile.setText(PLData.profile);
        holder.member_picture.setImageResource(PLData.pic);

        return row;
    }

    static class PLDataHolder
    {
        TextView member_name;
        TextView member_title;
        TextView member_role;
        TextView member_profile;
        ImageView member_picture;
    }
}
