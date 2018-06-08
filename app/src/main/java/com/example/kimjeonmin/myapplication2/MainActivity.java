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
    private TextView textViewCode, textViewName;
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
        mainImage = (ImageView)findViewById(R.id.mainImage);
        qrScan = new IntentIntegrator(this);



        //사용자 id 가져오기
        id = getIntent().getStringExtra("id");


        //공지사항 버튼
        NoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
                intent.putExtra("code", textViewCode.getText().toString());
                startActivity(intent);
            }
        });


        //메뉴 버튼
        MenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("code", textViewCode.getText().toString());
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        //장바구니 버튼
        shoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
                intent.putExtra("code", textViewCode.getText().toString());
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        // 스캐너 버튼
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
    //스캔 결과 얻어오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                Toast.makeText(MainActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                try {
                    //data를 json으로 변환
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
//                new BackgroundTask().execute();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    //이미지 불러오기
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
}
