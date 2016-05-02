package redacted.hvzui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class Z_settings extends AppCompatActivity {

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

        if(preferences.getBoolean("ColorBlind", false) == true)
            color.setChecked(true);
        else color.setChecked(false);

    }
}
