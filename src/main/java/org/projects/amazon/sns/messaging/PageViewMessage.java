package org.projects.amazon.sns.messaging;

public class PageViewMessage {
    private String id;
    private String url;
    private int viewCount;

    public PageViewMessage() {
    }

    public PageViewMessage(String id, String url, int viewCount) {
        this.id = id;
        this.url = url;
        this.viewCount = viewCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "PageViewMessage{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", viewCount=" + viewCount +
                '}';
    }
}
