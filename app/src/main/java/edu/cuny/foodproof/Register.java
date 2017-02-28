package edu.cuny.foodproof;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;


public class Register extends AppCompatActivity implements View.OnClickListener {

    Button btRegister;
    EditText etName, etAge, etEmail,etGender, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etAge=(EditText) findViewById(R.id.etAge);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etGender = (EditText) findViewById(R.id.etGender);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btRegister.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btRegister:



        }
    }
}
