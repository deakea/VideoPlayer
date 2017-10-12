package com.android.deak.videoplayer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 收藏的db
 */
@Table(name = "videoChild", onCreated = "CREATE UNIQUE INDEX index_name ON videoChild(name,id)")
public class VideoChildDb {
    @Column(name = "id",isId = true)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "cover")
    private String cover;
    @Column(name = "upInfo")
    private String upInfo;
    @Column(name = "isCollect")
    private boolean isCollect;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUpInfo() {
        return upInfo;
    }

    public void setUpInfo(String upInfo) {
        this.upInfo = upInfo;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", upInfo=" + upInfo +
                ", isCollect='" + isCollect + '\'' +
                '}';
    }
}
