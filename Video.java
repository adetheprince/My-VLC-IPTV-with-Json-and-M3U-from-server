package com.tianshaokai.video;

/**
 * Created by Administrator on 2016/11/21.
 */

public class Video {

    private String country;
    private String name;
    private String uri;
    private String extension;

    public Video() {
    }

    public Video(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
