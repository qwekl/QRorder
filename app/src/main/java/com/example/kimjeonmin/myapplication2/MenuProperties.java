package com.example.kimjeonmin.myapplication2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;

public class MenuProperties extends AppCompatActivity {

    private static String TAG = "phptest_MenuProperties";

    private TextView menunameText, menupriceText, menudescriptionText, orderCount, sumPrice;
    private Button countPluse, countMinus, shoppingButton;
    private ImageView menuImage;
    private String imgUrl = "http://sola0722.cafe24.com/uploads/";
    private back task;
    private Bitmap bpm;
    private String menuname, code, userid;
    private int menuprice;
    private int menucount = 1;
    MenuDescriptionList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuproperties);

        Intent intent = getIntent();


        menunameText = (TextView)findViewById(R.id.menunameText);
        menupriceText = (TextView)findViewById(R.id.menupriceText);
        menudescriptionText = (TextView)findViewById(R.id.menudescriptionText);
        menuImage = (ImageView)findViewById(R.id.menuimage);
        orderCount = (TextView)findViewById(R.id.ordercount);
        countPluse = (Button)findViewById(R.id.countpluse);
        countMinus = (Button)findViewById(R.id.countminus);
        shoppingButton = (Button)findViewById(R.id.shopping_button);
        sumPrice = (TextView)findViewById(R.id.price);
        final String menuid = getIntent().getStringExtra("menuid");
        final String price = getIntent().getStringExtra("price");
        userid = getIntent().getStringExtra("userid");


        menuprice =  Integer.parseInt(price);


        //메뉴 이미지 불러오기
        task = new back();
        try {
            bpm = task.execute(imgUrl+ menuid + ".jpg").get();
            menuImage.setImageBitmap(bpm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //메뉴 이름
        menunameText.setText(getIntent().getStringExtra("menuname"));
        //메뉴 가격
        menupriceText.setText(getIntent().getStringExtra("price")+"원");

        //메뉴 설명
        new BackgroundTask().execute();

        //개수에 따른 메뉴 총 가격
        sumPrice.setText(NumberFormat.getCurrencyInstance().format(menuprice));

        //개수 증가
        countPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menucount ++;
                display(menucount);
                price(menuprice*menucount);
            }
        });

        //개수 감소
        countMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menucount --;
                if(menucount<1){
                    menucount = 1;
                }
                display(menucount);
                price(menuprice*menucount);
            }
        });

        //장바구니 이동
        shoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menuname = menunameText.getText().toString();
                String price = String.valueOf(menuprice*menucount);
                String count = String.valueOf(menucount);
                String userid = getIntent().getStringExtra("userid");
                String companyid = getIntent().getStringExtra("code");


                InsertData take = new InsertData();
                take.execute(menuname,price,count,userid,companyid);
            }
        });

    }


    //개수 증가 감소 참조
    private void display(int number){
        TextView orderCount = (TextView)findViewById(R.id.ordercount);
        orderCount.setText("" + number);
    }


    //가격 참조
    private void price(int number){
        sumPrice.setText(NumberFormat.getCurrencyInstance().format(number));
    }




    //서버에서 해당 이미지 불러오기
    private class back extends AsyncTask<String, Integer, Bitmap> {
        private Bitmap bmImg;

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);


            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmImg;
        }
    }



    //DB에서 해당 메뉴 설명 불러오기
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute(){
            menuname = getIntent().getStringExtra("menuname");
            code = getIntent().getStringExtra("code");

            target = "http://sola0722.cafe24.com/menudescription.php?menuname=";
        }
        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target+menuname+"&companyid="+code);
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
                String menudescription;
                while (count < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    menudescription = object.getString("description");
                    list = new MenuDescriptionList(menudescription);
                    list.setMenudescription(menudescription);
                    count++;
                }
                menudescriptionText.setText(list.getMenudescription());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MenuProperties.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            shoppingButton.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String menuname = (String)params[0];
            String price = (String)params[1];
            String count = (String)params[2];
            String userid = (String)params[3];
            String companyid = (String)params[4];

            System.out.println(menuname + price + count + userid + companyid);

            String serverURL = "http://sola0722.cafe24.com/Shopping.php";
            String postParameters = "&menuname=" + menuname + "&price=" + price + "&count=" + count + "&userid=" + userid + "&companyid=" + companyid;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

}
