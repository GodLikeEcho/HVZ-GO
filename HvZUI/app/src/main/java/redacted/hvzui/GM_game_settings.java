package redacted.hvzui;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class GM_game_settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_game_settings);
        setColor();
    }

    public void update_settings_click(View v)
    {
        //get the text boxes containing the settings
        EditText timeperkill = (EditText) findViewById(R.id.time_per_kill);
        EditText stuntime = (EditText) findViewById(R.id.stun_timer_duration);
        EditText mandatorymissions = (EditText) findViewById(R.id.number_of_required_missions);

        //extract the strings from them
        String time_added_per = timeperkill.getText().toString();
        String stun_time = stuntime.getText().toString();
        String required_attendance = mandatorymissions.getText().toString();

        //notify the GM of the changes
        AlertDialog.Builder builder = new AlertDialog.Builder(GM_game_settings.this);
        builder.setTitle("Settings updated")
                .setMessage("Hours added to starve timer per kill:"+time_added_per+
                            "\nMinutes on stun timer: "+stun_time+
                            "\nMinimum acceptable mission attendance: "+required_attendance+"")
                .setNeutralButton("OK", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void setColor() {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("ColorBlind", false)) {
            View box1 = this.findViewById(R.id.label1);
            box1.setBackgroundColor(0xffffffff);

            View box2 = this.findViewById(R.id.time_per_kill);
            box2.setBackgroundColor(0xffffffff);

            View box3 = this.findViewById(R.id.label2);
            box3.setBackgroundColor(0xffffffff);

            View box4 = this.findViewById(R.id.stun_timer_duration);
            box4.setBackgroundColor(0xffffffff);

            View box5 = this.findViewById(R.id.label3);
            box5.setBackgroundColor(0xffffffff);

            View box6 = this.findViewById(R.id.number_of_required_missions);
            box6.setBackgroundColor(0xffffffff);

            View box7 = this.findViewById(R.id.update_settings);
            box7.setBackgroundColor(0xffffffff);

        }
    }
}
