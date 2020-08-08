package com.tianshaokai.video;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.videolan.vlc.media.MediaUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Action1;

public class MainActivity2 extends AppCompatActivity {

    //定义两个List，用来存放控件中Group/Child中的String
    private List<String> groupArray;
    private List<List<VideoLive>> childArray;
    ExpandableListView expandableListView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView = (ExpandableListView)findViewById(R.id.listView);
        //对这两个List进行初始化，并插入一些数据
        groupArray = new ArrayList<String>();
        childArray = new ArrayList<List<VideoLive>>();
        getVideoJson();
    }

    private void getVideoJson() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://satdoc.dyndns.info/")
                .client(AFHttpClient.getHttpClient())
                .build();

        VideoService loginService = retrofit.create(VideoService.class);
        Call<ResponseBody> call = loginService.getJson();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonString = null;
                try {
                    jsonString = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Type listType = new TypeToken<ArrayList<VideoInfo>>(){}.getType();
                ArrayList<VideoInfo> videoInfoList = new Gson().fromJson(jsonString, listType);

                Log.d("获取的视频个数: ", "" + videoInfoList.size());

                setData(videoInfoList);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("wxl", "onFailure=" + t.getMessage());
            }
        });
    }

    private void setData(ArrayList<VideoInfo> videoInfoList) {
        Observable.from(videoInfoList).subscribe(new Action1<VideoInfo>() {
            @Override
            public void call(VideoInfo videoInfo) {
                groupArray.add(videoInfo.getName());

                childArray.add(videoInfo.getSamples());
            }
        });
        initView();
    }

    private void initView() {
        ExpandableAdapter adapter = new ExpandableAdapter(this);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                VideoLive video = childArray.get(groupPosition).get(childPosition);

                MediaUtils.openStream(MainActivity2.this, video.getChlink(), video.getChname());
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
            VideoLive video = childArray.get(groupPosition).get(childPosition);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 64);
            TextView text = new TextView(activity);
            text.setLayoutParams(layoutParams);
            // Center the text vertically
            text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            text.setPadding(36, 0, 0, 0);
            text.setText(video.getChname());
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
