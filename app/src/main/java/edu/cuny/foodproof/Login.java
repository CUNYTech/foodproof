package edu.cuny.foodproof;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Login extends AppCompatActivity  implements View.OnClickListener{

    Button bLogin, bRegister;
    EditText etUsername, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.btLogin);
        bRegister = (Button) findViewById(R.id.btRegister);

        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        String inUsername = etUsername.getText().toString();
        String inPassword = etPassword.getText().toString();
        switch(v.getId()){
            case R.id.btLogin:
                String postParameters = "username=" + inUsername + "&password=" + inPassword;
                byte[] postData = postParameters.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;
                try {
                    URL url = new URL("http://ec2-54-90-187-63.compute-1.amazonaws.com/login");
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
                    }
                }
                catch(IOException e){
                    throw new RuntimeException(e);
                }
                break;
            case R.id.btRegister:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private class makePostRequest extends AsyncTask<String, Void, String>{

      @Override
      protected String doInBackground(){
        String inUsername = etUsername.getText().toString();
        String inPassword = etPassword.getText().toString();
        String postParameters = "username=" + inUsername + "&password=" + inPassword;
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
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            final StringBuilder output = new StringBuilder("Request URL " + params[0]);
            output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
            output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            System.out.println("output===============" + br);
            while((line = br.readLine()) != null ) {
              responseOutput.append(line);
            }
            br.close();
            output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
      }

      @Override
      protected void onPostExecute(String result){

      }



    }

}
