package redacted.hvzui;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class GM_choose_OZ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_choose__oz);
        (new PopulateUserSpinner()).execute();
    }

    // Async Task override function to run the networking in a new thread
    private class PopulateUserSpinner extends AsyncTask<Void, Integer, ArrayList<String>> {
        @Override
        // The function to override
        protected ArrayList<String> doInBackground(Void... params) {
            // For returning
            ArrayList<String> res = new ArrayList<String>();

            try {
                //Connection Parameters
                URL url;
                url = new URL("http://www.hvz-go.com/getPlayers.php");
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
            //if thre are user names returned, fill the spinner
            if (!strings.isEmpty()) {
                Spinner spinner = (Spinner) findViewById(R.id.spinner_user_to_ban);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(GM_choose_OZ.this, android.R.layout.simple_spinner_item, strings);

                spinner.setAdapter(adapter);
            } else {
                // If it is invalid, notify the user of this
                Toast.makeText(getBaseContext(), "Could not retrieve user list", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Async Task override function to run the networking in a new thread
    private class InfectOriginalZombie extends AsyncTask<String, Integer, Void>
    {
        @Override
        // The function to override
        protected Void doInBackground(String... params)
        {
            // For returning
            boolean res = false;

            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/banPlayer.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                // Prepare the parameters to be passed
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0]);

                String query = builder.build().getEncodedQuery();

                StringBuilder result = new StringBuilder();
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
                    Log.v("connected", "successful connection, preparing to write");

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    // Write the params and clean up
                    wr.write(query);
                    wr.flush();
                    wr.close();
                    os.close();

                    conn.connect();

                    Log.v("write", "Write finished");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void select_zero_click()
    {
        Spinner user_to_infect_spinner = (Spinner) findViewById(R.id.spinner_first_zombie);

        String original_zombie = user_to_infect_spinner.getSelectedItem().toString();

        (new InfectOriginalZombie()).execute(original_zombie);

        AlertDialog.Builder builder = new AlertDialog.Builder(GM_choose_OZ.this);
        //set it's attributes
        builder.setTitle("Original Zombie Created")
                .setMessage("" + original_zombie + " has been chosen as the original zombie, " +
                        "it is your duty as a moderator to make sure they understand their role in the game")
                .setNeutralButton("OK", null);
        //create an alert box from the builder
        AlertDialog alertDialog = builder.create();
        //and show it
        alertDialog.show();
    }

}
