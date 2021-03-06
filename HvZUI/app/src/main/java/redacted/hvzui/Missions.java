package redacted.hvzui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Missions extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String gettingMissions = prefs.getString("getMissions", "");

        setColor();
        if(gettingMissions.equals("0")) {
            (new getMission()).execute();
        }
    }

    protected class getMission extends AsyncTask<Void, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            ArrayList<String> retval = new ArrayList<String>();

            Log.v("Connecting", "starting connection");
            //connecion code
            try {
                //Connection Parameters
                URL url;
                url = new URL("http://www.hvz-go.com/getHMission.php");
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



            //read the rules text file and display it
            StringBuilder text = new StringBuilder();
            int endTime = 0;

            String line;
            //loop through the file and read the rules
            for (int i = 0; i < strings.size(); i++) {
                /*String regexStr = "^[0-9]*$";

                if(strings.get(i).matches(regexStr))
                {
                    endTime = Integer.valueOf(strings.get(i));
                }
                else{
                    text.append(strings.get(i));
                    text.append('\n');
                }*/
                //endTime = Integer.valueOf(strings.get(2));
                text.append(strings.get(i));
                text.append('\n');
            }

            //set the textviews text to those read from the rules
            TextView output = (TextView) findViewById(R.id.missionBody);
            //TextView Toutput = (TextView) findViewById(R.id.textView4);

            output.setText(text);
            //Toutput.setText(endTime);

        }
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


