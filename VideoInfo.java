package com.tianshaokai.video;

import java.io.Serializable;
import java.util.List;

public class VideoInfo implements Serializable {
    private String name;
    private List<VideoLive> samples;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VideoLive> getSamples() {
        return samples;
    }

    public void setSamples(List<VideoLive> samples) {
        this.samples = samples;
    }
}
