package redacted.hvzui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("LastMessage", "");
        editor.commit();

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        setColor();


    }


    public void goToLoginScreen(View v) {
        Intent intnt = new Intent(this, login.class);
        startActivity(intnt);
    }

    public void signUp_click(View v) {
        Intent intent = new Intent(this, sign_up.class);
        startActivity(intent);
    }

    //button for testing zombie ui without player functionality
    public void zombie_test_click(View v) {
        Intent intent = new Intent(this, Z_user_menu.class);
        startActivity(intent);
    }
    //button for testing game creation ui without player functionality
    public void create_test_click(View v) {
        Intent intent = new Intent(this, GM_game_creation.class);
        startActivity(intent);
    }
    //button for testing GM ui without player functionality
    public void GM_test_click(View v) {
        Intent intent = new Intent(this, GM_user_menu.class);
        startActivity(intent);
    }

    public void setColor() {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("ColorBlind", false)) {
            View box1 = this.findViewById(R.id.Login);
            box1.setBackgroundColor(0xffffffff);

            View box2 = this.findViewById(R.id.SignUp);
            box2.setBackgroundColor(0xffffffff);
        }
    }

}
