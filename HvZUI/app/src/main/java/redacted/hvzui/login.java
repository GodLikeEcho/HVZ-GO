package redacted.hvzui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public String returned;

    // Async Task override function to run the networking in a new thread
    private class verifyLogin extends AsyncTask<String, Integer, Boolean>
    {
        @Override
        // The function to override
        protected Boolean doInBackground(String... params)
        {
            // For returning
            boolean res = false;

            String user = params[0];
            String pass = params[1];

            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/appLogin.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestMethod("POST");

                // Prepare the parameters to be passed
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", user)
                        .appendQueryParameter("password", pass);

                String query = builder.build().getEncodedQuery();



                // Log for debugging
                Log.v("prep", "Preparing to connect");

                String returnString = "";

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
                if (responseCode == HttpURLConnection.HTTP_OK)
                {


                    Log.v("read", "Beginning read");

                    // Create a buffered reader object to get the data from the php call
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    // Line to read into
                    String line = "";

                    // As long as there is stuff to read, append each line to the returnstring variable
                    while((line = in.readLine()) != null)
                    {
                        Log.v("read", line);
                        returnString += line;
                    }

                    //close our reader
                    in.close();

                    // If we received a positive response (i.e. the username/pass combo is valid
                    if(returnString.equals("pass"))
                    {
                        res = true;
                    }

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
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //if the user/pass is valid
            if(aBoolean)
            {
                // Move the user to their main menu
                Intent intnt = new Intent(login.this, UserMenu.class);
                startActivity(intnt);
            }
            else
            {
                // If it is invalid, notify the user of this
                Toast.makeText(getBaseContext(), "invalid Username/Password combination", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void goToMainScreen(View v) {
        EditText usr = (EditText)findViewById(R.id.username);
        EditText pwd = (EditText) findViewById(R.id.password);
        String username = usr.getText().toString();
        String password = pwd.getText().toString();

        (new verifyLogin()).execute(username, password);
    }

}
