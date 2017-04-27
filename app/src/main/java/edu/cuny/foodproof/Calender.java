package edu.cuny.foodproof;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by losty on 4/17/2017.
 */
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

import static edu.cuny.foodproof.R.drawable.default_button;

public class Calender extends AppCompatActivity {
    CaldroidFragment caldroidFragment;
    FragmentTransaction t;
    ListView lvDates;
    ArrayAdapter<String> mArrayAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Standard onCreate() procedures
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        lvDates = (ListView) findViewById(R.id.lvDates);

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), date.toString(), Toast.LENGTH_SHORT).show();
                TextView tView = (TextView) view;
                tView.setText("My Recipe");
            }

        });

        new makePostRequest().execute();

        t = getSupportFragmentManager().beginTransaction();
        t.add(R.id.Calender, caldroidFragment).commit();
    }

    private class makePostRequest extends AsyncTask<String, Void, String> {
        String username;

        @Override
        protected void onPreExecute(){
            SharedPreferences mPrefs = getSharedPreferences("userInfo", 0);
            username = mPrefs.getString("username", "");
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params){
            String postParameters = "user=" + username;
            byte[] postData = postParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            try {

                String response = "";
                URL url = new URL("http://ec2-54-90-187-63.compute-1.amazonaws.com/get_recipe");
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
                JSONArray recipesJSON = resultJSON.getJSONArray("Recipes");

                //Case where the 'Result' attribute is 'success'
                if(resultJSON.getString("Result").equals("succeed")){
                    List<String> items = new ArrayList<>();


                    for(int i = 0; i < recipesJSON.length(); i++){

                        long date = recipesJSON.getJSONArray(i).getLong(0);

                        items.add(recipesJSON.getJSONArray(i).getString(1) + " on " + new Date(date * 1000).toString());

                        caldroidFragment.setBackgroundDrawableForDate(getDrawable(R.drawable.default_button), new Date(date * 1000));
                        caldroidFragment.refreshView();
                    }

                    mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.listview_recipes, R.id.tvRecipes,items);

                    lvDates.setAdapter(mArrayAdapter);


                }
            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }

}

