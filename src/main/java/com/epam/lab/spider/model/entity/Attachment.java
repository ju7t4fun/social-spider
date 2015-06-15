package com.epam.lab.spider.model.entity;

/**
 * Created by Sasha on 12.06.2015.
 */
public class Attachment {

    private Integer id;
    private String url;
    private Integer postId;
    private Type type;
    private Boolean deleted = false;

    public enum Type {
        AUDIO, PHOTO, VIDEO
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", postId=" + postId +
                ", type=" + type +
                ", deleted=" + deleted +
                '}';
    }
}
