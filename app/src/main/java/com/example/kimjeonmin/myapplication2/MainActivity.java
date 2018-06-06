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

    private Button MenuButton,buttonScan,buttonFind, shoppingButton;
    private TextView textViewCode, textViewName;
    private ImageView mainImage;
    private IntentIntegrator qrScan;
    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<NoticeActivirty> noticeActivirtyList;
    private ImageBack task = new ImageBack();
    private Bitmap bmp;
    private String logoimageUrl = "http://sola0722.cafe24.com/logoimage/";
    private  String code,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MenuButton = (Button)findViewById(R.id.menu_button);
        qrScan = new IntentIntegrator(this);
        buttonScan = (Button) findViewById(R.id.qr_button);
        buttonFind = (Button)findViewById(R.id.buttonFind);
        shoppingButton = (Button)findViewById(R.id.shopping_button);
        textViewCode = (TextView)findViewById(R.id.textViewCode);
        textViewName = (TextView)findViewById(R.id.textViewName);
        mainImage = (ImageView)findViewById(R.id.mainImage);


        //사용자 id 가져오기
        id = getIntent().getStringExtra("id");


        noticeListView = (ListView) findViewById(R.id.listView);
        noticeActivirtyList = new ArrayList<NoticeActivirty>();
        adapter = new NoticeListAdapter(getApplicationContext(), noticeActivirtyList);
        noticeListView.setAdapter(adapter);



        //매장 이미지 찾기 버튼
        buttonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = textViewCode.getText().toString();
                task = new ImageBack();
                try {
                    bmp = task.execute(logoimageUrl+code+".jpg").get();
                    mainImage.setImageBitmap(bmp);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                //공지사항 클레스 접근
                new BackgroundTask().execute();
            }
        });

        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NoticeDetailsActivity.class);
                intent.putExtra("title", noticeActivirtyList.get(position).getTitle());
                intent.putExtra("name", noticeActivirtyList.get(position).getName());
                intent.putExtra("datecreated", noticeActivirtyList.get(position).getDatecreated());
                startActivity(intent);
            }
        });




        // 공지사항 상세 보기 클릭버튼
        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NoticeDetailsActivity.class);
                intent.putExtra("title", noticeActivirtyList.get(i).getTitle());
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
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    //서버에서 공지사항 불러오기
    class BackgroundTask extends AsyncTask<Void, Void, String>{

        String target;
        String code = textViewCode.getText().toString();

        @Override
        protected void onPreExecute(){
            target = "http://sola0722.cafe24.com/Notice.php?companyid=";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target+code);
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
                String title, name, datecreated;
                while (count < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    title = object.getString("title");
                    name = object.getString("name");
                    datecreated = object.getString("datecreated");
                    NoticeActivirty notice = new NoticeActivirty(title, name,datecreated);
                    noticeActivirtyList.add(notice);
                    adapter.notifyDataSetChanged();
                    count++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
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
