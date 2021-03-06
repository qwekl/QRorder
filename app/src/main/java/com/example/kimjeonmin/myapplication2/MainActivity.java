package com.example.kimjeonmin.myapplication2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView MenuButton,buttonScan, shoppingButton, NoticeButton;
    private TextView textViewCode, textViewName, textViewPhonenumber;
    private ImageView mainImage;
    private IntentIntegrator qrScan;
    private ImageBack task;
    private Bitmap bmp;
    private String logoimageUrl = "http://sola0722.cafe24.com/logoimage/";
    private  String code,id;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

        MenuButton = (ImageView) findViewById(R.id.menu_button);
        buttonScan = (ImageView) findViewById(R.id.qr_button);
        shoppingButton = (ImageView) findViewById(R.id.shopping_button);
        NoticeButton = (ImageView)findViewById(R.id.notice_button);
        textViewCode = (TextView)findViewById(R.id.textViewCode);
        textViewName = (TextView)findViewById(R.id.textViewName);
        textViewPhonenumber = (TextView)findViewById(R.id.textViewPhonenumber);
        mainImage = (ImageView)findViewById(R.id.mainImage);
        qrScan = new IntentIntegrator(this);



        //ユーザーのidを持ってくる
        id = getIntent().getStringExtra("id");

        new UserPhoneNumber().execute();


        //お知らせボタン
        NoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
                intent.putExtra("code", textViewCode.getText().toString());
                startActivity(intent);
            }
        });


        //メニューボタン
        MenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("code", textViewCode.getText().toString());
                intent.putExtra("phonenumber", textViewPhonenumber.getText().toString());
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        //買い物かごボタン
        shoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
                intent.putExtra("code", textViewCode.getText().toString());
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        // スキャナーボタン
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("Scanning...");
                //qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });
    }

    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }
    //スキャン結果を得る
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcodeがない
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcodeがある
                Toast.makeText(MainActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                try {
                    //データをjsonに変換
                    JSONObject obj = new JSONObject(result.getContents());
                    textViewName.setText(obj.getString("name"));
                    textViewCode.setText(obj.getString("code"));
                } catch (JSONException e) {
                    e.printStackTrace();
                };

                task = new ImageBack();
                code = textViewCode.getText().toString();
                try {
                    bmp = task.execute(logoimageUrl+code+".jpg").get();
                    mainImage.setImageBitmap(bmp);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    //ウェブサーバーからイメージを得る
    private class ImageBack extends AsyncTask<String, Integer, Bitmap> {
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

    //データベースからお知らせのテーブルを得る
    class UserPhoneNumber extends AsyncTask<Void, Void, String> {

        String target;
        String id = getIntent().getStringExtra("id");

        @Override
        protected void onPreExecute(){
            target = "http://sola0722.cafe24.com/UserPhonenumber.php?id=";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target + id);
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
                String phonenumber = null;
                while (count < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    phonenumber = object.getString("phonenumber");
                    count++;
                }
                textViewPhonenumber.setText(phonenumber);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
