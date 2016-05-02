package redacted.hvzui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_settings);
        Switch color = (Switch) findViewById(R.id.ColorB);

        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor edit = preferences.edit();

        color.setChecked(preferences.getBoolean("ColorBlind", false));

        color.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //commit prefs on change
                edit.putBoolean("ColorBlind", isChecked);
                edit.commit();
                System.out.println(preferences.getAll());
                Toast.makeText(getBaseContext(), "Please restart app to see changes.", Toast.LENGTH_LONG).show();
            }

        });

        final Spinner game_spinner = (Spinner) findViewById(R.id.spinner_game_to_join);

        //set up the listner for when the user selects which game they are in
        game_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(settings.this);

                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("GameID", game_spinner.getSelectedItem().toString());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        (new PopulateGameSpinner()).execute();


        if(preferences.getBoolean("ColorBlind", false) == true)
            color.setChecked(true);
        else color.setChecked(false);

    }

    // Async Task override function to run the networking in a new thread
    private class PopulateGameSpinner extends AsyncTask<Void, Integer, ArrayList<String> >
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
                url = new URL( "http://www.hvz-go.com/getGames.php" );
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
                Spinner spinner = (Spinner) findViewById(R.id.spinner_game_to_join);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(settings.this, android.R.layout.simple_spinner_item, strings);

                spinner.setAdapter(adapter);
            }
            else
            {
                // If it is invalid, notify the user of this
                Toast.makeText(getBaseContext(), "Could not retrieve game list", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Z_logout_click(View v)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure you want to log out?");
        builder1.setCancelable(true);

        final Intent intnt = new Intent(this, MainMenu.class);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(intnt);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
