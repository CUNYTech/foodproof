package edu.cuny.foodproof;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanuor.onyx.Onyx;
import com.hanuor.onyx.hub.OnTaskCompletion;
import com.hanuor.pearl.Pearl;

import java.util.ArrayList;

public class WhatisIt extends AppCompatActivity {

        ImageView imageView;
        Button btn;
        TextView tv;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_whatisit);
            btn = (Button) findViewById(R.id.button);
            imageView = (ImageView) findViewById(R.id.ivy);
            tv = (TextView) findViewById(R.id.textView);
            final ProgressDialog pd = new ProgressDialog(WhatisIt.this);
            final String mImage = "http://lorempixel.com/600/300/food";
            Pearl.imageLoader(WhatisIt.this,mImage,imageView,0);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pd.setMessage("Loading...");
                    pd.show();
                    Onyx.with(WhatisIt.this).fromURL(mImage).getTagsfromApi(new OnTaskCompletion() {
                        @Override
                        public void onComplete(ArrayList<String> response) {
                            Log.d("Class",""+response);
                            pd.dismiss();
                            tv.setText(response.toString());
                            tv.setTextColor(Color.parseColor("#ffffff"));

                        }
                    });
                }
            });

        }
    }