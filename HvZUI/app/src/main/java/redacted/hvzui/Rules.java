package redacted.hvzui;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Rules extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        setColor();
        (new getRules()).execute();
    }

    // Async Task override function to run the networking in a new thread
    private class getRules extends AsyncTask<Void, Integer, ArrayList<String>> {
        @Override
        // The function to override
        protected ArrayList<String> doInBackground(Void... params) {
            // For returning
            ArrayList<String> res = new ArrayList<String>();

            try {
                //Connection Parameters
                URL url;
                url = new URL("http://www.hvz-go.com/getRules.php");
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

            if (!strings.isEmpty()) {
                TextView rulesbox = (TextView) findViewById(R.id.Hrules);


                for (int i = 0; i < strings.size(); i++) {
                    rulesbox.append(strings.get(i));
                    rulesbox.append("\n");
                }

            } else {
                // If it is invalid, notify the user of this
                Toast.makeText(getBaseContext(), "Could not retrieve rules", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void setColor() {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("ColorBlind", false)) {
            View box1 = this.findViewById(R.id.Hrules);
            box1.setBackgroundColor(0xffffffff);

        }
    }
}
