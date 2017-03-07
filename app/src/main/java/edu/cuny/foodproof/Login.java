package edu.cuny.foodproof;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

import org.json.*;

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
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btLogin:
                try {
                    new makePostRequest().execute("http://ec2-54-90-187-63.compute-1.amazonaws.com/login");
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
                break;
            case R.id.btRegister:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private class makePostRequest extends AsyncTask<String, Void, String> {
        String inUsername;
        String inPassword;

        @Override
        protected void onPreExecute(){
            inUsername = etUsername.getText().toString();
            inPassword = etPassword.getText().toString();
        }

      @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            if(responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line = br.readLine();
                return line;
            }
            else{
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                String line = br.readLine();
                return line;
            }
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
      }

        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject resultJSON = new JSONObject(result);
                String successJSON = resultJSON.getString("Result");
                if(successJSON.equals("succeed")){
                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    mainIntent.putExtra("loggedIn", "succeeded");
                    startActivity(mainIntent);
                    finish();
                }
                else{
                    Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }

}
