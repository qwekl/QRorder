package com.example.kimjeonmin.myapplication2;

public class MenuList {

    String menuname;
    String price;
    String menuid;
    String filename;

    public MenuList(String menuname, String price, String menuid, String filename) {
        this.menuname = menuname;
        this.price = price;
        this.menuid = menuid;
        this.filename = filename;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
