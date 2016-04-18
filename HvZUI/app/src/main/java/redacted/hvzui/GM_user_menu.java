package redacted.hvzui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GM_user_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_user_menu);

        setColor();
    }

    public void GM_ban_player_Click(View v)
    {
        Intent intnt = new Intent(this, GM_ban_player.class);
        startActivity(intnt);
    }

    public void GM_post_mission_Click(View v)
    {
        Intent intnt = new Intent(this, GM_post_mission.class);
        startActivity(intnt);
    }

    public void GM_post_alert_Click(View v)
    {
        Intent intnt = new Intent(this, GM_make_alert.class);
        startActivity(intnt);
    }

    public void GM_view_reports_Click(View v)
    {
        Intent intnt = new Intent(this, GM_view_reports.class);
        startActivity(intnt);
    }

    public void GM_edit_settings_click(View v)
    {
        Intent intnt = new Intent(this, GM_game_settings.class);
        startActivity(intnt);
    }

    public void GM_chat_Click(View v)
    {
        Intent intnt = new Intent(this, GM_chat_room.class);
        startActivity(intnt);
    }

    public void setColor() {
        //creates an instance of the global preferences
        String PREF_FILE_NAME = "PrefFile";
        final SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean("ColorBlind", false)) {
            View box1 = this.findViewById(R.id.TimerBox);
            box1.setBackgroundColor(0xffffffff);

            View box2 = this.findViewById(R.id.view_reports);
            box2.setBackgroundColor(0xffffffff);

            View box3 = this.findViewById(R.id.edit_settings);
            box3.setBackgroundColor(0xffffffff);

            View box4 = this.findViewById(R.id.ban_players);
            box4.setBackgroundColor(0xffffffff);

            View box5 = this.findViewById(R.id.post_mission);
            box5.setBackgroundColor(0xffffffff);

            View box7 = this.findViewById(R.id.post_mission);
            box7.setBackgroundColor(0xffffffff);

            View box8 = this.findViewById(R.id.chat);
            box8.setBackgroundColor(0xffffffff);

        }
    }
}
