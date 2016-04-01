package redacted.hvzui;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        (new getMission()).execute();

    }

    private class getMission extends AsyncTask<Void, Integer, ArrayList<String>> {

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

            //read the rules text file and display it
            StringBuilder text = new StringBuilder();

            String line;
            //loop through the file and read the rules
            for (int i = 0; i < strings.size(); i++) {
                text.append(strings.get(i));
                text.append('\n');
            }

            //set the textviews text to those read from the rules
            TextView output = (TextView) findViewById(R.id.missionBody);

            output.setText(text);

        }
    }
}

