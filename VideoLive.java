package com.tianshaokai.video;

import java.io.Serializable;

public class VideoLive implements Serializable {

    private Integer chid;
    private String chname;
    private String chlogo;
    private String chlink;
    private String chenable;
    private String chnote;

    public Integer getChid() {
        return chid;
    }

    public void setChid(Integer chid) {
        this.chid = chid;
    }

    public String getChname() {
        return chname;
    }

    public void setChname(String chname) {
        this.chname = chname;
    }

    public String getChlogo() {
        return chlogo;
    }

    public void setChlogo(String chlogo) {
        this.chlogo = chlogo;
    }

    public String getChlink() {
        return chlink;
    }

    public void setChlink(String chlink) {
        this.chlink = chlink;
    }

    public String getChenable() {
        return chenable;
    }

    public void setChenable(String chenable) {
        this.chenable = chenable;
    }

    public String getChnote() {
        return chnote;
    }

    public void setChnote(String chnote) {
        this.chnote = chnote;
    }
}
