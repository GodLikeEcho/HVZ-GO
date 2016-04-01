package redacted.hvzui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Main class to create a webView
 */
public class A_chat_room extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // pulls the all chat layout from the layouts folder
        setContentView(R.layout.activity_a_chat_room);

        // declaires a webView variable named myWebView and sets it to the id from
        // the webview window created in the layout
        WebView myWebView = (WebView) findViewById(R.id.webView3);
        // cleares any residual data from the last use.
        myWebView.clearCache(true);
        // pulls default behavior settings for the webview
        WebSettings webSettings = myWebView.getSettings();
        // enables java script to run in the web page
        webSettings.setJavaScriptEnabled(true);
        // loads the desired web page
        myWebView.loadUrl("http://www.achat.hvz-go.com/");

    }
}
