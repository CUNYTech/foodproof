package edu.cuny.foodproof;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;

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
}
