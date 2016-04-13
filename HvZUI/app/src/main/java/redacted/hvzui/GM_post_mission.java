package redacted.hvzui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GM_post_mission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_post_mission);
        setColor();
    }

    public void post_mission_click(View v)
    {
        //notify the GM that the mission was posted
        Toast.makeText(getBaseContext(), "Mission Posted", Toast.LENGTH_LONG).show();

        //and reset the text fields
        EditText mTime = (EditText) findViewById(R.id.mission_time);
        EditText mDetails = (EditText) findViewById(R.id.mission_details);

        //and clear the boxes so a new mission can be entered
        mTime.setText("");
        mDetails.setText("");
    }

    public void setColor() {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("ColorBlind", false)) {
            View box1 = this.findViewById(R.id.label2);
            box1.setBackgroundColor(0xffffffff);

            View box2 = this.findViewById(R.id.mission_time);
            box2.setBackgroundColor(0xffffffff);

            View box3 = this.findViewById(R.id.post_mission);
            box3.setBackgroundColor(0xffffffff);

            View box4 = this.findViewById(R.id.textView3);
            box4.setBackgroundColor(0xffffffff);

            View box5 = this.findViewById(R.id.mission_details);
            box5.setBackgroundColor(0xffffffff);
        }
    }
}
