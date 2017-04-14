package edu.cuny.foodproof;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MenuActivity extends Activity implements View.OnClickListener{
    private Button fridgeButton;
    private EditText edit;
    private ListView lv;
    ArrayList<String> ar = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    //private static final String Username = "user";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right, R.anim.fade_out);
        setContentView(R.layout.activity_menu);
        fridgeButton = (Button)findViewById(R.id.button5);
        lv = (ListView) findViewById(R.id.list);
        fridgeButton.setOnClickListener(this);
        edit = (EditText)findViewById(R.id.ingredient);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ar);
        lv.setAdapter(adapter);try {
            new updateArray().execute("http://ec2-54-90-187-63.compute-1.amazonaws.com/ingredient/return");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }
    private class updateArray extends AsyncTask<String, Void, String> {
        String username;

        @Override
        protected void onPreExecute(){
            SharedPreferences mPrefs = getSharedPreferences("userInfo", 0);
            username = mPrefs.getString("username", "");

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
                String line;

                for(int i = 0; i < resultArray.length(); i++){
                    line = resultArray.getString(i);
                    ar.add(line);
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ar); // change android.R.layout.simple_spinner_item to something wider
                    lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }

    }
    public void onClick(View view) {
        try {
            new makePostRequest().execute("http://ec2-54-90-187-63.compute-1.amazonaws.com/ingredient/add").toString();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }
    private class makePostRequest extends AsyncTask<String, Void, String> {
        String inUsername;
        String inIngredient;
        String input;

        @Override
        protected void onPreExecute(){
            input = edit.getText().toString();
            if (input.length() > 0) {
                adapter.add(input);
                edit.setText("");
            }
            sharedPreferences = getSharedPreferences("MyPref", MODE_APPEND);
            inUsername = sharedPreferences.getString("username", null);
            inIngredient = input;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params){
            String postParameters = "user=" + inUsername + "&ingredient=" + input;
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
                JSONObject resultJSON = new JSONObject(result);
                String successJSON = resultJSON.getString("Result");
                if(successJSON.equals("succeed")){
                    Toast.makeText(getApplicationContext(), input + " was added successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Failed to add", Toast.LENGTH_SHORT).show();
                }
            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }
}
