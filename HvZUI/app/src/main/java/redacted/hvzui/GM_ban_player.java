package redacted.hvzui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class GM_ban_player extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_ban_player);

        (new PopulateUserSpinner()).execute();
    }

    // Async Task override function to run the networking in a new thread
    private class PopulateUserSpinner extends AsyncTask<Void, Integer, ArrayList<String> >
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
                url = new URL( "http://www.hvz-go.com/getPlayers.php" );
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
                Spinner spinner = (Spinner) findViewById(R.id.spinner_user_to_ban);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(GM_ban_player.this, android.R.layout.simple_spinner_item, strings);

                spinner.setAdapter(adapter);
            }
            else
            {
                // If it is invalid, notify the user of this
                Toast.makeText(getBaseContext(), "Could not retrieve user list", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Async Task override function to run the networking in a new thread
    private class BanPlayer extends AsyncTask<String, Integer, Void>
    {
        @Override
        // The function to override
        protected Void doInBackground(String... params)
        {
            Log.v("connecting","");
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
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("reason", params[1])
                        .appendQueryParameter("till", params[2]);

                Log.v("user", params[0]);
                Log.v("reason", params[1]);
                Log.v("time", params[2]);

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

    public void ban_click(View v)
    {
        //get input boxes
        EditText bannedTill = (EditText) findViewById(R.id.spinner_banned_till);
        Spinner username_spnr = (Spinner) findViewById(R.id.spinner_user_to_ban);
        Spinner banReason_spnr = (Spinner) findViewById(R.id.spinner_ban_reason);

        //get the information from them and convert where needed
        int banTill = Integer.valueOf(bannedTill.getText().toString());
        String userBanned = username_spnr.getSelectedItem().toString();
        String reasonBanned = banReason_spnr.getSelectedItem().toString();
        String passTill = Integer.toString(banTill);

        //validate ban time
        if (banTill > 0)
        {
            BanPlayer ban = new BanPlayer();
            //ban the player
            ban.execute(userBanned, reasonBanned, passTill);

            //notify the mod that a player has been banned
            //create a new dialog box builder
            AlertDialog.Builder builder = new AlertDialog.Builder(GM_ban_player.this);
            //set it's attributes
            builder.setTitle("Player Banned")
                    .setMessage(""+userBanned+" has been banned for "+banTill+" days for reason "+ reasonBanned+"")
                    .setNeutralButton("OK", null);
            //create an alert box from the builder
            AlertDialog alertDialog = builder.create();
            //and show it
            alertDialog.show();
            //reset the form
            bannedTill.setText("");

            //restock the spinner so that the ban is reflected
            (new PopulateUserSpinner()).execute();
        }
        else
        {
            //if the time of ban is invlaid, notify the mod of that fact
            Toast.makeText(getBaseContext(), "Time banned must be a positive number", Toast.LENGTH_LONG).show();
            //reset the form
            bannedTill.setText("");
        }
    }
}
