package edu.cuny.foodproof;

import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class Profile extends AppCompatActivity {
    TextView tvUsername;
    TextView tvIngredients;
    TextView tvCoords;
    ListView lvIngredients;
    ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_right, R.anim.fade_out);
        setContentView(R.layout.activity_profile);

        tvUsername = (TextView) findViewById(R.id.tvUsername);

        tvIngredients = (TextView) findViewById(R.id.tvIngredients);

        tvCoords = (TextView) findViewById(R.id.tvCoords);

        lvIngredients = (ListView) findViewById(R.id.lvIngredients);

        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                new makeDeletePostRequest().execute(mArrayAdapter.getItem(position), "http://ec2-54-90-187-63.compute-1.amazonaws.com/ingredient/delete");
                mArrayAdapter.remove(mArrayAdapter.getItem(position));
                mArrayAdapter.notifyDataSetChanged();
            }

        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);

        if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            String longitude = "Longitude: " + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude() + "\n";
            String finalCoords = longitude + "Latitude: " + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            tvCoords.setText(finalCoords);
        }


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
                List<String> items = new ArrayList<>();

                for(int i = 0; i < resultArray.length(); i++){
                    items.add(resultArray.getString(i));
                }

                mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.listview_ingredients, R.id.tvIngredients2,items);


                lvIngredients.setAdapter(mArrayAdapter);
            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }

    private class makeDeletePostRequest extends AsyncTask<String, Void, String> {
        String username;

        @Override
        protected void onPreExecute(){
            SharedPreferences mPrefs = getSharedPreferences("userInfo", 0);
            username = mPrefs.getString("username", "");
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params){
            String postParameters = "user=" + username + "&ingredient=" + params[0];
            byte[] postData = postParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            try {

                String response = "";

                URL url = new URL(params[1]);
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
            //try {

                /*
                JSONObject fullResult = new JSONObject(result);
                JSONArray resultArray = fullResult.getJSONArray("ingredients");
                List<String> items = new ArrayList<>();

                for(int i = 0; i < resultArray.length(); i++){
                    items.add(resultArray.getString(i));
                }

                ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.activity_profile,R.id.tvIngredients,items);

                lvIngredients.setAdapter(mArrayAdapter);

            }
            catch(JSONException e){
                throw new RuntimeException(e);
                */
            //}

        }

    }

    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            //editLocation.setText("");
            //pb.setVisibility(View.INVISIBLE);
            String longitude = "Longitude: " + loc.getLongitude() + "\n";
            //Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            //Log.v(TAG, latitude);
            tvCoords.setText(longitude + latitude);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

}
