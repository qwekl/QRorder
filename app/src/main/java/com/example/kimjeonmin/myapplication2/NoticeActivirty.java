package com.example.kimjeonmin.myapplication2;

public class NoticeActivirty {

    String title;
    String name;
    String datecreated;

    public NoticeActivirty(String title, String name, String datecreated) {
        this.title = title;
        this.name = name;
        this.datecreated = datecreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
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
