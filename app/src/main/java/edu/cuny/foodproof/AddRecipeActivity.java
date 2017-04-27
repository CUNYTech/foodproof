package edu.cuny.foodproof;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {
    CaldroidFragment caldroidFragment;
    FragmentTransaction t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSelectDate(Date date, View view) {
                String recipeName = getIntent().getStringExtra("Recipe");
                TextView tView = (TextView) view;
                tView.setText(recipeName);
                Toast.makeText(getApplicationContext(), "Recipe has been saved to calendar", Toast.LENGTH_LONG).show();
                new makePostRequest().execute(String.valueOf((date.getTime() / 1000)));
            }
        });

        t = getSupportFragmentManager().beginTransaction();
        t.add(R.id.AddCalendar, caldroidFragment).commit();
    }


    private class makePostRequest extends AsyncTask<String, Void, String> {
        String username;
        String recipeName;
        String privacyValue;

        @Override
        protected void onPreExecute(){
            SharedPreferences mPrefs = getSharedPreferences("userInfo", 0);
            username = mPrefs.getString("username", "");
            recipeName = getIntent().getStringExtra("Recipe");
            privacyValue = getIntent().getStringExtra("Private");
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params){
            String postParameters = "user=" + username + "&recipe=" + recipeName + "&date=" + params[0] + "&private=" + privacyValue;
            byte[] postData = postParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            try {

                String response = "";
                URL url = new URL("http://ec2-54-90-187-63.compute-1.amazonaws.com/save_recipe");
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
                //Converting the response string that was returned from doInBackground() into a JSONObject
                JSONObject resultJSON = new JSONObject(result);

                //Getting the 'Result' attribute of the JSON
                String successJSON = resultJSON.getString("Result");

                //Case where the 'Result' attribute is 'success'
                if(successJSON.equals("succeed")){

                    Toast.makeText(getApplicationContext(), "Recipe has been saved to calendar", Toast.LENGTH_LONG).show();

                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    mainIntent.putExtra("loggedIn", "succeeded");
                    startActivity(mainIntent);

                }
            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }
}