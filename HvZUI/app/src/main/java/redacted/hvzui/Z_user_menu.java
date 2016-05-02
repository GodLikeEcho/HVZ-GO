package redacted.hvzui;

import android.app.AlertDialog;
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

public class Z_user_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_user_menu);

        // Retrieve a PendingIntent that will perform a broadcast
        //Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        //startAlarm();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Z_user_menu.this);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("LastMessage", "");
        editor.commit();

        setColor();
        contCheck();

    }

    TimerTask scanTask;
    Timer t = new Timer();

    public void contCheck(){
        scanTask = new TimerTask() {
            @Override
            public void run() {
                (new getMission()).execute();
                (new starveTimer()).execute();
                SetStarve();
                //(new getAlert()).execute();
            }
        };
        t.schedule(scanTask, 1000, 600000);
    }

    public void SetStarve(){

        //create thread to update every second

        final Thread x = new Thread() {
            // creates an instance of the global preferences
            String PREF_FILE_NAME = "PrefFile";
            final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

            int starvetime = preferences.getInt("StarveTime", 0);

            @Override
            //function to run every thread tick

            public void run() {
                try {
                    while (!isInterrupted()) {
                        //can change this to change the thread inverval
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {

                            //actual statement to do an action every tick
                            int days = 0, hours = 0, mins = 0, seconds = 0, rem = 0;
                            public void run() {
                                TextView txt = (TextView)findViewById(R.id.StarveBox);
                                if(starvetime >= 86400)
                                {
                                    days = starvetime / 86400;
                                    rem = starvetime % 86400;
                                    if(rem >= 3600)
                                    {
                                        hours = rem / 3600;
                                        rem = rem % 3600;
                                        if(rem >= 60)
                                        {
                                            mins = rem / 60;
                                            rem = rem % 60;
                                            seconds = rem;
                                        }
                                    }
                                    else if(starvetime >= 60)
                                    {
                                        mins = rem / 60;
                                        rem = rem % 60;
                                        seconds = rem;
                                    }
                                }
                                else if(starvetime >= 3600)
                                {
                                    days = 0;
                                    hours = starvetime / 3600;
                                    rem = starvetime % 3600;
                                    if(rem >= 60)
                                    {
                                        mins = rem / 60;
                                        rem = rem % 60;
                                        seconds = rem;
                                    }
                                }

                                else if(starvetime >= 60)
                                {
                                    days = 0;
                                    hours = 0;
                                    mins = starvetime / 60;
                                    rem = starvetime % 60;
                                   seconds = rem;
                                }
                                else
                                {
                                    days = 0;
                                    hours = 0;
                                    mins = 0;
                                    seconds = starvetime;
                                }

                                txt.setText("Starve in "+days+':'+hours+':'+mins+':'+seconds);
                                if(starvetime <= 0)
                                {
                                    txt.setText("You have starved.");
                                    return;
                                }
                                --starvetime;

                            }
                        });
                    }

                } catch (InterruptedException e) {
                }
            }

        };

        x.start();
    }

    public void beginTimer(View v) {

        //create thread to update every second

        final Thread j = new Thread() {

            int stuntimer = 5;

            @Override
            //function to run every thread tick
            public void run() {
                try {
                    while (!isInterrupted()) {
                        //can change this to change the thread inverval
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {

                            //actual statement to do an action every tick
                            public void run() {
                                TextView txt = (TextView)findViewById(R.id.TimerBox);
                                txt.setText("Stunned for "+stuntimer+" seconds");
                                if(stuntimer <= 0)
                                {
                                    txt.setText("");
                                    return;
                                }
                                --stuntimer;

                            }
                        });
                    }

                } catch (InterruptedException e) {
                }
            }

        };

        j.start();
    }


    public class starveTimer extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

            //ArrayList<String> retval = new ArrayList<String>();
            Integer time = 0;
            String times = null;

            Log.v("Connecting", "starting connection");
            //connecion code
            try {
                //Connection Parameters
                URL url;
                url = new URL("http://www.hvz-go.com/getStarveTime.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.v("Connect", "recived response");
                    conn.connect();
                    Log.v("Connected", "suceeded");

                    BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    Log.v("reading", "starting read");

                    times = input.readLine();
                    time = Integer.parseInt(times);
                    /*while ((line = input.readLine()) != null) {
                        Log.v("reading", "");
                        retval.add(line);
                        Log.v("read", line);
                    }*/

                   //time = input.read();
                    Log.v("Connected. read time is", times);

                    input.close();
                    Log.v("read", "read finish");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return time;
        }
        @Override
        protected void onPostExecute(Integer time) {
            super.onPostExecute(time);

            //creates an instance of the global preferences
            String PREF_FILE_NAME = "PrefFile";
            final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
            final SharedPreferences.Editor edit = preferences.edit();

            edit.putInt("StarveTime", time);
            edit.commit();

            if (time < 86400)
            {
                edit.putBoolean("StarveAlert", true);
                edit.commit();
            }
            else{
                edit.putBoolean("StarveAlert", false);
                edit.commit();
            }

            System.out.println(preferences.getBoolean("starveAlert", false));
            System.out.println(preferences.getInt("StarveTime", 0));
            System.out.println(preferences.getAll());
            Log.v("Done", "Finished check starve!");
            //output.setText(text);


        }
    }

    public class getMission extends AsyncTask<Void, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            ArrayList<String> retval = new ArrayList<String>();

            Log.v("Connecting", "starting connection");
            //connecion code
            try {
                //Connection Parameters
                URL url;
                url = new URL("http://www.hvz-go.com/getZMission.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.v("Connect", "recived response");
                    conn.connect();
                    Log.v("Connected", "suceeded");

                    BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String line;
                    Log.v("reading", "starting read");

                    while ((line = input.readLine()) != null) {
                        Log.v("reading","");
                        retval.add(line);
                        Log.v("read", line);
                    }

                    input.close();
                    Log.v("read", "read finish");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return retval;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            //creates an instance of the global preferences
            String PREF_FILE_NAME = "PrefFile";
            final SharedPreferences preferences;
            preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
            final SharedPreferences.Editor edit = preferences.edit();

            //read the rules text file and display it
            StringBuilder text = new StringBuilder();

            //String line;
            //loop through the file and read the rules
            for (int i = 0; i < strings.size(); i++) {
                text.append(strings.get(i));
                text.append('\n');
            }

            String old = preferences.getString("mission", " ");
            if( old.equals(text.toString()))
            {
                //commit prefs on change
                edit.putBoolean("missionUpdate", false);
                edit.commit();
                System.out.println(preferences.getAll());
            }
            else
            {
                //commit prefs on change
                edit.putBoolean("missionUpdate", true);
                edit.putString("mission", text.toString());
                edit.commit();
                Toast.makeText(getApplicationContext(), "There in a new Mission.", Toast.LENGTH_SHORT).show();
                System.out.println(preferences.getAll());
            }
        }
    }

    public void Z_report_Click(View v)
    {
        Intent intnt = new Intent(this, Z_mod_report.class);
        startActivity(intnt);
    }

    public void Z_missions_Click(View v)
    {
        Intent intnt = new Intent(this, Z_missions.class);
        startActivity(intnt);
    }

    public void Z_rules_Click(View v)
    {
        Intent intnt = new Intent(this, Z_rules.class);
        startActivity(intnt);
    }

    public void Z_tag_click(View v)
    {
        Intent intnt = new Intent(this, Z_tag_report.class);
        startActivity(intnt);
    }

    public void Z_chat_Click(View v)
    {
        Intent intnt = new Intent(this, Z_chat_room.class);
        startActivity(intnt);
    }

    public void A_chat_Click(View v)
    {
        Intent intnt = new Intent(this, A_chat_room.class);
        startActivity(intnt);
    }

    public void z_settings_Click(View v)
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

            View box2 =  this.findViewById(R.id.StunTimer);
            box2.setBackgroundColor(0xffffffff);

            View box3 =  this.findViewById(R.id.Rules);
            box3.setBackgroundColor(0xffffffff);

            View box4 =  this.findViewById(R.id.reportTag);
            box4.setBackgroundColor(0xffffffff);

            View box5 =  this.findViewById(R.id.Chat);
            box5.setBackgroundColor(0xffffffff);

            View box6 =  this.findViewById(R.id.Mission);
            box6.setBackgroundColor(0xffffffff);

            View box7 =  this.findViewById(R.id.button4);
            box7.setBackgroundColor(0xffffffff);

            View box8 =  this.findViewById(R.id.settingsbutton);
            box8.setBackgroundColor(0xffffffff);

            View box9 =  this.findViewById(R.id.Moderate);
            box9.setBackgroundColor(0xffffffff);

            View box10 =  this.findViewById(R.id.StarveBox);
            box10.setBackgroundColor(0xffffffff);
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
                        .appendQueryParameter("Faction", "Zombie");

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

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Z_user_menu.this);

            String last = prefs.getString("LastMessage", "");

            if (!strings.isEmpty() && !strings.get(1).equals(last)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Z_user_menu.this);
                //set it's attributes
                builder.setTitle("Alert for"+strings.get(0)+"")
                        .setMessage(""+strings.get(1)+"")
                        .setNeutralButton("Dismiss", null);
                //create an alert box from the builder
                AlertDialog alertDialog = builder.create();
                //and show it
                alertDialog.show();

                prefs = PreferenceManager.getDefaultSharedPreferences(Z_user_menu.this);

                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("LastMessage", strings.get(1));
                editor.commit();
            }
        }

    }
}
