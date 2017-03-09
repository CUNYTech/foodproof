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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button btRegister;
    EditText etName, etEmail, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btRegister = (Button) findViewById(R.id.btRegister);

        btRegister.setOnClickListener(this);


    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btRegister:
                try {
                    new makePostRequest().execute("http://ec2-54-90-187-63.compute-1.amazonaws.com/register");
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private class makePostRequest extends AsyncTask<String, Void, String> {
        String inUsername;
        String inPassword;
        String inEmail;

        @Override
        protected void onPreExecute(){
            inUsername = etName.getText().toString();
            inPassword = etPassword.getText().toString();
            inEmail = etEmail.getText().toString();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params){
            String postParameters = "user=" + inUsername + "&password=" + inPassword + "&email=" + inEmail;
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
                    Intent mainIntent = new Intent(getApplicationContext(), Login.class);
                    startActivity(mainIntent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }
}
