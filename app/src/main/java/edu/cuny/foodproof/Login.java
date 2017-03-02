package edu.cuny.foodproof;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Login extends AppCompatActivity  implements View.OnClickListener{

    Button bLogin, bRegister;
    EditText etUsername, etPassword;
    TextView tvResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvResults = (TextView) findViewById(R.id.tvResults);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.btLogin);
        bRegister = (Button) findViewById(R.id.btRegister);

        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);
        //        etUsername.setBackgroundColor(Color.parseColor("#ffffff"));

    }

    @Override
    public void onClick(View v){
        String inUsername = etUsername.getText().toString();
        String inPassword = etPassword.getText().toString();
        switch(v.getId()){
            case R.id.btLogin:
                String result;
                try {
                    new makePostRequest().execute("http://ec2-54-90-187-63.compute-1.amazonaws.com/login");
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.putExtra("succeeded")
                startActivity(mainIntent);
                break;
            case R.id.btRegister:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private class makePostRequest extends AsyncTask<String, Void, String> {
        String inUsername;
        String inPassword;
        String success;
        byte[] postData;
        int postDataLength;

        @Override
        protected void onPreExecute(){
            inUsername = etUsername.getText().toString();
            inPassword = etPassword.getText().toString();
        }

      @Override
      protected String doInBackground(String... params){
          String postParameters = "user=" + inUsername + "&password=" + inPassword;
          byte[] postData = postParameters.getBytes(StandardCharsets.UTF_8);
          int postDataLength = postData.length;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty( "charset", "utf-8");
            urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            urlConnection.setUseCaches(false);
            try(DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())){
                wr.write(postData);
                wr.flush();
                wr.close();
            }
            int responseCode = urlConnection.getResponseCode();
            final StringBuilder output = new StringBuilder("Request URL " + params[0]);
            output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            System.out.println("output===============" + br);
            line = br.readLine();
            responseOutput.append(line);
            br.close();
            return responseOutput.toString();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
      }

        @Override
        protected void onPostExecute(String result){
            tvResults.setText(result);
        }


    }

}
