package edu.cuny.foodproof;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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

}
