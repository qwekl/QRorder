package com.example.kimjeonmin.myapplication2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NoticeDetailsActivity extends AppCompatActivity{

    private TextView noticeTitle, noticeName, noticeDateCreated,noticeDetails;
    String noticeid,code;
    private NoticeDetailList list;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticedetails);

        noticeTitle = (TextView)findViewById(R.id.noticetitle);
        noticeName = (TextView)findViewById(R.id.noticename);
        noticeDateCreated = (TextView)findViewById(R.id.noticedatecreated);
        noticeDetails = (TextView)findViewById(R.id.noticedetails);

        noticeTitle.setText(getIntent().getStringExtra("title"));
        noticeName.setText(getIntent().getStringExtra("name"));
        noticeDateCreated.setText(getIntent().getStringExtra("datecreated"));

        noticeid = getIntent().getStringExtra("noticeid");

        //お知らせの説明クラス呼び出し
        new BackgroundTask().execute();

    }



    //DBからコードに合うお知らせ説明呼んでくる
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute(){
            noticeid = getIntent().getStringExtra("noticeid");
            code = getIntent().getStringExtra("code");
            System.out.println(code);

            target = "http://sola0722.cafe24.com/NoticeDetail.php?noticeid=";
        }
        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target+noticeid+"&code="+code);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        @Override
        public void onProgressUpdate(Void...values){
            super.onProgressUpdate();
        }
        @Override
        public void onPostExecute(String result){

            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String detail;
                while (count < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    detail = object.getString("detail");
                    list = new NoticeDetailList(detail);
                    list.setDetail(detail);
                    count++;
                }
                noticeDetails.setText(list.getDetail());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
