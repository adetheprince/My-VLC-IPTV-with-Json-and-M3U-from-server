package com.tianshaokai.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.videolan.vlc.media.MediaUtils;

import java.net.URL;
import java.util.List;

public class VideoFragment extends Fragment {

    private ListView listView;
    private VideoAdapter adapter;
    private static List<VideoLive> mChildArray;

    public static VideoFragment getInstance(List<VideoLive> childArray) {
        mChildArray = childArray;
        VideoFragment v = new VideoFragment();
        return v;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        init();
        return view;
    }

    public void init() {
        adapter = new VideoAdapter(getContext(), mChildArray);
        listView.setAdapter(adapter);



        final String url="http://chilani.selfip.com:8000/get.php?username=test&password=1234&type=m3u";
        final String videoName= "MyPlaylist";
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //MediaUtils.openStream(getContext(), mChildArray.get(position).getChlink(), mChildArray.get(position).getChname());
                MediaUtils.openStream(getContext(), url, videoName);
            }
        });
    }

}
