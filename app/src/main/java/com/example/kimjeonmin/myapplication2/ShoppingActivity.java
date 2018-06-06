package com.example.kimjeonmin.myapplication2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity{

    private ListView shopping_listview;
    private ShoppingListAdapter adapter;
    private List<ShoppingList> shoppingList;
    private Button orderButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        shopping_listview = (ListView)findViewById(R.id.shopping_listView);
        shoppingList = new ArrayList<ShoppingList>();
        adapter = new ShoppingListAdapter(getApplicationContext(),shoppingList);
        shopping_listview.setAdapter(adapter);
        orderButton = (Button)findViewById(R.id.orderButton);

        final String code = getIntent().getStringExtra("code");


        //결제 버튼
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Order().execute();
                new ShoppingDelete().execute();
                Toast.makeText(getApplicationContext(),"결제가 완료되었습니다.",Toast.LENGTH_LONG).show();

            }
        });



        //메뉴 클레스 접근
        new BackgroundTask().execute();


        shopping_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(ShoppingActivity.this, PopUpActivity.class);
                intent.putExtra("menuname", shoppingList.get(i).getMenuname());
                intent.putExtra("code",code);
                startActivityForResult(intent,1);
            }
        });
    }


    //서버에서 메뉴 불러오기
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;
        String userid = getIntent().getStringExtra("id");
        String code = getIntent().getStringExtra("code");

        @Override
        protected void onPreExecute(){
            target = "http://sola0722.cafe24.com/Shoppinglist.php?userid=" + userid + "&companyid=" + code;
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
                int i = 0;
                String menuname, price, count;
                while (i < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(i);
                    menuname = object.getString("menuname");
                    price = object.getString("price");
                    count = object.getString("count");
                    ShoppingList shopping = new ShoppingList(menuname,price,count);
                    shoppingList.add(shopping);
                    adapter.notifyDataSetChanged();
                    i++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //결제 클레스
    class Order extends AsyncTask<Void, Void, String> {

        String target;
        String code = getIntent().getStringExtra("code");


        @Override
        protected void onPreExecute(){
            target = "http://sola0722.cafe24.com/Order.php?code="+code;
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
        }
    }

    //결제완료목록 지우기
    class ShoppingDelete extends AsyncTask<Void, Void, String> {

        String target;
        String code = getIntent().getStringExtra("code");

        @Override
        protected void onPreExecute(){
            target = "http://sola0722.cafe24.com/ShoppingDelete.php?code="+code;
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
        }
    }
}
