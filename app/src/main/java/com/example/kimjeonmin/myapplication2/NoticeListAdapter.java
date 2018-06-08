package com.example.kimjeonmin.myapplication2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NoticeListAdapter extends BaseAdapter{

    private Context context;
    private List<NoticeListActivirty> noticeList;

    // 생성자
    public NoticeListAdapter(Context context, List<NoticeListActivirty> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.activity_noticelist, null);
        TextView titleText = (TextView)v.findViewById(R.id.titleText);
        TextView nameText = (TextView)v.findViewById(R.id.nameText);
        TextView dateText = (TextView)v.findViewById(R.id.dateText);

        titleText.setText(noticeList.get(i).getTitle());
        nameText.setText(noticeList.get(i).getName());
        dateText.setText(noticeList.get(i).getDatecreated());

        v.setTag(noticeList.get(i).getTitle());
        return v;
    }
}
