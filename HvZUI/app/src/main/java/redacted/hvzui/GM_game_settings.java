package redacted.hvzui;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GM_game_settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_game_settings);
        setColor();

        (new getSettings()).execute("1");
    }

    //changes the status of the gamepaused value
    private class pauseGame extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String returnString = "";

            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/pauseGame.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestMethod("POST");

                // Prepare the parameters to be passed
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("GameID", params[0])
                        .appendQueryParameter("IsPaused", params[1]);


                String query = builder.build().getEncodedQuery();

                // Log for debugging
                Log.v("prep", "Preparing to connect");

                // More logging
                Log.v("connected", "successful connection, preparing to write");

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter wr = new OutputStreamWriter(os, "UTF-8");

                // Write the params and clean up
                wr.write(query);
                wr.flush();
                wr.close();
                os.close();

                conn.connect();

                Log.v("write", "Write finished");

                // Get a response from the server
                int responseCode = conn.getResponseCode();

                // If the response is the one we are looking for
                if (responseCode == HttpURLConnection.HTTP_OK) {


                    Log.v("read", "Beginning read");

                    // Create a buffered reader object to get the data from the php call
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    // Line to read into
                    String line = "";

                    // As long as there is stuff to read, append each line to the returnstring variable
                    while ((line = in.readLine()) != null) {
                        Log.v("read", line);
                        returnString += line;
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
            Log.v("returning", "here");
            return returnString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("pass"))
            {
                Toast.makeText(getBaseContext(), "Game paused", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Could not pause the game", Toast.LENGTH_LONG).show();
            }
        }
    }

    //pushes the changed settings to the database
    private class updateSettings extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params) {

            String returnString = "";

            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/updateSettings.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestMethod("POST");

                // Prepare the parameters to be passed
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("GameID", params[0])
                        .appendQueryParameter("StarveTime", params[1])
                        .appendQueryParameter("StunTime", params[2])
                        .appendQueryParameter("RequiredMissions", params[3]);

                String query = builder.build().getEncodedQuery();

                // Log for debugging
                Log.v("prep", "Preparing to connect");

                // More logging
                Log.v("connected", "successful connection, preparing to write");

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter wr = new OutputStreamWriter(os, "UTF-8");

                // Write the params and clean up
                wr.write(query);
                wr.flush();
                wr.close();
                os.close();

                conn.connect();

                Log.v("write", "Write finished");

                // Get a response from the server
                int responseCode = conn.getResponseCode();

                // If the response is the one we are looking for
                if (responseCode == HttpURLConnection.HTTP_OK) {


                    Log.v("read", "Beginning read");

                    // Create a buffered reader object to get the data from the php call
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    // Line to read into
                    String line = "";

                    // As long as there is stuff to read, append each line to the returnstring variable
                    while ((line = in.readLine()) != null) {
                        Log.v("read", line);
                        returnString += line;
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
            Log.v("returning", "here");
            return returnString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("pass"))
            {
                Toast.makeText(getBaseContext(), "Settings updated", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Could not update settings", Toast.LENGTH_LONG).show();
            }

        }
    }

    // Async Task override function to run the networking in a new thread
    private class getSettings extends AsyncTask<String, Integer, ArrayList<String>>
    {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            // For returning
            ArrayList<String> returnString = new ArrayList<String>();


            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/getSettings.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestMethod("POST");

                // Prepare the parameters to be passed
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("GameID", params[0]);

                String query = builder.build().getEncodedQuery();

                // Log for debugging
                Log.v("prep", "Preparing to connect");

                // More logging
                Log.v("connected", "successful connection, preparing to write");

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter wr = new OutputStreamWriter(os, "UTF-8");

                // Write the params and clean up
                wr.write(query);
                wr.flush();
                wr.close();
                os.close();

                conn.connect();

                Log.v("write", "Write finished");

                // Get a response from the server
                int responseCode = conn.getResponseCode();

                // If the response is the one we are looking for
                if (responseCode == HttpURLConnection.HTTP_OK) {


                    Log.v("read", "Beginning read");

                    // Create a buffered reader object to get the data from the php call
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    // Line to read into
                    String line = "";

                    // As long as there is stuff to read, append each line to the returnstring variable
                    while ((line = in.readLine()) != null) {
                        Log.v("read", line);
                        returnString.add(line);
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
            Log.v("returning", "here");
            return returnString;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            if(!strings.isEmpty())
            {
                EditText timeperkill = (EditText) findViewById(R.id.time_per_kill);
                EditText stuntime = (EditText) findViewById(R.id.stun_timer_duration);
                EditText mandatorymissions = (EditText) findViewById(R.id.number_of_required_missions);
                Button paused = (Button) findViewById(R.id.pause_game);

                timeperkill.setText(strings.get(0));
                stuntime.setText(strings.get(1));
                mandatorymissions.setText(strings.get(2));

                if(strings.get(3).equals("1"))
                {
                    paused.setText("Resume Game");
                }
                else if(strings.get(3).equals("0"))
                {
                    paused.setText("Pause Game");
                }
            }
            else
            {
                // If it is invalid, notify the user of this
                Toast.makeText(getBaseContext(), "Could not retrieve settings", Toast.LENGTH_LONG).show();
            }



        }
    }

    public void update_settings_click(View v)
    {
        //get the text boxes containing the settings
        EditText timeperkill = (EditText) findViewById(R.id.time_per_kill);
        EditText stuntime = (EditText) findViewById(R.id.stun_timer_duration);
        EditText mandatorymissions = (EditText) findViewById(R.id.number_of_required_missions);

        //extract the strings from them
        String time_added_per = timeperkill.getText().toString();
        String stun_time = stuntime.getText().toString();
        String required_attendance = mandatorymissions.getText().toString();

        (new updateSettings()).execute("1", time_added_per, stun_time, required_attendance);
    }

    public void pause_game_click(View v)
    {
        Button paused = (Button) findViewById(R.id.pause_game);

        if(paused.getText().equals("Resume Game"))
        {
            (new pauseGame()).execute("1", "0");
        }
        else if(paused.getText().equals("Pause Game"))
        {
            (new pauseGame()).execute("1", "1");
        }
    }

    public void setColor() {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("ColorBlind", false)) {
            View box1 = this.findViewById(R.id.label1);
            box1.setBackgroundColor(0xffffffff);

            View box2 = this.findViewById(R.id.time_per_kill);
            box2.setBackgroundColor(0xffffffff);

            View box3 = this.findViewById(R.id.label2);
            box3.setBackgroundColor(0xffffffff);

            View box4 = this.findViewById(R.id.stun_timer_duration);
            box4.setBackgroundColor(0xffffffff);

            View box5 = this.findViewById(R.id.label3);
            box5.setBackgroundColor(0xffffffff);

            View box6 = this.findViewById(R.id.number_of_required_missions);
            box6.setBackgroundColor(0xffffffff);

            View box7 = this.findViewById(R.id.update_settings);
            box7.setBackgroundColor(0xffffffff);

        }
    }
}
