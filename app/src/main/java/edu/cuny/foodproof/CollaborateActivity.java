package edu.cuny.foodproof;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.Date;
import java.util.List;

public class CollaborateActivity extends AppCompatActivity {
    ListView lvBroadcasts;
    List<String> phoneNumbers = new ArrayList<>();
    ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborate);

        lvBroadcasts = (ListView) findViewById(R.id.lvBroadcasts);

        lvBroadcasts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumbers.get(position)));

                if (ActivityCompat.checkSelfPermission(CollaborateActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CollaborateActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(CollaborateActivity.this,new String[]{Manifest.permission.CALL_PHONE}, 1);
                }

                startActivity(intent);
            }

        });

        try {
            new makePostRequest().execute("http://ec2-54-90-187-63.compute-1.amazonaws.com/get_all_recipe");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private class makePostRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params){
            String postParameters = "";
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
                JSONArray resultArray = fullResult.getJSONArray("Recipes");
                List<String> items = new ArrayList<>();
                phoneNumbers = new ArrayList<>();

                for(int i = 0; i < resultArray.length(); i++){
                    items.add(resultArray.getJSONObject(i).getString("user") + " is cooking " + resultArray.getJSONObject(i).getJSONArray("meals").getString(1) + " on " + new Date(resultArray.getJSONObject(i).getJSONArray("meals").getLong(0) * 1000).toString());
                    phoneNumbers.add(resultArray.getJSONObject(i).getJSONArray("meals").getString(2));
                }

                mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.listview_ingredients, R.id.tvIngredients2,items);


                lvBroadcasts.setAdapter(mArrayAdapter);
            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }
}
