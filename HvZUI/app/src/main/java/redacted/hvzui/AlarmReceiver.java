package redacted.hvzui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Clint on 4/5/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        Z_missions missions = new Z_missions();
        // For our recurring task, we'll just display a message
        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
        Log.v("AlarmReciever", "Recieved alarm");

        //have to create local instance of activity
        Missions mission = new Missions();

        mission.new getMission().execute();



    }

}
