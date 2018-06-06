package com.example.kimjeonmin.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NoticeDetailsActivity extends AppCompatActivity{

    private TextView noticeTitle, noticeName, noticeDateCreated,noticeDetails;
    String title,name,datecreated;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticedetails);

        noticeTitle = (TextView)findViewById(R.id.noticetitle);
        noticeName = (TextView)findViewById(R.id.noticename);
        noticeDateCreated = (TextView)findViewById(R.id.noticedatecreated);
        noticeDetails = (TextView)findViewById(R.id.noticedetails);

        title = getIntent().getStringExtra("title");
        name = getIntent().getStringExtra("name");
        datecreated = getIntent().getStringExtra("datecreated");

        System.out.println(title+name+datecreated);

        noticeTitle.setText(title);
        //noticeName.setText(getIntent().getStringExtra("name"));
        //noticeTitle.setText(getIntent().getStringExtra("datecreated"));


    }
}
