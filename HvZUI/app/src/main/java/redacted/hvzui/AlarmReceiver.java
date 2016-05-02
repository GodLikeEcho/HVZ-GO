package redacted.hvzui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Clint on 4/5/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        // For our recurring task, we'll just display a message
        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
        Log.v("AlarmReciever", "Recieved alarm");

        //have to create local instance of activity
        Z_missions mission = new Z_missions();
        Z_user_menu starve = new Z_user_menu();
        //mission.new getMission().execute();
        //starve.new starveTimer().execute();



    }

}
