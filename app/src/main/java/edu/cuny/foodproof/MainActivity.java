package edu.cuny.foodproof;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.view.Menu;
import android.content.SharedPreferences;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // slides activity right and fades out
        overridePendingTransition(R.anim.slide_right, R.anim.fade_out);

        setContentView(R.layout.activity_main);
        Intent mainIntent = getIntent();
        if (!(mainIntent.hasExtra("loggedIn"))) {
            startActivity(new Intent(this, Login.class));
        } else if (!(mainIntent.getStringExtra("loggedIn").equals("succeeded"))) {
            startActivity(new Intent(this, Login.class));
        }
    }



    public void buttonClick(View v) {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void searchClick(View v) {
        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(searchIntent);
    }

    public void whatisClick(View v) {
        Intent whatisIntent = new Intent(MainActivity.this, WhatisIt.class);
        startActivity(whatisIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                // Edit Profile was selected
                Intent profileIntent = new Intent(this, Profile.class);
                startActivity(profileIntent);
                return true;

            case R.id.menu_logout:
                // Log out was selected
                Intent logOutIntent = new Intent(this, MainActivity.class);
                startActivity(logOutIntent);
                return true;

            case R.id.menu_calender:
                // Calender was selected
                Intent menu_Calender = new Intent(this, Calender.class);
                startActivity(menu_Calender);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void findARecipe(View view) {
        Intent findARecipeIntent = new Intent(this, WebViewActivity.class);
        startActivity(findARecipeIntent);
        SharedPreferences sharedpref = getSharedPreferences("Pref", MODE_APPEND);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString("recipeURL", "https://www.randomlists.com/random-recipes");
        editor.commit();

    }
    public void findIngredients (View view){
        Intent findIngredients = new Intent (this, MapsActivity.class);
        startActivity(findIngredients);
    }


    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


}



