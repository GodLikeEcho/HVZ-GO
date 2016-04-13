package redacted.hvzui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Z_user_menu extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_user_menu);

        // Retrieve a PendingIntent that will perform a broadcast
        //Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        //startAlarm();
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
            }
        };
        t.schedule(scanTask,1000, 600000);
    }

    public void startAlarm() {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 4000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        Log.v("startAlarm", "In alarm starter");
    }

    public void beginTimer(View v){

        //create thread to update every second

        final Thread t = new Thread() {

            int stuntimer = 5;
            @Override
            //function to run every thread tick
            public void run() {
                try {
                    while (!isInterrupted()) {
                        //can change this to change the thread inverval
                        Thread.sleep(1000);
                        if(stuntimer == 0)
                        {
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            //actual statement to do an action every tick
                            public void run() {
                                TextView txt = (TextView)findViewById(R.id.TimerBox);
                                txt.setText(""+stuntimer+" seconds");
                                --stuntimer;

                            }
                        });
                    }

                } catch (InterruptedException e) {
                }
            }

        };

        t.start();


    }

    public class starveTimer extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

            //ArrayList<String> retval = new ArrayList<String>();
            Integer time = 0;

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

                    /*while ((line = input.readLine()) != null) {
                        Log.v("reading", "");
                        retval.add(line);
                        Log.v("read", line);
                    }*/

                   time = input.read();

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
            //boolean found = false;

            //set the textviews text to those read from the rules
            //TextView output = (TextView) findViewById(R.id.missionBody);

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

            //output.setText(text);


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
        }
    }
}
