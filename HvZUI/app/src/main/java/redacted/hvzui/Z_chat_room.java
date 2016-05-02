package redacted.hvzui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by victo on 2/15/2016.
 */
public class Z_chat_room extends AppCompatActivity{
    String url = "http://www.zchat.hvz-go.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_chat_room);

        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        //checks global preferences Color Blind bool
        if(preferences.getBoolean("ColorBlind", false))
            url = "http://www.zchat.hvz-go.com/cblind.html";

        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.clearCache(true);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(url);

    }
}
