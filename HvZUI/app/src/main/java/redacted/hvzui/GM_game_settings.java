package redacted.hvzui;

import android.app.AlertDialog;
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
}
