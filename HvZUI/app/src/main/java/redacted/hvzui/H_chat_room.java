package redacted.hvzui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by victo on 2/15/2016.
 */
public class H_chat_room extends AppCompatActivity{
    String url = "http://www.hchat.hvz-go.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_chat_room);

        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        //checks global preferences Color Blind bool
        if(preferences.getBoolean("ColorBlind", false))
            url = "http://www.hchat.hvz-go.com/cblind.html";

        WebView myWebView = (WebView) findViewById(R.id.webView2);
        myWebView.clearCache(true);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(url);

    }
}