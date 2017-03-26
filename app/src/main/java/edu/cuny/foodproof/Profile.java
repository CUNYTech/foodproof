package edu.cuny.foodproof;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Profile extends AppCompatActivity {
    TextView tvUsername;
    TextView tvIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUsername = (TextView) findViewById(R.id.tvUsername);

        tvIngredients = (TextView) findViewById(R.id.tvIngredients);


        try {
            new makePostRequest().execute("http://ec2-54-90-187-63.compute-1.amazonaws.com/ingredient/return");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    private class makePostRequest extends AsyncTask<String, Void, String> {
        String username;

        @Override
        protected void onPreExecute(){
            SharedPreferences mPrefs = getSharedPreferences("userInfo", 0);
            username = mPrefs.getString("username", "");
            tvUsername.setText(username);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params){
            String postParameters = "user=" + username + "&count=20";
            byte[] postData = postParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            try {

                String response = "";

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
                    String line;

                    while((line=br.readLine()) != null){
                        response += line;
                    }

                    return response;
                }
                else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                    String line;

                    while((line=br.readLine()) != null){
                        response += line;
                    }

                    return response;
                }
            }
            catch(IOException e){
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String result){
            try {

                JSONObject fullResult = new JSONObject(result);
                JSONArray resultArray = fullResult.getJSONArray("ingredients");
                String ingredients = "";
                String line;

                for(int i = 0; i < resultArray.length(); i++){
                    line = resultArray.getString(i);
                    ingredients += line + ", ";
                }

                tvIngredients.setText(ingredients);

            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }

}
