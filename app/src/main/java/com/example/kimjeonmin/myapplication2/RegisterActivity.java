package com.example.kimjeonmin.myapplication2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    //private static String TAG = "phptest_RegisterActivity";

    private EditText IdText;
    private EditText PasswordText;
    private EditText NameText;
    private EditText EmailText;
    private EditText PhonenumberText;
    private Button RegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        IdText = (EditText)findViewById(R.id.idText);
        PasswordText = (EditText)findViewById(R.id.passwordText);
        NameText = (EditText)findViewById(R.id.nameText);
        EmailText = (EditText)findViewById(R.id.emailText);
        PhonenumberText = (EditText)findViewById(R.id.phoneNumText);

        RegisterButton = (Button)findViewById(R.id.registerButton);

        RegisterButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String id = IdText.getText().toString();
                String password = PasswordText.getText().toString();
                String name = NameText.getText().toString();
                String email = EmailText.getText().toString();
                String phonenumber = PhonenumberText.getText().toString();

                InsertData take = new InsertData();
                take.execute(id,password,name,email,phonenumber);

                IdText.setText("");
                PasswordText.setText("");
                NameText.setText("");
                EmailText.setText("");
                PhonenumberText.setText("");
            }
        });

    }
    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RegisterActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            RegisterButton.setText(result);
           // Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[0];
            String password = (String)params[1];
            String name = (String)params[2];
            String email = (String)params[3];
            String phonenumber = (String)params[4];

            String serverURL = "http://sola0722.cafe24.com/RegistarRequest.php";
            String postParameters = "&id=" + id + "&password=" + password + "&name=" + name + "&email=" + email + "&phonenumber=" + phonenumber;


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
               // Log.d(TAG, "POST response code - " + responseStatusCode);

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

                //Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
