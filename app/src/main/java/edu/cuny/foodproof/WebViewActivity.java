package edu.cuny.foodproof;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
    FloatingActionButton fab_plus, fab_confirm, fab_collab;
    Animation FabOpen, FabClose, FabRClockwise, FabRCClockwise;
    boolean isOpen = false;
    private WebView webView;
    private SharedPreferences sharedpref;
    String url;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpref = getSharedPreferences("Pref", MODE_APPEND);
        url = sharedpref.getString("recipeURL", null);
        setContentView(R.layout.webview);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        fab_plus = (FloatingActionButton)findViewById(R.id.fab_plus);
        fab_confirm = (FloatingActionButton)findViewById(R.id.fab_confirm);
        fab_collab = (FloatingActionButton)findViewById(R.id.fab_collab);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRCClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_counterclockwise);
        fab_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isOpen){
                    fab_collab.startAnimation(FabClose);
                    fab_confirm.startAnimation(FabClose);
                    fab_plus.startAnimation(FabRCClockwise);
                    fab_confirm.setClickable(false);
                    fab_collab.setClickable(false);
                    isOpen = false;
                }
                else{
                    fab_collab.startAnimation(FabOpen);
                    fab_confirm.startAnimation(FabOpen);
                    fab_plus.startAnimation(FabRClockwise);
                    fab_confirm.setClickable(true);
                    fab_collab.setClickable(true);
                    isOpen = true;
                }
            }
        });
        fab_confirm.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent saveRecipeIntent = new Intent(WebViewActivity.this, AddRecipeActivity.class);
                saveRecipeIntent.putExtra("Recipe", getIntent().getStringExtra("Recipe"));
                saveRecipeIntent.putExtra("Private", "true");
                startActivity(saveRecipeIntent);
            }
        });
        fab_collab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent saveRecipeIntent = new Intent(WebViewActivity.this, AddRecipeActivity.class);
                saveRecipeIntent.putExtra("Recipe", getIntent().getStringExtra("Recipe"));
                saveRecipeIntent.putExtra("Private", "false");
                startActivity(saveRecipeIntent);
            }
        });

    }
//
//    public void addRecipe(View v){
//        Intent saveRecipeIntent = new Intent(this, AddRecipeActivity.class);
//        saveRecipeIntent.putExtra("Recipe", getIntent().getStringExtra("Recipe"));
//        saveRecipeIntent.putExtra("Private", "true");
//        startActivity(saveRecipeIntent);
//    }

//    public void shareRecipe(View v){
//
//        Intent saveRecipeIntent = new Intent(this, AddRecipeActivity.class);
//        saveRecipeIntent.putExtra("Recipe", getIntent().getStringExtra("Recipe"));
//        saveRecipeIntent.putExtra("Private", "false");
//        startActivity(saveRecipeIntent);
//    }

}
