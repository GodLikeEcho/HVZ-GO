package redacted.hvzui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class ModReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_report);
    }

    public void send_Report(View v)
    {
        final Spinner spin = (Spinner) findViewById(R.id.spinner_ViolationType);
        final String report_type = String.valueOf(spin.getSelectedItem());
        Button rpt = (Button) findViewById(R.id.SendReport);
        rpt.setText("Report Sent");
    }
}
