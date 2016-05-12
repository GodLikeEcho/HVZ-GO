package redacted.hvzui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Z_tag_report extends AppCompatActivity {
    Button report;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_tag_report);

        report = (Button) findViewById(R.id.report_tag);
        report.setOnClickListener(check);
        setColor();
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = getSharedPreferences("Tag", 0);
        String  scanned= sp.getString("Scanned", "");

        EditText tagBox = (EditText)findViewById(R.id.tag_field);
        tagBox.setText(scanned);
    }

    public void scanCode(View v)
    {
        launchActivity(Z_tag_scanner.class);
    }

    View.OnClickListener check = new View.OnClickListener() {
        public void onClick(View v) {
            EditText tagBox = (EditText)findViewById(R.id.tag_field);
            String tag = tagBox.getText().toString();
            /*
            if(tag.length() != 8) {
                Toast.makeText(getBaseContext(), "tag must be 8 characters long", Toast.LENGTH_LONG).show();
            }
            else {*/
                (new reportTag()).execute(tag);
            //}
        }
    };

    private class reportTag extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            String res = "";
            String tag = params[0];
            try {
                //Connection Parameters
                URL url;
                url = new URL( "http://www.hvz-go.com/checkTag.php" );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                    // Prepare the parameters to be passed
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("userID", tag);

                    String query = builder.build().getEncodedQuery();

                    StringBuilder result = new StringBuilder();

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
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);

            Log.v("Tag", "Change faction");
            if(strings.equals("pass"))
            {
                Toast.makeText(getBaseContext(), "User tagged", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "User not tagged", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setColor()
    {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if(preferences.getBoolean("ColorBlind", false)) {
            View box1 =  this.findViewById(R.id.game_tags);
            box1.setBackgroundColor(0xffffffff);

            View box2 =  this.findViewById(R.id.tag_field);
            box2.setBackgroundColor(0xffffffff);

            View box3 =  this.findViewById(R.id.report_tag);
            box3.setBackgroundColor(0xffffffff);

        }
    }
}
