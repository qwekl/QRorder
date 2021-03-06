package com.example.kimjeonmin.myapplication2;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private MenuListAdapter adapter;
    private List<MenuList> menuList;
    private ListView menuListView;
    private String userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        menuListView = (ListView)findViewById(R.id.listView);
        menuList = new ArrayList<MenuList>();
        adapter = new MenuListAdapter(getApplicationContext(),menuList);
        menuListView.setAdapter(adapter);
        final String code = getIntent().getStringExtra("code");
        final String phonenumber = getIntent().getStringExtra("phonenumber");
        userid = getIntent().getStringExtra("id");


        //メニューのクラスアクセス
        new BackgroundTask().execute();


        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MenuActivity.this, MenuPropertiesActivity.class);
                intent.putExtra("menuname", menuList.get(position).getMenuname());
                intent.putExtra("price", menuList.get(position).getPrice());
                intent.putExtra("menuid", menuList.get(position).getMenuid());
                intent.putExtra("filename", menuList.get(position).getFilename());
                intent.putExtra("code", code);
                intent.putExtra("phonenumber", phonenumber);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });

    }


    //データベースからメニューテーブルを得る
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;
        String code = getIntent().getStringExtra("code");

        @Override
        protected void onPreExecute(){
            target = "http://sola0722.cafe24.com/menu.php?code="+code;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
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
                String menuname, price, menuid,filename;
                while (count < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    menuname = object.getString("menuname");
                    price = object.getString("price");
                    menuid = object.getString("menuid");
                    filename = object.getString("filename");
                    MenuList menu = new MenuList(menuname,price,menuid,filename);
                    menuList.add(menu);
                    adapter.notifyDataSetChanged();
                    count++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
