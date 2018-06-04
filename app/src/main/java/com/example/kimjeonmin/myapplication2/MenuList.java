package com.example.kimjeonmin.myapplication2;

public class MenuList {

    String menuname;
    String price;
    String menuid;

    public MenuList(String menuname, String price, String menuid) {
        this.menuname = menuname;
        this.price = price;
        this.menuid = menuid;
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
}
