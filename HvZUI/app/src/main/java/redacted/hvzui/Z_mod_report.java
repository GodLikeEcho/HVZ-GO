package redacted.hvzui;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Z_mod_report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_mod_report);
        setColor();
    }

    private class makeReport extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String res = "";

            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/makeReport.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

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
                        res = line;
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

            if(s.equals("pass"))
            {
                Toast.makeText(getBaseContext(), "report submitted", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "submission of report failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void zreport(View v)
    {
        EditText details = (EditText)findViewById(R.id.reportDetails);
        Spinner reason = (Spinner)findViewById(R.id.spinner_ViolationType);

        String reportDetails = details.getText().toString();
        String reportReason = reason.getSelectedItem().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String reportMadeMy = prefs.getString("username", "");

        (new makeReport()).execute(reportMadeMy, reportDetails, reportReason);

        details.setText("");
    }

    public void setColor()
    {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if(preferences.getBoolean("ColorBlind", false)) {
            View box1 =  this.findViewById(R.id.spinner_ViolationType);
            box1.setBackgroundColor(0xffffffff);

            View box2 =  this.findViewById(R.id.reportDetails);
            box2.setBackgroundColor(0xffffffff);

            View box3 =  this.findViewById(R.id.SendReport);
            box3.setBackgroundColor(0xffffffff);

        }
    }
}
