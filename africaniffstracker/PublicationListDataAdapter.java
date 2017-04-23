package com.miasmesh.meqdim.africaniffstracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by meqdim on 29/7/2015.
 */
public class PublicationListDataAdapter extends ArrayAdapter<PublicationsData> {
    Context context;
    int layoutResourceId;
    PublicationsData data[] = null;

    public PublicationListDataAdapter(Context context, int layoutResourceId, PublicationsData[] data) {
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
            holder.book_cover = (ImageView)row.findViewById(R.id.document_cover);
            holder.title = (TextView)row.findViewById(R.id.publication_title);
            holder.url = (TextView)row.findViewById(R.id.publication_url);
            holder.dop = (TextView)row.findViewById(R.id.publication_dop);

            row.setTag(holder);
        }
        else{
            holder = (PLDataHolder)row.getTag();
        }

        PublicationsData PLData = data[position];
        holder.title.setText(PLData.title);
        holder.dop.setText(PLData.dop);
        holder.url.setText(PLData.url);
        holder.book_cover.setImageResource(PLData.cover);

        return row;
    }

    static class PLDataHolder
    {
        TextView title;
        TextView dop;
        TextView url;
        ImageView book_cover;
    }
}
