package redacted.hvzui;

import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class Z_rules extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_rules);

        //read the rules text file and display it
        StringBuilder text = new StringBuilder();
        setColor();

        try {
            //find the text file stored in our assets folder
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("ZombieRules.txt")));
            //new string for reading lines into
            String line;
            //loop through the file and read the rules
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Error reading file!",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        //set the textviews text to those read from the rules
        TextView output=(TextView) findViewById(R.id.Zrules);

        output.setText(text);

    }

    public void setColor() {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("ColorBlind", false)) {
            View box1 = this.findViewById(R.id.Zrules);
            box1.setBackgroundColor(0xffffffff);

        }
    }
}
