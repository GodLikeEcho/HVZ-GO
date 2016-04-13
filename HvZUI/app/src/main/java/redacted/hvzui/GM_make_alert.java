package redacted.hvzui;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class GM_make_alert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_make_alert);
        setColor();
    }
    protected class postAlert extends AsyncTask<String, Integer, String>{


        @Override
        protected String doInBackground(String... params) {
            String returnString = "";

            String target = params[0];
            String message = params[1];

            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/postAlert.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestMethod("POST");

                // Prepare the parameters to be passed
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("Faction", target)
                        .appendQueryParameter("Message", message);

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

            if(s.equals("success"))
            {
                EditText post_body_text = (EditText) findViewById(R.id.alert_details);
                post_body_text.setText("");
                Toast.makeText(getBaseContext(), "Alert posted successfully", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Alert posting failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void make_announcement_Click(View v)
    {
        RadioGroup radGroup = (RadioGroup) findViewById(R.id.who);
        int radID = radGroup.getCheckedRadioButtonId();
        String target_group = ((RadioButton) findViewById(radID)).getText().toString();

        EditText post_body_text = (EditText) findViewById(R.id.alert_details);
        String post_body = post_body_text.toString();

        if(target_group.equals("Alert Everyone"))
        {
            (new postAlert()).execute("Everyone", post_body);
        }
        else if(target_group.equals("Alert Humans"))
        {
            (new postAlert()).execute("Human", post_body);
        }
        else if(target_group.equals("Alert Zombies"))
        {
            (new postAlert()).execute("Zombie", post_body);
        }

    }

    public void setColor() {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("ColorBlind", false)) {
            View box1 = this.findViewById(R.id.post_mission);
            box1.setBackgroundColor(0xffffffff);

            View box2 = this.findViewById(R.id.textView3);
            box2.setBackgroundColor(0xffffffff);

            View box3 = this.findViewById(R.id.alert_details);
            box3.setBackgroundColor(0xffffffff);
        }
    }

}
