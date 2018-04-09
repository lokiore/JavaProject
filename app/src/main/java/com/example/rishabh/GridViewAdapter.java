package com.example.rishabh;


import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GridViewAdapter extends ArrayAdapter<Upload> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Upload> mGridData = new ArrayList<Upload>();
    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<Upload> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    public void setGridData(ArrayList<Upload> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Upload item = mGridData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getName()));
        Picasso.get().load(item.getImageUrl()).into(holder.imageView);
        return row;
    }
    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
