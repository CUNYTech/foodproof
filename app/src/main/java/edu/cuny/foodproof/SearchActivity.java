package edu.cuny.foodproof;

/**
 * Created by brianhernandez on 3/15/17.
 */

import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    EditText etSearchParameter;
    TextView tvResult;
    ListView lvResult;
    List<String> links;
    ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        overridePendingTransition(R.anim.slide_right, R.anim.fade_out);
        setContentView(R.layout.activity_search);

        etSearchParameter = (EditText) findViewById(R.id.etSearch);

        tvResult = (TextView) findViewById(R.id.tvResult);

        lvResult = (ListView) findViewById(R.id.lvResult);

        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id){
                String toURL = links.get(position);                 //pass url through intents
                SharedPreferences sharedpref = getSharedPreferences("Pref", MODE_APPEND);
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString("recipeURL", toURL);
                editor.commit();
                Intent openRecipe = new Intent(SearchActivity.this, WebViewActivity.class);
                openRecipe.putExtra("Recipe", lvResult.getItemAtPosition(position).toString());
                startActivity(openRecipe);
            }

        });

    }

    public void searchButtonClick(View v) {
        try {
            new makePostRequest().execute("http://food2fork.com/api/search");
            //new makeIngredientPostRequest().execute("http://ec2-54-90-187-63.compute-1.amazonaws.com/register")
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void ingredientSearchButtonClick(View v) {
        try {
            new makeIngredientPostRequest().execute("http://food2fork.com/api/search");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private class makePostRequest extends AsyncTask<String, Void, String> {
        String searchQuery;

        @Override
        protected void onPreExecute(){
            searchQuery = etSearchParameter.getText().toString();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params){
            String postParameters = "key=d96e1dc598dd65072cbd1a7a55e07a18" + "&q=" + searchQuery;
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
                JSONArray recipeArray = fullResult.getJSONArray("recipes");
                List<String> items = new ArrayList<>();
                links = new ArrayList<>();


                for(int i = 0; i < recipeArray.length(); i++){
                    items.add(recipeArray.getJSONObject(i).getString("title"));
                    links.add(recipeArray.getJSONObject(i).getString("source_url"));
                }

                mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.activity_profile,R.id.tvIngredients,items);

                lvResult.setAdapter(mArrayAdapter);
            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }

    private class makeIngredientPostRequest extends AsyncTask<String, Void, String> {
        String username;

        @Override
        protected void onPreExecute(){
            SharedPreferences mPrefs = getSharedPreferences("userInfo", 0);
            username = mPrefs.getString("username", "");
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params){

            String ingredients = "";

            String postParameters = "user=" + username + "&count=20";
            byte[] postData = postParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            String response = "";

            try {

                URL url = new URL("http://ec2-54-90-187-63.compute-1.amazonaws.com/ingredient/return");
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

                }
                else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                    String line;

                    while((line=br.readLine()) != null){
                        response += line;
                    }

                }
            }
            catch(IOException e){
                throw new RuntimeException(e);
            }

            try {

                JSONObject fullResult = new JSONObject(response);
                JSONArray resultArray = fullResult.getJSONArray("ingredients");
                String line;

                for(int i = 0; i < resultArray.length(); i++){
                    line = resultArray.getString(i);
                    ingredients += line + ", ";
                }

            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }

            //MIDDLE SECTION

            String APIPostParameters = "key=d96e1dc598dd65072cbd1a7a55e07a18" + "&q=" + ingredients;
            byte[] APIPostData = APIPostParameters.getBytes(StandardCharsets.UTF_8);
            int APIPostDataLength = APIPostData.length;
            try {

                String APIresponse = "";

                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty( "charset", "utf-8");
                urlConnection.setRequestProperty( "Content-Length", Integer.toString( APIPostDataLength ));
                urlConnection.setUseCaches(false);
                try(DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())){
                    wr.write(APIPostData);
                    wr.flush();
                    wr.close();
                }
                int APIResponseCode = urlConnection.getResponseCode();
                if(APIResponseCode == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line = "";

                    while((line=br.readLine()) != null){
                        APIresponse += line;
                    }

                    return APIresponse;
                }
                else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                    String line;

                    while((line=br.readLine()) != null){
                        APIresponse += line;
                    }

                    return APIresponse;
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
                JSONArray recipeArray = fullResult.getJSONArray("recipes");
                List<String> items = new ArrayList<>();
                links = new ArrayList<>();


                for(int i = 0; i < recipeArray.length(); i++){
                    items.add(recipeArray.getJSONObject(i).getString("title"));
                    links.add(recipeArray.getJSONObject(i).getString("source_url"));
                }

                mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.activity_profile,R.id.tvIngredients,items);

                lvResult.setAdapter(mArrayAdapter);
            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }

}
