package edu.cuny.foodproof;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
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

    //Variables used to manipulate and work with the views of the Login activity
    TextView bLogin, bRegister;
    EditText etUsername, etPassword;
    TextView tvResults;
    String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Standard onCreate() procedures
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Saving the views from the newly created Login activity into the variables of the class
        tvResults = (TextView) findViewById(R.id.tvResults);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.btLogin);
        bRegister = (TextView) findViewById(R.id.textRegister);

        //Setting OnClickListeners so that the buttons can respond to click events
        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);
    }

    @Override
    public void onBackPressed(){
        //Prevents back press from returning to logged in activity
    }

    //Method gets called whenever a button with an OnClickListener is clicked
    @Override
    public void onClick(View v){
        Username = etUsername.getText().toString();
        SharedPreferences pref = getSharedPreferences("MyPref",MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString("username", Username);
        editor.commit();

        //Switch statement used to check what view is the one being clicked
        switch(v.getId()){

            //Case where Login button is clicked
            case R.id.btLogin:
                try {
                    //Make a POST request to the server using private makePostRequest AsyncTask child class
                    new makePostRequest().execute("http://ec2-54-90-187-63.compute-1.amazonaws.com/login");
                }
                catch(Exception e){
                    throw new RuntimeException(e); //Exception handling
                }
                break;

            //Case where Login button is clicked
            case R.id.textRegister:
                //Open up the Register activity.
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    //AsyncTask child class to make a POST request to a server in a background thread of the Android device
    private class makePostRequest extends AsyncTask<String, Void, String> {

        //Variables used to store the username and password that were inputted into the EditText view
        String inUsername;
        String inPassword;

        @Override
        protected void onPreExecute(){ //Function called immediately when the AsyncTask is invoked through the execute method

            //Storing the inputted username and password into their respective variables
            inUsername = etUsername.getText().toString();
            inPassword = etPassword.getText().toString();
        }

        //Cannot use HttpURLConnection without a version of KitKat or above
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params){ //Function that runs in the background thread where POST request will be made

            //Preparing the parameters to be added to the POST request
            String postParameters = "user=" + inUsername + "&password=" + inPassword;

            //Preparing fields that are required for the POST request to be made with ease
            byte[] postData = postParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            try {

                String response = "";

                //Creating a URL out of the string that is passed into the AsyncTask
                URL url = new URL(params[0]);

                //Creating the HttpURLConnection and setting its various attributes to what is needed
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty( "charset", "utf-8");
                urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                urlConnection.setUseCaches(false);

                //Outputting the data via POST request
                try(DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())){
                    wr.write(postData);
                    wr.flush();
                    wr.close();
                }

                //Getting the response code from the server and storing it in an integer variable
                int responseCode = urlConnection.getResponseCode();

                //Case where response code is 200
                if(responseCode == 200) {

                    //Reading the response string from the server
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;

                    while((line=br.readLine()) != null){
                        response += line;
                    }

                    //Returning the response
                    return response;
                }

                //Otherwise an error occurred
                else{

                    //Reading the error response string from the server
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                    String line;

                    while((line=br.readLine()) != null){
                        response += line;
                    }

                    //Returning the error response
                    return response;
                }
            }
            catch(IOException e){
                throw new RuntimeException(e); //Exception handling
            }
        }

        @Override
        protected void onPostExecute(String result){ //Function that runs after doInBackground() is completed

            try {
                //Converting the response string that was returned from doInBackground() into a JSONObject
                JSONObject resultJSON = new JSONObject(result);

                //Getting the 'Result' attribute of the JSON
                String successJSON = resultJSON.getString("Result");

                //Case where the 'Result' attribute is 'success'
                if(successJSON.equals("succeed")){

                    SharedPreferences mPrefs = getSharedPreferences("userInfo", 0);
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("username", inUsername);
                    editor.apply();

                    //Creating an Intent to start the MainActivity, and inputting the extra that allows the user to stay logged in.
                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    mainIntent.putExtra("loggedIn", "succeeded");

                    //Starting the created activity
                    startActivity(mainIntent);

                    //Completing the AsyncTask call
                    finish();
                }

                //Case where the 'Result' attribute is not 'success'
                else{

                    //Creating an Intent to restart the Login activity and starting it
                    Intent loginIntent = new Intent(getApplicationContext(), Login.class);

                    //Starting the created activity
                    startActivity(loginIntent);

                    //Completing the AsyncTask call
                    finish();
                }
            }
            catch(JSONException e){
                throw new RuntimeException(e); //Exception handling
            }
        }
    }

}
