package com.example.kimjeonmin.myapplication2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ShoppingListAdapter extends BaseAdapter {
    private Context context;
    private List<ShoppingList> shoppingList;

    public ShoppingListAdapter(Context context, List<ShoppingList> shoppingList) {
        this.context = context;
        this.shoppingList = shoppingList;
    }

    @Override
    public int getCount() {
        return shoppingList.size();
    }

    @Override
    public Object getItem(int i) {
        return shoppingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.activity_shoppinglist, null);
        TextView menunameText = (TextView)v.findViewById(R.id.menunameText);
        TextView menupriceText = (TextView)v.findViewById(R.id.menupriceText);
        TextView menucountText = (TextView)v.findViewById(R.id.menucountText);

        menunameText.setText(shoppingList.get(i).getMenuname());
        menupriceText.setText(shoppingList.get(i).getPrice()+"원");
        menucountText.setText(shoppingList.get(i).getCount());


        v.setTag(shoppingList.get(i).getMenuname());
        return v;
    }
}
