package com.example.kimjeonmin.myapplication2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MenuListAdapter extends BaseAdapter {

    private Context context;
    private List<MenuList> menuList;

    public MenuListAdapter(Context context, List<MenuList> noticeList) {
        this.context = context;
        this.menuList = noticeList;
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int i) {
        return menuList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.activity_manulist, null);
        TextView menunameText = (TextView)v.findViewById(R.id.menunameText);
        TextView menupriceText = (TextView)v.findViewById(R.id.menupriceText);

        menunameText.setText(menuList.get(i).getMenuname());
        menupriceText.setText(menuList.get(i).getPrice()+"원");

        v.setTag(menuList.get(i).getMenuname());
        return v;
    }
}
