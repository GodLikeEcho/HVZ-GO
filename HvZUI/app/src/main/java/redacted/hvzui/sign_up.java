package redacted.hvzui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setColor();
    }

    private class AddPlayer extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params) {
            // For returning
            String res = "";
            Log.v("enter", "entered thread");

            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/appRegister.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                // Prepare the parameters to be passed
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1])
                        .appendQueryParameter("email", params[2]);

                String query = builder.build().getEncodedQuery();

                StringBuilder result = new StringBuilder();
                boolean first = true;

                // Log for debugging
                Log.v("prep", "Preparing to connect");

                Log.v("connected", "successful connection, preparing to write");

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter wr = new OutputStreamWriter(os, "UTF-8");

                // Write the params and clean up
                wr.write(query);
                wr.flush();
                wr.close();
                os.close();

                // Get a response from the server
                int responseCode = conn.getResponseCode();

                // If the response is the one we are looking for
                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    // More logging


                    conn.connect();

                    Log.v("write", "Write finished");

                    Log.v("read", "Beginning read");

                    // Create a buffered reader object to get the data from the php call
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    // Line to read into
                    String line = "";

                    // As long as there is stuff to read, append each line to the returnstring variable
                    while((line = in.readLine()) != null)
                    {
                        Log.v("returned", line);
                        res += line;
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v("s = ", s);

            Intent intnt = new Intent(sign_up.this, login.class);
            startActivity(intnt);

        }
    }

    //static pattern for matching emails


    //checker for email validation
    private boolean checkEmail(String email)
    {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();

    }

    //onclick event for signing up, verifys information matches before sending it on
    public void verify_and_send(View v){

        //get each edittextbox from the form
        EditText usernameBox = (EditText)findViewById(R.id.username);
        EditText passwordBox = (EditText)findViewById(R.id.password);
        EditText confirm_passwordBox = (EditText)findViewById(R.id.confirm_password);
        EditText emailBox = (EditText)findViewById(R.id.email);
        EditText confirm_emailBox = (EditText)findViewById(R.id.confirm_email);

        //and get the text from each field
        String username = usernameBox.getText().toString();
        String password = passwordBox.getText().toString();
        String conf_password = confirm_passwordBox.getText().toString();
        String email = emailBox.getText().toString();
        String conf_email = confirm_emailBox.getText().toString();

        //verify passwords and emails match
        if (!(conf_password.equals(password)))
        {
            Toast.makeText(getBaseContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
        }
        else if (!(email.equals(conf_email)))
        {
            Toast.makeText(getBaseContext(), "Emails do not match", Toast.LENGTH_LONG).show();
        }
        else if(!checkEmail(email))
        {
            Toast.makeText(getBaseContext(), "Email is not valid", Toast.LENGTH_LONG).show();
        }
        else
        {
            (new AddPlayer()).execute(username, password, email);
            //package and send the data to server for verification and addtion to the database
        }
    }

    public void setColor()
    {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if(preferences.getBoolean("ColorBlind", false)) {
            View box1 =  this.findViewById(R.id.username);
            box1.setBackgroundColor(0xffffffff);

            View box2 =  this.findViewById(R.id.password);
            box2.setBackgroundColor(0xffffffff);

            View box3 =  this.findViewById(R.id.confirm_password);
            box3.setBackgroundColor(0xffffffff);

            View box4 =  this.findViewById(R.id.email);
            box4.setBackgroundColor(0xffffffff);

            View box5 =  this.findViewById(R.id.confirm_email);
            box5.setBackgroundColor(0xffffffff);

            View box6 =  this.findViewById(R.id.sign_up);
            box6.setBackgroundColor(0xffffffff);

        }
    }
}
