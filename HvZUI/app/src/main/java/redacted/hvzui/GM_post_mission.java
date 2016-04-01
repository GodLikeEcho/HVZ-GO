package redacted.hvzui;

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
}
