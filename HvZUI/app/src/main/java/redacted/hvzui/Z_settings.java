package redacted.hvzui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        Switch mission = (Switch) findViewById(R.id.checkMissions);

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

        mission.setChecked(preferences.getBoolean("getMissions", false));

        mission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //commit prefs on change
                edit.putBoolean("getMissions", isChecked);
                edit.commit();
            }
        });

        if(preferences.getBoolean("ColorBlind", false) == true)
            color.setChecked(true);
        else color.setChecked(false);

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
