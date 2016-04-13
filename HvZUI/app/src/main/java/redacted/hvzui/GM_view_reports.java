package redacted.hvzui;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class GM_view_reports extends AppCompatActivity {

    private ListView lv;
    ArrayList<String> listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_view_reports);

        String type = "Harassment";
        setColor();
        (new GetReports()).execute(type);
    }

    // Async Task override function to run the networking in a new thread
    private class GetReports extends AsyncTask<String, Integer, ArrayList<String> >
    {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            Log.v("connecting", "");
            // For returning
            ArrayList<String> res = new ArrayList<String>();

            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/getReports.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestMethod("POST");

                // Prepare the parameters to be passed
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("ReportType", params[0]);

                String query = builder.build().getEncodedQuery();

                // Log for debugging
                Log.v("prep", "Preparing to connect");

                // More logging
                Log.v("connected", "successful connection, preparing to write");
                Log.v("writing", params[0]);
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

            if(!strings.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>((GM_view_reports.this), android.R.layout.simple_list_item_1, strings );
                ListView list = (ListView) findViewById(R.id.reportlist);
                list.setAdapter(adapter);
            }
            else
            {
                // If it is invalid, notify the user of this
                Toast.makeText(getBaseContext(), "Could not retrieve reports", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void SelectReportTypeClick(View v)
    {
        Spinner type = (Spinner) findViewById(R.id.reportTypeSpinner);
        String typeOfReport = type.getSelectedItem().toString();

        (new GetReports()).execute(typeOfReport);
    }

    public void setColor() {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("ColorBlind", false)) {
            View box1 = this.findViewById(R.id.reportTypeSpinner);
            box1.setBackgroundColor(0xffffffff);

            View box2 = this.findViewById(R.id.reportlist);
            box2.setBackgroundColor(0xffffffff);
        }
    }
}
