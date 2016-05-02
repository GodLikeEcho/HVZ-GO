package redacted.hvzui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class UserMenu extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private AlarmManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserMenu.this);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("LastMessage", "");
        editor.commit();

        final Spinner game_spinner = (Spinner) findViewById(R.id.spinner_game_to_join);

        //set up the listner for when the user selects which game they are in
        game_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserMenu.this);

                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("GameID", game_spinner.getSelectedItem().toString());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        (new PopulateGameSpinner()).execute();

        setColor();
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

    //listner for when the user changes the item selection in the game spinner

    // Async Task override function to run the networking in a new thread
    private class PopulateGameSpinner extends AsyncTask<Void, Integer, ArrayList<String> >
    {
        @Override
        // The function to override
        protected ArrayList<String> doInBackground(Void... params)
        {
            // For returning
            ArrayList<String> res = new ArrayList<String>();

            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/getGames.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                boolean first = true;

                // Log for debugging
                Log.v("prep", "Preparing to connect");

                String returnString = "";

                // Get a response from the server
                int responseCode = conn.getResponseCode();

                // If the response is the one we are looking for
                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    // More logging
                    Log.v("connected", "successful connection, preparing to read");


                    conn.connect();

                    Log.v("read", "Beginning read");

                    // Create a buffered reader object to get the data from the php call
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    // Line to read into
                    String line = "";

                    // As long as there is stuff to read, add each line to the array
                    while((line = in.readLine()) != null)
                    {
                        Log.v("read", line);
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
            //if thre are user names returned, fill the spinner
            if(!strings.isEmpty())
            {
                Spinner spinner = (Spinner) findViewById(R.id.spinner_game_to_join);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserMenu.this, android.R.layout.simple_spinner_item, strings);

                spinner.setAdapter(adapter);
            }
            else
            {
                // If it is invalid, notify the user of this
                Toast.makeText(getBaseContext(), "Could not retrieve game list", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void report_Click(View v)
    {
        Intent intnt = new Intent(this, ModReport.class);
        startActivity(intnt);
    }

    public void missions_Click(View v)
    {
        Intent intnt = new Intent(this, Missions.class);
        startActivity(intnt);
    }

    public void rules_Click(View v)
    {
        Intent intnt = new Intent(this, Rules.class);
        startActivity(intnt);
    }

    public void H_chat_Click(View v)
    {
        Intent intnt = new Intent(this, H_chat_room.class);
        startActivity(intnt);
    }

    public void A_chat_Click(View v)
    {
        Intent intnt = new Intent(this, A_chat_room.class);
        startActivity(intnt);
    }

    public void h_settings_Click(View v)
    {
        Intent intnt = new Intent(this, Z_settings.class);
        startActivity(intnt);
    }

    public void setColor()
    {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if(preferences.getBoolean("ColorBlind", false)) {
            View box1 =  this.findViewById(R.id.TimerBox);
            box1.setBackgroundColor(0xffffffff);

            View box2 =  this.findViewById(R.id.button2);
            box2.setBackgroundColor(0xffffffff);

            View box3 =  this.findViewById(R.id.button3);
            box3.setBackgroundColor(0xffffffff);

            View box4 =  this.findViewById(R.id.Rules);
            box4.setBackgroundColor(0xffffffff);

            View box5 =  this.findViewById(R.id.button);
            box5.setBackgroundColor(0xffffffff);

            View box6 =  this.findViewById(R.id.Moderate);
            box6.setBackgroundColor(0xffffffff);

            View box7 =  this.findViewById(R.id.settings);
            box7.setBackgroundColor(0xffffffff);

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

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserMenu.this);

            String last = prefs.getString("LastMessage", "");

            if (!strings.isEmpty() && !strings.get(1).equals(last)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UserMenu.this);
                //set it's attributes
                builder.setTitle("Alert for"+strings.get(0)+"")
                        .setMessage(""+strings.get(1)+"")
                        .setNeutralButton("Dismiss", null);
                //create an alert box from the builder
                AlertDialog alertDialog = builder.create();
                //and show it
                alertDialog.show();

                prefs = PreferenceManager.getDefaultSharedPreferences(UserMenu.this);

                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("LastMessage", strings.get(1));
                editor.commit();

            }
        }

    }
}
