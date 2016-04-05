package redacted.hvzui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Z_user_menu extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_user_menu);

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        setColor();
        startAlarm();
    }

    public void startAlarm() {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 4000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        Log.v("startAlarm", "In alarm starter");
    }

    public void beginTimer(View v){

        //create thread to update every second

        Thread t = new Thread() {

            int stuntimer = 100;
            @Override
            //function to run every thread tick
            public void run() {
                try {
                    while (!isInterrupted()) {
                        //can change this to change the thread inverval
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            //actual statement to do an action every tick
                            public void run() {
                                TextView txt = (TextView)findViewById(R.id.TimerBox);
                                txt.setText(""+stuntimer+" seconds");
                                --stuntimer;
                            }
                        });
                    }
                    if(stuntimer == 0)
                    {
                        //crete an aliert when the stun timer runs out
                        AlertDialog.Builder builder = new AlertDialog.Builder(Z_user_menu.this);
                        builder.setTitle("Stun timer ended")
                                .setMessage("Your stun timer has expired, get hunting")
                                .setNeutralButton("OK", null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                } catch (InterruptedException e) {
                }
            }

        };

        t.start();


    }

    public void Z_report_Click(View v)
    {
        Intent intnt = new Intent(this, Z_mod_report.class);
        startActivity(intnt);
    }

    public void Z_missions_Click(View v)
    {
        Intent intnt = new Intent(this, Z_missions.class);
        startActivity(intnt);
    }

    public void Z_rules_Click(View v)
    {
        Intent intnt = new Intent(this, Z_rules.class);
        startActivity(intnt);
    }

    public void Z_tag_click(View v)
    {
        Intent intnt = new Intent(this, Z_tag_report.class);
        startActivity(intnt);
    }

    public void Z_chat_Click(View v)
    {
        Intent intnt = new Intent(this, Z_chat_room.class);
        startActivity(intnt);
    }

    public void A_chat_Click(View v)
    {
        Intent intnt = new Intent(this, A_chat_room.class);
        startActivity(intnt);
    }

    public void z_settings_Click(View v)
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

            View box2 =  this.findViewById(R.id.StunTimer);
            box2.setBackgroundColor(0xffffffff);

            View box3 =  this.findViewById(R.id.Rules);
            box3.setBackgroundColor(0xffffffff);

            View box4 =  this.findViewById(R.id.reportTag);
            box4.setBackgroundColor(0xffffffff);

            View box5 =  this.findViewById(R.id.Chat);
            box5.setBackgroundColor(0xffffffff);

            View box6 =  this.findViewById(R.id.Mission);
            box6.setBackgroundColor(0xffffffff);

            View box7 =  this.findViewById(R.id.button4);
            box7.setBackgroundColor(0xffffffff);

            View box8 =  this.findViewById(R.id.settingsbutton);
            box8.setBackgroundColor(0xffffffff);

            View box9 =  this.findViewById(R.id.Moderate);
            box9.setBackgroundColor(0xffffffff);
        }
    }
}
