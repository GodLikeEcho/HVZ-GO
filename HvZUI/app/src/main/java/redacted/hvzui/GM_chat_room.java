package redacted.hvzui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by Clint on 4/17/2016.
 */
public class GM_chat_room extends AppCompatActivity {
    String url = "http://www.hvz-go.com/mchat";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_chat_room);

        WebView myWebView = (WebView) findViewById(R.id.webView4);
        myWebView.clearCache(true);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(url);

    }
}
