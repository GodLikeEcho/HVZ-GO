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
        contCheck();

    }

    TimerTask scanTask;
    Timer t = new Timer();

    public void contCheck(){
        scanTask = new TimerTask() {
            @Override
            public void run() {
                (new getAlert()).execute();
            }
        };
        t.schedule(scanTask, 1000, 600000);
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

    private class getAlert extends AsyncTask<Void, Integer, ArrayList<String> > {
        @Override
        // The function to override
        protected ArrayList<String> doInBackground(Void... params) {
            // For returning
            ArrayList<String> res = new ArrayList<String>();

            try {
                //Connection Parameters
                URL url;
                url = new URL("http://www.hvz-go.com/getAlert.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");


                boolean first = true;

                // Log for debugging
                Log.v("prep", "Preparing to connect");

                String returnString = "";

                // Prepare the parameters to be passed
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("Faction", "Human");

                String query = builder.build().getEncodedQuery();

                StringBuilder result = new StringBuilder();

                // Log for debugging
                Log.v("prep", "Preparing to connect");

                Log.v("connected", "successful connection, preparing to write");

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter wr = new OutputStreamWriter(os, "UTF-8");

                // Write the params and clean up
                wr.write(query);
                wr.flush();
                wr.close();
                os.close();


                // Get a response from the server
                int responseCode = conn.getResponseCode();

                // If the response is the one we are looking for
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // More logging
                    Log.v("connected", "successful connection, preparing to read");


                    conn.connect();

                    Log.v("read", "Beginning read");

                    // Create a buffered reader object to get the data from the php call
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    // Line to read into
                    String line = "";

                    // As long as there is stuff to read, add each line to the array
                    while ((line = in.readLine()) != null) {
                        res.add(line);
                    }

                    //close our reader
                    in.close();


                    Log.v("read", "Read complete");

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);

            String last = prefs.getString("LastMessage", "");

            if (!strings.isEmpty() && !strings.get(1).equals(last)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                //set it's attributes
                builder.setTitle("Alert for"+strings.get(0)+"")
                        .setMessage(""+strings.get(1)+"")
                        .setNeutralButton("Dismiss", null);
                //create an alert box from the builder
                AlertDialog alertDialog = builder.create();
                //and show it
                alertDialog.show();

                prefs = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);

                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("LastMessage", strings.get(1));
                editor.commit();

            }
        }

    }
}
