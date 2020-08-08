package com.tianshaokai.video;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class VideoAdapter extends BaseAdapter {

    private Context context;
    private List<VideoLive> childArray;

    public VideoAdapter(Context context, List<VideoLive> childArray) {
        this.context = context;
        this.childArray = childArray;
    }

    @Override
    public int getCount() {
        return childArray.size();
    }

    @Override
    public Object getItem(int position) {
        return childArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setText(childArray.get(position).getChname());
        view.setTextColor(Color.BLACK);
        return view;
    }
}
