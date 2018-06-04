package com.example.kimjeonmin.myapplication2;

public class ShoppingList {

    String menuname;
    String price;
    String count;



    public ShoppingList(String menuname, String price, String count) {
        this.menuname = menuname;
        this.count = count;
        this.price = price;

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

    public String getCount() { return count; }

    public void setCount(String count) { this.count = count; }
}
