package redacted.hvzui;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Z_missions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_missions);

        setColor();
        (new getMission()).execute();
        (new missionTime()).execute();
        StartClock();

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
            preferences = Z_missions.this.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
            final SharedPreferences.Editor edit = preferences.edit();

            //read the rules text file and display it
            StringBuilder text = new StringBuilder();

            //String line;
            //loop through the file and read the rules
            for (int i = 0; i < strings.size(); i++) {
                text.append(strings.get(i));
                text.append('\n');
            }

            //set the textviews text to those read from the rules
            TextView output = (TextView) findViewById(R.id.missionBody);

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
                System.out.println(preferences.getAll());
            }

            output.setText(text);


        }
    }

    public class missionTime extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

            Integer time = 0;
            String times = null;

            Log.v("Connecting", "starting connection");

            //connecion code
            try {
                //Connection Parameters
                URL url;
                url = new URL("http://www.hvz-go.com/getZMissionTime.php");
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
            SharedPreferences preferences;
            preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();

            //set the textviews text to those read from the rules
            TextView output = (TextView) findViewById(R.id.textView4);
            if (time <= 0)
            {
                //commit prefs on change
                edit.putInt("missionTimeLeft", 0);
                edit.commit();
                System.out.println(preferences.getAll());
                output.setText("Mission has ended.");
            }
            else{
                //commit prefs on change
                edit.putInt("missionTimeLeft", time);
                edit.commit();
                System.out.println(preferences.getAll());
            }

            Log.v("Done", "Finished check starve!");
        }
    }

    public void StartClock(){

        //create thread to update every second

        final Thread x = new Thread() {
            // creates an instance of the global preferences
            String PREF_FILE_NAME = "PrefFile";
            final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

            int endTime = preferences.getInt("missionTimeLeft", 0);

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
                                TextView txt = (TextView)findViewById(R.id.textView4);
                                if(endTime >= 86400)
                                {
                                    days = endTime / 86400;
                                    rem = endTime % 86400;
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
                                    else if(endTime >= 60)
                                    {
                                        mins = rem / 60;
                                        rem = rem % 60;
                                        seconds = rem;
                                    }
                                }
                                else if(endTime >= 3600)
                                {
                                    days = 0;
                                    hours = endTime / 3600;
                                    rem = endTime % 3600;
                                    if(rem >= 60)
                                    {
                                        mins = rem / 60;
                                        rem = rem % 60;
                                        seconds = rem;
                                    }
                                }

                                else if(endTime >= 60)
                                {
                                    days = 0;
                                    hours = 0;
                                    mins = endTime / 60;
                                    rem = endTime % 60;
                                    seconds = rem;
                                }
                                else
                                {
                                    days = 0;
                                    hours = 0;
                                    mins = 0;
                                    seconds = endTime;
                                }

                                txt.setText("Mission ends in "+days+':'+hours+':'+mins+':'+seconds);
                                if(endTime <= 0)
                                {
                                    txt.setText("Mission has ended");
                                    return;
                                }
                                --endTime;

                            }
                        });
                    }

                } catch (InterruptedException e) {
                }
            }

        };

        x.start();
    }

    public void setColor()
    {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if(preferences.getBoolean("ColorBlind", false)) {
            View box1 =  this.findViewById(R.id.missionBody);
            box1.setBackgroundColor(0xffffffff);

            View box2 =  this.findViewById(R.id.textView4);
            box2.setBackgroundColor(0xffffffff);

        }
    }
}

