package com.tianshaokai.video;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.videolan.vlc.gui.video.VideoPlayerActivity;
import org.videolan.vlc.media.MediaUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //定义两个List，用来存放控件中Group/Child中的String
    private List<String> groupArray;
    private List<List<Video>> childArray;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //对这两个List进行初始化，并插入一些数据
        groupArray = new ArrayList<String>();
        childArray = new ArrayList<List<Video>>();

        groupArray.add("视频1");
        groupArray.add("视频2");

        List<Video> tempArray = new ArrayList<Video>();
        Video video = new Video();
        video.setName("唐唐讲段子");
        video.setUri("http://mvvideo1.meitudata.com/579f07913f4254431.mp4");
        tempArray.add(video);

        for (int index = 0; index < groupArray.size(); ++index) {
            childArray.add(tempArray);
        }

        //给定义好的ExpandableListView添加上Adapter
        ExpandableListView expandableListView = (ExpandableListView)findViewById(R.id.listView);
        ExpandableAdapter adapter = new ExpandableAdapter(this);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(MainActivity.this, "group=" + groupPosition + "---child=" + childPosition + "---" + childArray.get(groupPosition).get(childPosition).getUri(), Toast.LENGTH_SHORT).show();
                Video video = childArray.get(groupPosition).get(childPosition);

                // Two method to open the video.
                MediaUtils.openStream(MainActivity.this, video.getUri(), video.getName());
                //VideoPlayerActivity.start(MainActivity.this, Uri.parse(video.getUri()), video.getName());
                return false;
            }
        });
    }

    //定义ExpandableListView的Adapter
    public class ExpandableAdapter extends BaseExpandableListAdapter {
        Activity activity;

        public ExpandableAdapter(Activity a) {
            activity = a;
        }

        public Object getChild(int groupPosition, int childPosition) {
            return childArray.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return childArray.get(groupPosition).size();
        }

        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            Video video = childArray.get(groupPosition).get(childPosition);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 64);
            TextView text = new TextView(activity);
            text.setLayoutParams(layoutParams);
            // Center the text vertically
            text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            text.setPadding(36, 0, 0, 0);
            text.setText(video.getName());
            return text;
        }

        // group method stub
        public Object getGroup(int groupPosition) {
            return groupArray.get(groupPosition);
        }

        public int getGroupCount() {
            return groupArray.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String string = groupArray.get(groupPosition);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 64);
            TextView text = new TextView(activity);
            text.setLayoutParams(layoutParams);
            // Center the text vertically
            text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            text.setPadding(36, 0, 0, 0);
            text.setText(string);
            return text;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
