package edu.cuny.foodproof;


import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MenuActivity extends Activity implements View.OnClickListener{
    private Button fridgeButton;
    private EditText edit;
    private ListView lv;
    ArrayList<String> ar = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        fridgeButton = (Button)findViewById(R.id.button5);
        lv = (ListView) findViewById(R.id.list);
        fridgeButton.setOnClickListener(this);
        edit = (EditText)findViewById(R.id.ingredient);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ar);
        lv.setAdapter(adapter);

    }
    public void onClick(View view) {
        String input = edit.getText().toString();
        if (input.length() > 0) {
            adapter.add(input);
            edit.setText("");
        }
    }
}
