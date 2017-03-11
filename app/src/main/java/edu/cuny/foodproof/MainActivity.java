package edu.cuny.foodproof;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent mainIntent = getIntent();
        if(!(mainIntent.hasExtra("loggedIn"))) {
            startActivity(new Intent(this, Login.class));
        }
        else if(!(mainIntent.getStringExtra("loggedIn").equals("succeeded"))){
            startActivity(new Intent(this, Login.class));
        }
    }
    public void buttonClick(View v)
    {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
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
                return true;
            case R.id.menu_logout:
                // Log out was selected
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}