package redacted.hvzui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class UserMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setColor();
    }

    public void report_Click(View v)
    {
        Intent intnt = new Intent(this, ModReport.class);
        startActivity(intnt);
    }

    public void missions_Click(View v)
    {
        Intent intnt = new Intent(this, Missions.class);
        startActivity(intnt);
    }

    public void rules_Click(View v)
    {
        Intent intnt = new Intent(this, Rules.class);
        startActivity(intnt);
    }

    public void H_chat_Click(View v)
    {
        Intent intnt = new Intent(this, H_chat_room.class);
        startActivity(intnt);
    }

    public void A_chat_Click(View v)
    {
        Intent intnt = new Intent(this, A_chat_room.class);
        startActivity(intnt);
    }

    public void h_settings_Click(View v)
    {
        Intent intnt = new Intent(this, Z_settings.class);
        startActivity(intnt);
    }

    public void setColor()
    {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if(preferences.getBoolean("ColorBlind", false)) {
            View box1 =  this.findViewById(R.id.TimerBox);
            box1.setBackgroundColor(0xffffffff);

            View box2 =  this.findViewById(R.id.button2);
            box2.setBackgroundColor(0xffffffff);

            View box3 =  this.findViewById(R.id.button3);
            box3.setBackgroundColor(0xffffffff);

            View box4 =  this.findViewById(R.id.Rules);
            box4.setBackgroundColor(0xffffffff);

            View box5 =  this.findViewById(R.id.button);
            box5.setBackgroundColor(0xffffffff);

            View box6 =  this.findViewById(R.id.Moderate);
            box6.setBackgroundColor(0xffffffff);

            View box7 =  this.findViewById(R.id.settings);
            box7.setBackgroundColor(0xffffffff);

        }
    }
}
