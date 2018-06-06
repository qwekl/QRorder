package com.example.kimjeonmin.myapplication2;

public class NoticeActivirty {

    String noticeid;
    String title;
    String name;
    String datecreated;

    public NoticeActivirty(String noticeid ,String title, String name, String datecreated) {
        this.noticeid = noticeid;
        this.title = title;
        this.name = name;
        this.datecreated = datecreated;
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }
}
