package edu.cuny.foodproof;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

    private WebView webView;
    private SharedPreferences sharedpref;
    String url;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpref = getSharedPreferences("Pref", MODE_APPEND);
        url = sharedpref.getString("recipeURL", null);
        setContentView(R.layout.webview);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    public void addRecipe(View v){
        Intent saveRecipeIntent = new Intent(this, AddRecipeActivity.class);
        saveRecipeIntent.putExtra("Recipe", getIntent().getStringExtra("Recipe"));
        saveRecipeIntent.putExtra("Private", "true");
        startActivity(saveRecipeIntent);
    }

    public void shareRecipe(View v){

        Intent saveRecipeIntent = new Intent(this, AddRecipeActivity.class);
        saveRecipeIntent.putExtra("Recipe", getIntent().getStringExtra("Recipe"));
        saveRecipeIntent.putExtra("Private", "false");
        startActivity(saveRecipeIntent);
    }

}