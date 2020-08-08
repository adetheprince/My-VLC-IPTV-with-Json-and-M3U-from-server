package com.tianshaokai.video;

import android.app.Application;

import org.videolan.vlc.media.MediaUtils;

import java.util.List;

public class MyApplication extends Application {
    private List<Video> videoList;
    @Override
    public void onCreate() {
        super.onCreate();

        MediaUtils.init(this);
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }
}
