package com.example.kimjeonmin.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class NoticeDetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticedetails);

        Intent intent = getIntent();

        NoticeActivirty n =intent.getParcelableExtra("noticeActivirtyList");

    }
}
